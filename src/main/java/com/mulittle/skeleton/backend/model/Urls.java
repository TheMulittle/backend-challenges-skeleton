package com.mulittle.skeleton.backend.model;

public class Urls {
  public static final String BASE_URL;

  static {
    BASE_URL = System.getProperty("BASE_URL");
  }
}
