package com.mulittle.skeleton.backend.model;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Response {
  String body;
  HttpHeaders headers;
  HttpStatusCode statusCode;
}
