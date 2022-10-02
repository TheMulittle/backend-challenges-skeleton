package com.mulittle.skeleton.backend;

import com.mulittle.skeleton.backend.context.StepsContext;

import io.cucumber.java.AfterStep;
import io.cucumber.java.BeforeStep;

public class Hooks {

  public StepsContext stepsContext;

  public Hooks(StepsContext stepsContext) {
    this.stepsContext = stepsContext;
  }

  @BeforeStep
  public void register() {

  }
  
  @AfterStep
  public void resetStepContext() {
    stepsContext.body = null;
  }
}
