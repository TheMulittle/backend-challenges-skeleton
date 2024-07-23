package com.mulittle.skeleton.backend.unit.parser;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.mulittle.skeleton.backend.parser.JsonAssertions;

public class JsonAsserterPlaceholderAware {

  /*@Test
  @DisplayName("")
  public void mapWithoutEllipsis() throws JsonMappingException, JsonProcessingException {
    // Arranje
    
    // Act
    JsonAssertions.assertJsonsAreMatch(actual, expected);

    // Assert
    //check it fails when jsons are different
    //check it passess when jsons are the same
    //check it passess when placeholder matcher match
    //check it fails when placholder matcher does not match
  }*/

  @Test
  @DisplayName("")
  public void mapWithoutEllipsis() throws JsonMappingException, JsonProcessingException {
    // Arranje
    String expected = 
    """
      {
        "id": P&|Integer_0_2147483647|
      }  
    """;
    
    String actual = 
    """
      {
        "id": 1
      }  
    """;
    
    // Act
    Assertions.assertThatThrownBy(() -> {throw new OutOfMemoryError();})
    .isInstanceOf(OutOfMemoryError.class);

    JsonAssertions.assertJsonsMatch(actual, expected);

    // Assert
    //check it fails when placholder matcher does not match for boolean
    //check it fails when placholder matcher does not match for int
    //check it fails when placholder matcher does not match for float
    //check it fails when placholder matcher does not match inside object
    //check it fails when placholder matcher does not match inside array of int
    //check it fails when placholder matcher does not match inside array of float
    //check it fails when placholder matcher does not match inside array of object
  }
}
