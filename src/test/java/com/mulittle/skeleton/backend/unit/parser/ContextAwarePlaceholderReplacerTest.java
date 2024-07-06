package com.mulittle.skeleton.backend.unit.parser;

import static org.mockito.Mockito.when;

import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mulittle.skeleton.backend.context.Context;
import com.mulittle.skeleton.backend.parser.ContextAwarePlaceholderReplacer;

@ExtendWith(MockitoExtension.class)
public class ContextAwarePlaceholderReplacerTest {

  @Mock
  Context context;

  @Mock
  UUID uuidMock;


  @Test
  @DisplayName("ContextAwarePlaceholderReplacer#replace replace context placeholder ℙ${} for a String")
  public void return_json_with_replaced_string_when_there_is_a_context_placeholder() {
    // Arranje
    String originalPayload =       
    """ 
      {
        "name": "ℙ${placeholder}"
      }
    """;

    String expected =       
    """ 
      {
        "name": "abcd"
      }
    """;

    ContextAwarePlaceholderReplacer contextAwarePlaceholderReplacer = new ContextAwarePlaceholderReplacer(context);
    when(context.findAs("placeholder", String.class)).thenReturn("abcd");

    // Act
    String actual = contextAwarePlaceholderReplacer.replace(originalPayload);

    // Assert
    Assertions.assertThat(expected).isEqualTo(actual);
  }

  @Test
  @DisplayName("ContextAwarePlaceholderReplacer#replace replace context placeholder ℙ${} for a Number")
  public void return_json_with_replaced_integer_when_there_is_a_context_placeholder() {
    // Arranje
    String originalPayload =       
    """ 
      {
        "name": ℙ${placeholder}
      }
    """;

    String expected =       
    """ 
      {
        "name": 123.0
      }
    """;

    ContextAwarePlaceholderReplacer contextAwarePlaceholderReplacer = new ContextAwarePlaceholderReplacer(context);
    when(context.findAs("placeholder", String.class)).thenReturn("123.0");

    // Act
    String actual = contextAwarePlaceholderReplacer.replace(originalPayload);

    // Assert
    Assertions.assertThat(expected).isEqualTo(actual);
  }

  @Test
  @DisplayName("ContextAwarePlaceholderReplacer#replace replace generated placeholder ℙ%{}")
  public void return_json_with_replaced_integer_when_there_is_a_generation_placeholder() {
    // Arranje
    String originalPayload =       
    """ 
      {
        "name": ℙ%{UUID, placeholder}
      }
    """;

    String expected =       
    """ 
      {
        "name": 0e8a6599-e030-469c-a4af-32a8b8767727
      }
    """;

    ContextAwarePlaceholderReplacer contextAwarePlaceholderReplacer = new ContextAwarePlaceholderReplacer(context);
    UUID uuid = UUID.fromString("0e8a6599-e030-469c-a4af-32a8b8767727");
    MockedStatic<UUID> uuidMock = Mockito.mockStatic(UUID.class);
    uuidMock.when(() -> UUID.randomUUID()).thenReturn(uuid);

    // Act
    String actual = contextAwarePlaceholderReplacer.replace(originalPayload);

    // Assert
    Assertions.assertThat(actual).isEqualTo(expected);
  }
}
