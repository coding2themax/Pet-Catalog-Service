package com.coding2.the.max.petstore.catalog.dto;

import com.coding2.the.max.petstore.catalog.model.Pet;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilityUpdateRequest {

  @NotNull
  private Pet.Availability availability;

  @JsonProperty("reserved_until")
  private Instant reservedUntil;

  private String notes;
}
