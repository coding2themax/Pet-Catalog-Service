package com.coding2.the.max.petstore.catalog.dto;

import java.util.List;

import com.coding2.the.max.petstore.catalog.model.HealthInfo;
import com.coding2.the.max.petstore.catalog.model.PetImage;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePetRequest {

  private String name;

  @Positive
  private Integer age;

  @Positive
  private Double price;

  private String description;
  private List<String> characteristics;
  private HealthInfo healthInfo;
  private List<PetImage> images;
}
