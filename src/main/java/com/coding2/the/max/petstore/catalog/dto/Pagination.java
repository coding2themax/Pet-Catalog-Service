package com.coding2.the.max.petstore.catalog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pagination {

  private Integer page;
  private Integer limit;
  @JsonProperty("total_items")
  private Long totalItems;
  @JsonProperty("total_pages")
  private Integer totalPages;
  @JsonProperty("has_next")
  private Boolean hasNext;
  @JsonProperty("has_prev")
  private Boolean hasPrev;
}
