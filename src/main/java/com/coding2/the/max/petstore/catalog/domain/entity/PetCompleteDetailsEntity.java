package com.coding2.the.max.petstore.catalog.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Entity representing the pets_complete_details view.
 * This view includes all necessary data for the Pet API responses,
 * with pre-joined breed, health, and location information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("pets_complete_details")
public class PetCompleteDetailsEntity {

  // Core pet data
  @Id
  private String id;

  private String name;

  private Integer age;

  private PetEntity.Size size;

  private PetEntity.Gender gender;

  private BigDecimal price;

  private String description;

  private PetEntity.Availability availability;

  @JsonProperty("location_id")
  @Column("location_id")
  private Long locationId;

  @JsonProperty("health_info_id")
  @Column("health_info_id")
  private Long healthInfoId;

  @JsonProperty("breed_id")
  @Column("breed_id")
  private Long breedId;

  @JsonProperty("created_at")
  @Column("created_at")
  private Instant createdAt;

  @JsonProperty("updated_at")
  @Column("updated_at")
  private Instant updatedAt;

  // Breed information (from breeds table)
  @JsonProperty("breed_name")
  @Column("breed_name")
  private String breedName;

  private PetEntity.Species species;

  // Age category (from pet_age_categories view)
  @JsonProperty("age_category")
  @Column("age_category")
  private String ageCategory;

  // Health information (from health_info table with defaults)
  private Boolean vaccinated;

  @JsonProperty("spayed_neutered")
  @Column("spayed_neutered")
  private Boolean spayedNeutered;

  @JsonProperty("health_certificate")
  @Column("health_certificate")
  private Boolean healthCertificate;

  // Location information (from locations table)
  @JsonProperty("store_name")
  @Column("store_name")
  private String storeName;

  private String city;

  private String state;

  // Computed fields for API convenience
  @JsonProperty("is_available")
  @Column("is_available")
  private Boolean isAvailable;

  // Primary image URL (from pet_images subquery)
  @JsonProperty("primary_image_url")
  @Column("primary_image_url")
  private String primaryImageUrl;

  // Helper methods for API compatibility
  public boolean getIsAvailableBoolean() {
    return Boolean.TRUE.equals(isAvailable);
  }

  public String getSpeciesAsString() {
    return species != null ? species.name().toLowerCase().replace('_', '-') : null;
  }

  public String getSizeAsString() {
    return size != null ? size.name().toLowerCase().replace('_', '-') : null;
  }

  public String getGenderAsString() {
    return gender != null ? gender.name().toLowerCase() : null;
  }
}