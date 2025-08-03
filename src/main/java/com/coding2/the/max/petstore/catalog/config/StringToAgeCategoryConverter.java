package com.coding2.the.max.petstore.catalog.config;

import com.coding2.the.max.petstore.catalog.model.Pet;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToAgeCategoryConverter implements Converter<String, Pet.AgeCategory> {

  @Override
  public Pet.AgeCategory convert(String source) {
    if (source == null || source.isEmpty()) {
      return null;
    }

    switch (source.toLowerCase()) {
      case "puppy":
        return Pet.AgeCategory.PUPPY;
      case "young":
        return Pet.AgeCategory.YOUNG;
      case "adult":
        return Pet.AgeCategory.ADULT;
      case "senior":
        return Pet.AgeCategory.SENIOR;
      default:
        throw new IllegalArgumentException("Invalid age category: " + source);
    }
  }
}
