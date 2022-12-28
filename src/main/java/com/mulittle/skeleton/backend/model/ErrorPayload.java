package com.mulittle.skeleton.backend.model;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ErrorPayload implements Serializable {
  private String err;   
}
