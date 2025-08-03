package com.coding2.the.max.petstore.catalog.dto;

import com.coding2.the.max.petstore.catalog.model.HealthInfo;
import com.coding2.the.max.petstore.catalog.model.Pet;
import com.coding2.the.max.petstore.catalog.model.PetImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Positive;
import java.util.List;

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
