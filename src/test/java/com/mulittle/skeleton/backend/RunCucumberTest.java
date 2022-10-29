package com.mulittle.skeleton.backend;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("com/mulittle/skeleton/backend")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, html:build/cucumber-report.html, com.mulittle.skeleton.backend.reporters.HtmlReporter:build/extentReport/Spark.html")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.mulittle.skeleton.backend")
public class RunCucumberTest {
}
