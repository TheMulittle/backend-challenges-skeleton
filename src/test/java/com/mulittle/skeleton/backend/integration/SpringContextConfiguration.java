package com.mulittle.skeleton.backend.integration;

import org.springframework.test.context.ContextConfiguration;

import com.mulittle.skeleton.backend.SpringConfiguration;

import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration 
@ContextConfiguration(classes = SpringConfiguration.class)
public class SpringContextConfiguration { 

}
