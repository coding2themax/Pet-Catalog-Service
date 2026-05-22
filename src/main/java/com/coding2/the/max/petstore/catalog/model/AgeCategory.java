package com.coding2.the.max.petstore.catalog.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum AgeCategory {
  @JsonProperty("puppy")
  PUPPY,

  @JsonProperty("young")
  YOUNG,

  @JsonProperty("adult")
  ADULT,

  @JsonProperty("senior")
  SENIOR
}
