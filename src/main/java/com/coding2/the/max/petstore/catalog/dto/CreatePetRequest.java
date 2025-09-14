package com.coding2.the.max.petstore.catalog.dto;

import com.coding2.the.max.petstore.catalog.domain.entity.PetEntity;
import com.coding2.the.max.petstore.catalog.model.HealthInfo;
import com.coding2.the.max.petstore.catalog.model.Location;
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
  private PetEntity.Species species;

  @NotBlank
  private String breed;

  @NotNull
  @Positive
  private Integer age;

  private PetEntity.Size size;
  private PetEntity.Gender gender;

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
