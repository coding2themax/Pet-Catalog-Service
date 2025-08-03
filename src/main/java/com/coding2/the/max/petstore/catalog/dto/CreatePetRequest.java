package com.coding2.the.max.petstore.catalog.dto;

import com.coding2.the.max.petstore.catalog.model.HealthInfo;
import com.coding2.the.max.petstore.catalog.model.Location;
import com.coding2.the.max.petstore.catalog.model.Pet;
import com.coding2.the.max.petstore.catalog.model.PetImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePetRequest {

  @NotBlank
  private String name;

  @NotNull
  private Pet.Species species;

  @NotBlank
  private String breed;

  @NotNull
  @Positive
  private Integer age;

  private Pet.Size size;
  private Pet.Gender gender;

  @NotNull
  @Positive
  private Double price;

  @NotBlank
  private String description;

  private List<String> characteristics;
  private HealthInfo healthInfo;
  private Location location;
  private List<PetImage> images;
}
