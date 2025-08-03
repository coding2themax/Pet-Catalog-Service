package com.coding2.the.max.petstore.catalog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpeciesResponse {

  private List<Species> species;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Species {
    private String name;
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty("breed_count")
    private Integer breedCount;
  }
}
