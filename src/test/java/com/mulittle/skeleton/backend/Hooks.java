package com.mulittle.skeleton.backend;

import com.mulittle.skeleton.backend.context.StepContext;

import io.cucumber.java.AfterStep;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.Scenario;

public class Hooks {

  public StepContext stepsContext;

  public Hooks(StepContext stepsContext) {
    this.stepsContext = stepsContext;
  }

  @BeforeStep
  public void register() {
    
  }
  
}
