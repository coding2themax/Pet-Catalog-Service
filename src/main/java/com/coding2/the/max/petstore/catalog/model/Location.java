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
public class Location {

  @JsonProperty("store_id")
  private String storeId;
  @JsonProperty("store_name")
  private String storeName;
  private String city;
  private String state;
  @JsonProperty("zip_code")
  private String zipCode;
}
