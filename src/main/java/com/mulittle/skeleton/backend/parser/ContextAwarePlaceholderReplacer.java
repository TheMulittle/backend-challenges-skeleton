package com.mulittle.skeleton.backend.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mulittle.skeleton.backend.context.PlaceholderContext;
public class ContextAwarePlaceholderReplacer {

  private static final String PLACEHOLDER_PATTERN = "\\!\\[(.*\\..*)\\]";

  public static String replace(String jsonWithPlaceholder, PlaceholderContext context) {
    Matcher matcher = Pattern.compile(PLACEHOLDER_PATTERN).matcher(jsonWithPlaceholder);

    StringBuffer jsonWithPlaceholderReplaced = new StringBuffer();

    while(matcher.find()) {
      String value = context.getAttributeAs(matcher.group(1), String.class);
      matcher.appendReplacement(jsonWithPlaceholderReplaced, value);
    }
    matcher.appendTail(jsonWithPlaceholderReplaced);

    return jsonWithPlaceholderReplaced.toString();
  }

}
