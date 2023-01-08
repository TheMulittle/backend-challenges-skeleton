package com.mulittle.skeleton.backend.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company implements Serializable {
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private Integer id;
  private String name;   
}
