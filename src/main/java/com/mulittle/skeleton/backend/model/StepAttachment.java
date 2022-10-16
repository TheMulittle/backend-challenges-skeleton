package com.mulittle.skeleton.backend.model;

import lombok.Getter;

@Getter
public class StepAttachment {
  RequestAttachment attachment;
  String stepName;
  String scenarioName;
}
