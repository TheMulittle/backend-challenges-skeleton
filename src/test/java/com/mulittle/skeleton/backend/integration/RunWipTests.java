package com.mulittle.skeleton.backend.integration;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.FILTER_TAGS_PROPERTY_NAME;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("com/mulittle/skeleton/backend/integration")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, html:build/cucumber-report.html, com.mulittle.skeleton.backend.reporters.HtmlReporter:build/extentReport/Spark.html, com.mulittle.skeleton.backend.reporters.XRayReporter")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.mulittle.skeleton.backend.integration")
@ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "@wip")
public class RunWipTests {
}
