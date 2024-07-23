package com.mulittle.skeleton.backend.unit.parser;

import static org.mockito.Mockito.verify;
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
  @DisplayName("ContextAwarePlaceholderReplacer#replace throws exception when invalid operation is passed to placeholder P[operation]{[operator], [argument1], [argument2], ...}")
  public void throws_exception_when_invalid_operation_is_used() {
    // Arranje
    String originalPayload = """
          {
            "name": "PB|placeholder|"
          }
        """;

    ContextAwarePlaceholderReplacer contextAwarePlaceholderReplacer = new ContextAwarePlaceholderReplacer(context);

    // Act
    // Assert
    Assertions.assertThatThrownBy(() -> contextAwarePlaceholderReplacer.replace(originalPayload))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid placeholder operation [B]");
  }

  @Test
  @DisplayName("ContextAwarePlaceholderReplacer#replace replace context placeholder P${[placeholderName]} for a String")
  public void return_json_with_replaced_string_when_there_is_a_context_placeholder() {
    // Arranje
    String originalPayload =       
    """ 
      {
        "name": "P$|placeholder|"
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
  @DisplayName("ContextAwarePlaceholderReplacer#replace replace context placeholder P${[placeholderName]} for a Number")
  public void return_json_with_replaced_integer_when_there_is_a_context_placeholder() {
    // Arranje
    String originalPayload =       
    """ 
      {
        "name": P$|placeholder|
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
    Assertions.assertThat(actual).isEqualTo(expected);
  }

  @Test
  @DisplayName("ContextAwarePlaceholderReplacer#replace replace UUID generative placeholder P%{UUID, [placeholderName]}")
  public void return_json_with_replaced_integer_when_there_is_a_generation_placeholder() {
    // Arranje
    String originalPayload =       
    """ 
      {
        "name": "P%|UUID_placeholder|"
      }
    """;

    String expected =       
    """ 
      {
        "name": "0e8a6599-e030-469c-a4af-32a8b8767727"
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
    verify(context).put("placeholder", "0e8a6599-e030-469c-a4af-32a8b8767727");
  }

  @Test
  @DisplayName("ContextAwarePlaceholderReplacer#replace throws exception when there are less arguments than expected in UUID generative placeholder P%{UUID, [placeholderName]}")
  public void throws_exception_when_uuid_generative_placeholder_has_less_arguments() {
    // Arranje
    String originalPayload = """
          {
            "name": "P%|UUID|"
          }
        """;

    ContextAwarePlaceholderReplacer contextAwarePlaceholderReplacer = new ContextAwarePlaceholderReplacer(context);

    // Act
    // Assert
    Assertions.assertThatThrownBy(() -> contextAwarePlaceholderReplacer.replace(originalPayload))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(
            """
                  Generative placeholder has less arguments than expected. It should have at least two but have [1].
                  Generative placeholder arguments should follow the format [Operation]_[argument1]_[argument2] but it was [UUID]
                """);
  }

  @Test
  @DisplayName("ContextAwarePlaceholderReplacer#replace throws exception when invalid operator is passed to generative placeholder P%{[operation], [argument1], [argument2], ...}")
  public void throws_exception_when_invalid_operator_is_used() {
    // Arranje
    String originalPayload = """
          {
            "name": "P%|INVALID_name|"
          }
        """;

    ContextAwarePlaceholderReplacer contextAwarePlaceholderReplacer = new ContextAwarePlaceholderReplacer(context);

    // Act
    // Assert
    Assertions.assertThatThrownBy(() -> contextAwarePlaceholderReplacer.replace(originalPayload))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid operator for generative placeholder [INVALID]");
  }
}
