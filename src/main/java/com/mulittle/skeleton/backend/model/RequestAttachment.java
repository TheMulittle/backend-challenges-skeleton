package com.mulittle.skeleton.backend.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestAttachment extends AbstractAttachment {
  private String url;
  private String method;
  
  public RequestAttachment() {
    super("Request");
  }

  @Override
  public String metaDataToString() {
    return url + " - " + method + "\n" + header;
  }
}
