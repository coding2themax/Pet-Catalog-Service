package com.coding2.the.max.petstore.catalog.dto;

import com.coding2.the.max.petstore.catalog.model.Pet;
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
public class BreedsResponse {

  private List<Breed> breeds;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Breed {
    private String name;
    private String species;
    @JsonProperty("typical_size")
    private Pet.Size typicalSize;
    private List<String> characteristics;
  }
}
