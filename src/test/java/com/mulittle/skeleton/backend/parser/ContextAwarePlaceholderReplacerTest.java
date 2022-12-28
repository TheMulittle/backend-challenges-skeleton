package com.mulittle.skeleton.backend.parser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mulittle.skeleton.backend.context.PlaceholderContext;

@ExtendWith(MockitoExtension.class)
public class ContextAwarePlaceholderReplacerTest {

  @Mock
  PlaceholderContext context;

  @Test
  public void return_json_with_replaced_string_when_there_is_a_placeholder()
      throws IOException, URISyntaxException {

    //Arrange
    Path jsonPath = Paths.get(getClass().getClassLoader().getResource("com/mulittle/skeleton/backend/unit/parser/json_with_placeholder.json").toURI());
    String jsonWithPlaceholder = Files.readString(jsonPath);

    when(context.getAttributeAs("objectKey.attributeKey", String.class)).thenReturn("Name");

    //Act
    String jsonWihtoutPlaceholder = ContextAwarePlaceholderReplacer.replace(jsonWithPlaceholder, context);

    //Assert
    JsonNode node = new ObjectMapper().readTree(jsonWihtoutPlaceholder);
    assertThat(node.get("property").asText(), is("Name"));
  }

}
