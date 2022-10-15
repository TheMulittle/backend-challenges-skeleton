package com.mulittle.skeleton.backend.reporters;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import io.cucumber.messages.types.TestCaseFinished;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EmbedEvent;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.Result;
import io.cucumber.plugin.event.Step;
import io.cucumber.plugin.event.TestCaseStarted;
import io.cucumber.plugin.event.TestRunFinished;
import io.cucumber.plugin.event.TestRunStarted;
import io.cucumber.plugin.event.TestSourceRead;
import io.cucumber.plugin.event.TestStep;
import io.cucumber.plugin.event.TestStepFinished;
import io.cucumber.plugin.event.TestStepStarted;

public class Reporter implements ConcurrentEventListener {

    private final ExtentSparkReporter spark;
    private final ExtentReports extent;

    private static Map<String, ExtentTest> features = new ConcurrentHashMap<String, ExtentTest>();
    private static Map<String, ExtentTest> scenarios = new ConcurrentHashMap<String, ExtentTest>();
    private static ThreadLocal<TestStep> testStep = new ThreadLocal<>();
    private static ThreadLocal<Result> testStepResult = new ThreadLocal<>();

    public Reporter(String path) {
        spark = new ExtentSparkReporter(path);
        extent = new ExtentReports();
    }

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestRunStarted.class, this::runStarted);
        publisher.registerHandlerFor(TestRunFinished.class, this::runFinished);
        publisher.registerHandlerFor(TestSourceRead.class, this::featureRead);
        publisher.registerHandlerFor(TestCaseStarted.class, this::scenarioStarted);
        publisher.registerHandlerFor(TestStepStarted.class, this::stepStarted);
        publisher.registerHandlerFor(TestStepFinished.class, event -> {
            try {
                stepFinished(event);
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        publisher.registerHandlerFor(EmbedEvent.class, event -> {
            try {
                embed(event);
            } catch (ClassNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
    };



    private void runStarted(TestRunStarted event) {
        extent.attachReporter(spark);
    };


    private void runFinished(TestRunFinished event) {
        extent.flush();
    };


    private void featureRead(TestSourceRead event) {
        Pattern pattern = Pattern.compile("Feature:(.*)");
        Matcher matcher = pattern.matcher(event.getSource());
        matcher.find();
        features.put(event.getUri().toString(), extent.createTest(matcher.group(1).trim()));
    };


    private void scenarioStarted(TestCaseStarted event) {
        ExtentTest feature = features.get(event.getTestCase().getUri().toString());
        ExtentTest scenario = feature.createNode(event.getTestCase().getName());
        scenarios.put(event.getTestCase().getName(), scenario);
    };

    private void scenarioFinished(TestCaseFinished event) {
        
    };

    private void stepStarted(TestStepStarted event) {
        if(event.getTestStep() instanceof PickleStepTestStep) {
            testStep.set(event.getTestStep());
        }
    };


    private void stepFinished(TestStepFinished event) throws ClassNotFoundException {
        if(event.getTestStep() instanceof PickleStepTestStep) {
            testStepResult.set(event.getResult());
        }
    };

    private void embed(EmbedEvent event) throws ClassNotFoundException {
        if (testStep.get() instanceof PickleStepTestStep) {
            PickleStepTestStep pickleTestStep = (PickleStepTestStep) testStep.get();
            Step step = pickleTestStep.getStep();
            ExtentTest scenario = scenarios.get(event.getTestCase().getName()); 
            ExtentTest stepNode = scenario.createNode(step.getKeyword() + " " + step.getText());
            io.cucumber.plugin.event.Status resultStatus = testStepResult.get().getStatus();
            updateStepNodeResult(stepNode, resultStatus);
            addPayloadInformation(stepNode, new String(event.getData()));
        }
    }

    private void updateStepNodeResult(ExtentTest stepNode, io.cucumber.plugin.event.Status resultStatus) {
        switch (resultStatus) {
            case PASSED:
                stepNode.fail("Step passed");
                break;
            case FAILED:
                stepNode.skip("Step skipped");
                break;
            case SKIPPED:
                stepNode.skip("Step skipped");
                break;
            case PENDING:
            case UNDEFINED:
                stepNode.fail("Step not implemented");
                break;
            default:
                break;
        }
    }

    private void addPayloadInformation(ExtentTest stepNode, String payload) {
        stepNode.info(MarkupHelper.createCodeBlock(payload, CodeLanguage.JSON));
    }
}