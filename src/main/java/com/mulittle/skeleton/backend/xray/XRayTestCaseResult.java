package com.mulittle.skeleton.backend.xray;

import java.util.LinkedList;
import java.util.List;

import io.cucumber.plugin.event.Status;
import lombok.Data;

@Data
public class XRayTestCaseResult {
  private final List<XRayTestStepResult> stepResults = new LinkedList<>();
  private static final String DIVISION = "\n";

  public Boolean passed() {
    return stepResults.stream().allMatch(stepResult -> stepResult.getStatus().equals(Status.PASSED));
  }

  public String comment() {
    return stepResults.stream()
      .map(stepResult -> stepResult.comment())
      .reduce("", (testCaseComment, testStepComment) -> testCaseComment + testStepComment + DIVISION);
  }
}
