package com.mulittle.skeleton.backend.parser;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.RuntimeErrorException;

import org.apache.commons.lang3.NotImplementedException;

import com.mulittle.skeleton.backend.context.Context;

public class ContextAwarePlaceholderReplacer {

  private final Context context;

  private final String PLACEHOLDER_PATTERN = "ℙ(.)\\{(.*?)\\}";

  public ContextAwarePlaceholderReplacer(Context context) {
    this.context = context;
  }

  public String replace(String jsonWithPlaceholder) {
    Matcher matcher = Pattern.compile(PLACEHOLDER_PATTERN).matcher(jsonWithPlaceholder);

    StringBuffer jsonWithPlaceholderReplaced = new StringBuffer();

    while (matcher.find()) {
      String value = "";
      switch (matcher.group(1)) {
        case "$":
          value = extractPlaceholderReplacer(matcher.group(2));
          break;

        case "%":
          value = generativePlaceholder(matcher.group(2));
        default:
          break;
      }
      matcher.appendReplacement(jsonWithPlaceholderReplaced, value);
    }
    matcher.appendTail(jsonWithPlaceholderReplaced);

    return jsonWithPlaceholderReplaced.toString();
  }

  private String generativePlaceholder(String arguments) {
    String[] argumentArray = arguments.split(",");
    String value;
    switch (argumentArray[0]) {
      case "UUID":
        String uuid = UUID.randomUUID().toString();
        context.put(argumentArray[1], uuid);
        value = uuid;
        break;
        
      default:
        throw new RuntimeErrorException(null);
    }
    // TODO Auto-generated method stub
    return value;
  }

  private String extractPlaceholderReplacer(String key) {
    try {
      return context.findAs(key, String.class);
    } catch (ClassCastException e) {
      throw new NotImplementedException();
    }
  }

}
