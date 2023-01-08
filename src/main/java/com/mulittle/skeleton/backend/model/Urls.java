package com.mulittle.skeleton.backend.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class Urls {

  public Urls(@Value("${baseUrl:http://localhost:8001/v1}") String baseUrl) {
    this.baseUrl = baseUrl;
  }

  public String baseUrl;
}
