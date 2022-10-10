package com.mulittle.skeleton.backend.reporters;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.GherkinKeyword;
import com.aventstack.extentreports.gherkin.model.Feature;
import com.aventstack.extentreports.gherkin.model.Scenario;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import io.cucumber.messages.types.TestCaseFinished;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.Result;
import io.cucumber.plugin.event.Step;
import io.cucumber.plugin.event.TestCaseStarted;
import io.cucumber.plugin.event.TestRunFinished;
import io.cucumber.plugin.event.TestRunStarted;
import io.cucumber.plugin.event.TestSourceRead;
import io.cucumber.plugin.event.TestStepFinished;
import io.cucumber.plugin.event.TestStepStarted;

public class Reporter implements ConcurrentEventListener {

    private final ExtentSparkReporter spark;
    private final ExtentReports extent;

    Map<String, ExtentTest> features = new ConcurrentHashMap<String, ExtentTest>();
    Map<String, ExtentTest> scenarios = new ConcurrentHashMap<String, ExtentTest>();

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
        features.put(event.getUri().toString(), extent.createTest(Feature.class, matcher.group(1).trim()));
    };


    private void scenarioStarted(TestCaseStarted event) {
        ExtentTest feature = features.get(event.getTestCase().getUri().toString());
        ExtentTest scenario = feature.createNode(Scenario.class, event.getTestCase().getName());
        scenarios.put(event.getTestCase().getName(), scenario);
    };

    private void scenarioFinished(TestCaseFinished event) {

    };

    private void stepStarted(TestStepStarted event) {

    };


    private void stepFinished(TestStepFinished event) throws ClassNotFoundException {
        if (event.getTestStep() instanceof PickleStepTestStep) {
            ExtentTest scenario = scenarios.get(event.getTestCase().getName()); 
            PickleStepTestStep stepEvent = (PickleStepTestStep) event.getTestStep();
            Step step = stepEvent.getStep();
            Result stepResult = event.getResult();
            ExtentTest stepNode = scenario.createNode(new GherkinKeyword(step.getKeyword()), step.getText());


            if(stepResult.getStatus().is(io.cucumber.plugin.event.Status.PASSED)) {
                stepNode.log(com.aventstack.extentreports.Status.PASS,"Passed");
            } else {
                stepNode.log(com.aventstack.extentreports.Status.FAIL, "Failed");
            }
        }
    };


}