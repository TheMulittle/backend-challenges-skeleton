package com.mulittle.skeleton.backend.integration;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;

import com.mulittle.skeleton.backend.AutomationConfiguration;

import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration 
@ContextConfiguration(classes = {AutomationConfiguration.class})
public class SpringContextConfiguration { 

}
