package com.mulittle.skeleton.backend.integration;

import org.springframework.util.SerializationUtils;

import com.mulittle.skeleton.backend.context.EvidenceContext;

import io.cucumber.java.AfterStep;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.Scenario;

public class Hooks {

  public EvidenceContext evidenceContext;

  public Hooks(EvidenceContext evidenceContext) {
    this.evidenceContext = evidenceContext;
  }

  @BeforeStep
  public void clearEvidenceContext() {
    evidenceContext.getAttachments().clear();
  }

  @AfterStep
  public void attachRequest(Scenario scenario) {
    scenario.attach(SerializationUtils.serialize(evidenceContext.getAttachments()), "application/json", null);
  }
  
}
