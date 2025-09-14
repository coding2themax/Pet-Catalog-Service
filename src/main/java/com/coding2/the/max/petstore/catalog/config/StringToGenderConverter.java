package com.coding2.the.max.petstore.catalog.config;

import jakarta.validation.constraints.NotBlank;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.coding2.the.max.petstore.catalog.domain.entity.PetEntity;

@Component
public class StringToGenderConverter implements Converter<String, PetEntity.Gender> {

  @Override
  public PetEntity.Gender convert(@NonNull @NotBlank String source) {
    if (source == null || source.isEmpty()) {
      return null;
    }

    switch (source.toLowerCase()) {
      case "male":
        return PetEntity.Gender.MALE;
      case "female":
        return PetEntity.Gender.FEMALE;
      default:
        throw new IllegalArgumentException("Invalid gender: " + source);
    }
  }
}
