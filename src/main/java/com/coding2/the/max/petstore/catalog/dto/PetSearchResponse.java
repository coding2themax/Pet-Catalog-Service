package com.coding2.the.max.petstore.catalog.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetSearchResponse {

  private List<PetResponseDTO> pets;
  private Pagination pagination;
  @JsonProperty("filters_applied")
  private Map<String, String> filtersApplied;
}
