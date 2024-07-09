package com.mulittle.skeleton.backend.parser;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.mulittle.skeleton.backend.context.Context;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class ContextAwarePlaceholderReplacer {

  private final static String PLACEHOLDER_PATTERN = "ℙ(.)\\|(.*?)\\|";

  private final static String INCOMPLETE_GENERATIVE_ARGUMENTS_MESSAGE = 
  """
    Generative placeholder has less arguments than expected. It should have at least two but have [%s]. 
    Generative placeholder arguments should follow the format [Operation]_[argument1]_[argument2] but it was [%s]
  """;

  private final static String INVALID_OPERATION_MESSAGE = "Invalid placeholder operation [%s]";

  private final static String INVALID_OPERATOR_MESSAGE = "Invalid operator for generative placeholder [%s]";

  private final Context context;

  @Autowired
  public ContextAwarePlaceholderReplacer(Context context) {
    this.context = context;
  }

  public String replace(String jsonWithPlaceholder) {
    Matcher matcher = Pattern.compile(PLACEHOLDER_PATTERN).matcher(jsonWithPlaceholder);

    StringBuffer jsonWithPlaceholderReplaced = new StringBuffer();

    while (matcher.find()) {
      String value = "";
      value = extract(matcher.group(1), matcher.group(2));
      matcher.appendReplacement(jsonWithPlaceholderReplaced, value);
    }
    matcher.appendTail(jsonWithPlaceholderReplaced);

    return jsonWithPlaceholderReplaced.toString();
  }

  private String extract(String operation, String arguments) {
    switch (operation) {
      case "$":
        return extractPlaceholderReplacer(arguments.trim());

      case "%":
        return extractGenerativePlaceholder(arguments.trim());

      default:
        throw new IllegalArgumentException(INVALID_OPERATION_MESSAGE.formatted(operation));
        
    }
  }

  private String extractGenerativePlaceholder(String arguments) {
    String[] argumentArray = arguments.split("_");
    if(argumentArray.length < 2) {
      throw new IllegalArgumentException(INCOMPLETE_GENERATIVE_ARGUMENTS_MESSAGE.formatted(argumentArray.length, arguments));
    }

    switch (argumentArray[0].trim()) {
      case "UUID":
        String uuid = UUID.randomUUID().toString();
        context.put(argumentArray[1].trim(), uuid);
        return uuid;

      default:
        throw new IllegalArgumentException(INVALID_OPERATOR_MESSAGE.formatted(argumentArray[0]));
    }
  }

  private String extractPlaceholderReplacer(String key) {
    try {
      return context.findAs(key, String.class);
    } catch (ClassCastException e) {
      throw new NotImplementedException();
    }
  }

}
