package com.mulittle.skeleton.backend.integration;

import org.springframework.test.context.ContextConfiguration;

import com.mulittle.skeleton.backend.AutomationConfiguration;

import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration 
@ContextConfiguration(classes = {AutomationConfiguration.class})
public class SpringContextConfiguration { 

}
