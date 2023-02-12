package com.mulittle.skeleton.backend.xray;

import io.cucumber.plugin.event.Status;

public class XRayCommentBuilder {

  private final static String COLOR_TEXT = "{color:#%s}%s{color}";
  private final static String BULLET_POINT = "* ";
  private final static String LINK = "[%s|%s]";

  private final StringBuilder comment = new StringBuilder();

  public enum Color {
    RED("c05454"),
    GREEN("0bba71");

    String value;

    Color(String value) {this.value = value;}

    String getValue() {return value;}
  }

  public synchronized String build() {
      return comment.toString();
  }

  public synchronized  XRayCommentBuilder text(String text) {
    comment.append(text);

    return this;
}

  public synchronized  XRayCommentBuilder textWithColor(String text, Color color) {
      comment.append(COLOR_TEXT.formatted(color.getValue(), text));

      return this;
  }
  
  public synchronized  XRayCommentBuilder textWithColorForStatus(String text, Status status) {

    Color color = Status.PASSED.equals(status) ? Color.GREEN : Color.RED;

    comment.append(COLOR_TEXT.formatted(color.getValue(), text));

    return this;
  }

  public synchronized XRayCommentBuilder newLine() {
      comment.append("\n");

      return this;
  }

  public synchronized XRayCommentBuilder link(String text, String link) {
      comment.append(LINK.formatted(text, link));

      return this;
  }

  public synchronized  XRayCommentBuilder textForAttachment(String text) {

    this.bullet()
        .text(text)
        .newLine();

    return this;
  }
  
  public synchronized XRayCommentBuilder bullet() {
      comment.append("\n" + BULLET_POINT);

      return this;
  }


}