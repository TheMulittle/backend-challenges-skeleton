package com.mulittle.skeleton.backend.reporters;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.util.SerializationUtils;

import com.graphql.generated.AttachmentDataInput;
import com.graphql_java_generator.exception.GraphQLRequestExecutionException;
import com.graphql_java_generator.exception.GraphQLRequestPreparationException;
import com.mulittle.skeleton.backend.model.AbstractAttachment;
import com.mulittle.skeleton.backend.xray.XRayCommentBuilder;
import com.mulittle.skeleton.backend.xray.XRayService;
import com.mulittle.skeleton.backend.xray.XRayTestCaseResult;
import com.mulittle.skeleton.backend.xray.XRayTestStepResult;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EmbedEvent;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.Status;
import io.cucumber.plugin.event.TestCaseFinished;
import io.cucumber.plugin.event.TestCaseStarted;
import io.cucumber.plugin.event.TestRunFinished;
import io.cucumber.plugin.event.TestRunStarted;
import io.cucumber.plugin.event.TestStepFinished;
import lombok.extern.slf4j.Slf4j;

// TODO: this is a raw version of the reporter, further PRs will enchance it
@Slf4j
public class XRayReporter implements ConcurrentEventListener {

    private static String testExecutionId = System.getProperty("testExecution");
    private static String XRAY_BASE_URL = System.getProperty("xRayBaseUrl");
    private static XRayService xRayService;
    private static ThreadLocal<String> internalTestExecutionId = new ThreadLocal<>();
    private static ThreadLocal<String> testRunInternalIdLocal = new ThreadLocal<>();
    private static ThreadLocal<XRayTestStepResult> currentTestStepResult = new ThreadLocal<>();
    private static ConcurrentHashMap<String, ConcurrentHashMap<String, XRayTestCaseResult>> testCaseResults = new ConcurrentHashMap<>();
    
    
    @Override
    public void setEventPublisher(EventPublisher publisher) {

       if(testExecutionId == null || testExecutionId.isEmpty()) {
            log.info("Disabling XRay reporter as there isn't a -DtestExecution property set");
            return;
        }

        if(XRAY_BASE_URL == null) {
            throw new RuntimeException("-DtestExecution parameter has been provided, but -DxRayBaseUrl is missing");
        }

        publisher.registerHandlerFor(TestRunStarted.class, event -> {
            try {
                runStarted(event);
            } catch (GraphQLRequestExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (GraphQLRequestPreparationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        publisher.registerHandlerFor(TestRunFinished.class, this::runFinished);
        publisher.registerHandlerFor(TestCaseStarted.class, event -> {
            try {
                scenarioStarted(event);
            } catch (GraphQLRequestExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        publisher.registerHandlerFor(TestCaseFinished.class, event -> {
            try {
                scenarioFinished(event);
            } catch (GraphQLRequestExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        publisher.registerHandlerFor(TestStepFinished.class, event -> {
            try {
                stepFinished(event);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (GraphQLRequestExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        publisher.registerHandlerFor(EmbedEvent.class, event -> {
            try {
                embed(event);
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            } catch (GraphQLRequestExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    };

    private void runStarted(TestRunStarted event) throws GraphQLRequestExecutionException, GraphQLRequestPreparationException {
        xRayService = new XRayService();
        xRayService.authenticate(XRAY_BASE_URL + "/v1/authenticate");
        xRayService.setup(XRAY_BASE_URL + "/v2/graphql");
            
        String testExecutionInternalId = xRayService.getTestExecutionInternalId(testExecutionId);

        if(testExecutionInternalId == null) {
            throw new RuntimeException("Test execution '%s' does not exist in JIRA".formatted(testExecutionId));
        }

        internalTestExecutionId.set(testExecutionInternalId);
    };


    private void runFinished(TestRunFinished event) {
        
    };

    private void scenarioStarted(TestCaseStarted event) throws GraphQLRequestExecutionException {
        //TODO Use regex
        Optional<String> jiraTestCaseTag = event.getTestCase().getTags().stream().filter(tag -> tag.startsWith("@TC:")).findFirst();
       
        if(!jiraTestCaseTag.isPresent()) {
            log.warn("Test scenario does not have tag starting with '@TC:'' ");
            return;
        }

        String testCaseId = jiraTestCaseTag.get().replace("@TC:", "");

        String internalTestCaseId = xRayService.getTestCaseInternalId(testCaseId);

        if(internalTestCaseId == null) {
            log.error("Test case '{1}' is not linked to test execution", testCaseId);
        }

        String testRunInternalId = xRayService.getTestRunInternalId(internalTestCaseId, internalTestExecutionId.get());
        testRunInternalIdLocal.set(testRunInternalId);
        testCaseResults.putIfAbsent(testRunInternalId, new ConcurrentHashMap<>());
        testCaseResults.get(testRunInternalId).putIfAbsent(event.getTestCase().getId().toString(),
                new XRayTestCaseResult());
    };

    

    private void stepFinished(TestStepFinished event) throws ClassNotFoundException, GraphQLRequestExecutionException {
        if (testRunInternalIdLocal.get() == null) {
            return;
        }

        if (event.getTestStep() instanceof PickleStepTestStep) {
            Status status = event.getResult().getStatus();
            PickleStepTestStep testStep = (PickleStepTestStep) event.getTestStep();
            XRayCommentBuilder commentBuilder = new XRayCommentBuilder()
                .textWithColorForStatus(testStep.getStep().getKeyword() + " " + testStep.getStep().getText(), status)
                .newLine();
            
            XRayTestStepResult testStepResult = new XRayTestStepResult(status, commentBuilder);
            currentTestStepResult.set(testStepResult);
            testCaseResults.get(testRunInternalIdLocal.get()).get(event.getTestCase().getId().toString())
                    .getStepResults().add(testStepResult);

        }
    };

    private void scenarioFinished(TestCaseFinished event) throws GraphQLRequestExecutionException {

        String testRunInternalId = testRunInternalIdLocal.get();

        if (testRunInternalId == null) {
            return;
        }

        updateTestRunStatus(testRunInternalId);
        addTestRunComment(testRunInternalId);
    }

    private void updateTestRunStatus(String testRunInternalId) throws GraphQLRequestExecutionException {
        String passed = testCaseResults
                .get(testRunInternalId)
                .entrySet()
                .stream()
                .allMatch(entry -> entry.getValue().passed()) ? "PASSED" : "FAILED";

        xRayService.updateTestRunStatus(testRunInternalId, passed);
    }

    private void addTestRunComment(String testRunInternalId) throws GraphQLRequestExecutionException {
        String comment = testCaseResults.get(testRunInternalId).values().stream()
                .map(testRunResult -> testRunResult.comment())
                .reduce("", (testExecutionComment, testCaseComment) -> testExecutionComment + testCaseComment);

        xRayService.updateTestRunComment(testRunInternalId, comment);
    }

    private void embed(EmbedEvent event) throws ClassNotFoundException, GraphQLRequestExecutionException {
        if (testRunInternalIdLocal.get() == null) {
            return;
        }

        List<AbstractAttachment> attachments = (List<AbstractAttachment>) SerializationUtils.deserialize(event.getData());

        List<AttachmentDataInput> xRayAttachments = abstractAttachmentToXRayAttachment(event, attachments);

        xRayService.attach(testRunInternalIdLocal.get(), xRayAttachments);


        XRayCommentBuilder comment = currentTestStepResult.get().getComment();
        IntStream.range(0, xRayAttachments.size())
                .forEach(index -> {
                    comment.textForAttachment(xRayAttachments.get(index).getFilename());
                });
    }

    private List<AttachmentDataInput> abstractAttachmentToXRayAttachment(EmbedEvent event, List<AbstractAttachment> attachments) {
        return attachments.stream()
                .filter(attachment -> attachment.getBody() != null)
                .map(attachment -> {
                    AttachmentDataInput att = new AttachmentDataInput();
                    att.setFilename(event.getInstant() + " - " + attachment.getType());
                    att.setMimeType(event.getMediaType());
                    att.setData(Base64.getEncoder().encodeToString(attachment.getBody().getBytes()));
                    return att;
                })
                .collect(Collectors.toList());
    }
}