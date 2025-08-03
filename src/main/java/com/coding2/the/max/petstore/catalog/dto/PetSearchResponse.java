package com.coding2.the.max.petstore.catalog.dto;

import com.coding2.the.max.petstore.catalog.model.Pet;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetSearchResponse {

  private List<Pet> pets;
  private Pagination pagination;
  @JsonProperty("filters_applied")
  private Map<String, String> filtersApplied;
}
