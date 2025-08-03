package com.coding2.the.max.petstore.catalog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

  private String error;
  private String message;
  private Map<String, String> details;
  private Instant timestamp;
  @JsonProperty("request_id")
  private String requestId;
}
