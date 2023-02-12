package com.mulittle.skeleton.backend.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractAttachment implements Serializable {

  protected final String type;
  protected String endpoint;
  protected String header;
  protected String body;
  
  public AbstractAttachment(String type) {
    this.type = type;
  }

  public abstract String metaDataToString();
}
