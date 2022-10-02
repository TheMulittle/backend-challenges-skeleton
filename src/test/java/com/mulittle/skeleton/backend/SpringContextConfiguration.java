package com.mulittle.skeleton.backend;

import org.springframework.test.context.ContextConfiguration;

import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration 
@ContextConfiguration(classes = SpringConfiguration.class)
public class SpringContextConfiguration { 

}
