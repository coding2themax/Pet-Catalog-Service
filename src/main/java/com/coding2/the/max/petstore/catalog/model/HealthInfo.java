package com.coding2.the.max.petstore.catalog.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthInfo {

  private Boolean vaccinated;
  @JsonProperty("spayed_neutered")
  private Boolean spayedNeutered;
  @JsonProperty("health_certificate")
  private Boolean healthCertificate;
  @JsonProperty("special_needs")
  private List<String> specialNeeds;
  @JsonProperty("last_vet_visit")
  private LocalDate lastVetVisit;
}
