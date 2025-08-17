package com.coding2.the.max.petstore.catalog.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("health_info")
public class HealthInfo {

  @Id
  private Long id;

  private Boolean vaccinated;
  @JsonProperty("spayed_neutered")
  @Column("spayed_neutered")
  private Boolean spayedNeutered;
  @JsonProperty("health_certificate")
  @Column("health_certificate")
  private Boolean healthCertificate;
  @JsonProperty("last_vet_visit")
  @Column("last_vet_visit")
  private LocalDate lastVetVisit;

  // Mapped via health_info_special_needs table
  @JsonProperty("special_needs")
  @Transient
  private List<String> specialNeeds;

  @JsonProperty("created_at")
  @CreatedDate
  @Column("created_at")
  private Instant createdAt;
  @JsonProperty("updated_at")
  @LastModifiedDate
  @Column("updated_at")
  private Instant updatedAt;
}
