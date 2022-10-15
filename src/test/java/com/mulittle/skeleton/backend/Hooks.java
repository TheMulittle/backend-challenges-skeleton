package com.mulittle.skeleton.backend;

import com.mulittle.skeleton.backend.context.StepContext;

import io.cucumber.java.AfterStep;
import io.cucumber.java.Scenario;

public class Hooks {

  public StepContext stepContext;

  public Hooks(StepContext stepContext) {
    this.stepContext = stepContext;
  }

  @AfterStep
  public void attachRequest(Scenario scenario) {
    scenario.attach(stepContext.body, "application/json",null);
  }
  
}
