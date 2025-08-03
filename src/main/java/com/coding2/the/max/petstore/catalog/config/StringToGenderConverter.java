package com.coding2.the.max.petstore.catalog.config;

import com.coding2.the.max.petstore.catalog.model.Pet;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToGenderConverter implements Converter<String, Pet.Gender> {

  @Override
  public Pet.Gender convert(String source) {
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
