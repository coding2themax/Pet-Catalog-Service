package com.coding2.the.max.petstore.catalog.config;

import com.coding2.the.max.petstore.catalog.model.Pet;

import jakarta.validation.constraints.NotBlank;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class StringToGenderConverter implements Converter<String, Pet.Gender> {

  @Override
  public Pet.Gender convert(@NonNull @NotBlank String source) {
    if (source == null || source.isEmpty()) {
      return null;
    }

    switch (source.toLowerCase()) {
      case "male":
        return Pet.Gender.MALE;
      case "female":
        return Pet.Gender.FEMALE;
      default:
        throw new IllegalArgumentException("Invalid gender: " + source);
    }
  }
}
