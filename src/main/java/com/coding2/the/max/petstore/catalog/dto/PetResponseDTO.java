package com.coding2.the.max.petstore.catalog.dto;

import java.math.BigDecimal;
import java.util.List;

import com.coding2.the.max.petstore.catalog.model.Pet;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetResponseDTO {

  private String id;
  private String name;
  private Pet.Species species;
  private String breed;
  private Integer age;
  private Pet.Gender gender;
  private BigDecimal price;
  private String description;

  @JsonProperty("imageUrl")
  private String imageUrl;

  @JsonProperty("isAvailable")
  private Boolean isAvailable;

  private List<String> characteristics;

  @JsonProperty("healthStatus")
  private String healthStatus;

  private Boolean vaccinated;

  @JsonProperty("spayedNeutered")
  private Boolean spayedNeutered;

  private Pet.Size size;

  @JsonProperty("energyLevel")
  private String energyLevel;

  @JsonProperty("goodWithKids")
  private Boolean goodWithKids;

  @JsonProperty("goodWithPets")
  private Boolean goodWithPets;
}
