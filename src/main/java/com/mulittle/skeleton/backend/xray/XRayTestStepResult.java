package com.mulittle.skeleton.backend.xray;

import java.util.LinkedList;
import java.util.List;

import io.cucumber.plugin.event.Status;
import lombok.Data;

@Data
public class XRayTestStepResult {
  private final List<String> arguments = new LinkedList<>();
  private final List<String> attachementsInternalIds = new LinkedList<>();
  private final Status status;
  private final XRayCommentBuilder comment;

  public String comment() {
    return comment.build();
  }
}
