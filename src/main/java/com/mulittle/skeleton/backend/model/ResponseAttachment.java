package com.mulittle.skeleton.backend.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseAttachment extends AbstractAttachment  {
  private String status;
  
  public ResponseAttachment() {
    super("Response");
  }

  @Override
  public String metaDataToString() {
    return status + "\n" + header;
  }
}
