package com.coding2.the.max.petstore.catalog.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetImage {

  private String url;
  @JsonProperty("alt_text")
  private String altText;
  @JsonProperty("is_primary")
  private Boolean isPrimary;
}
