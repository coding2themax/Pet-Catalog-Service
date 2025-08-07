package com.coding2.the.max.petstore.catalog.config;

import com.coding2.the.max.petstore.catalog.model.Pet;

import jakarta.validation.constraints.NotBlank;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class StringToSizeConverter implements Converter<String, Pet.Size> {

  @Override
  public Pet.Size convert(@NonNull @NotBlank String source) {
    if (source == null || source.isEmpty()) {
      return null;
    }

    switch (source.toLowerCase().replace("-", "_")) {
      case "small":
        return Pet.Size.SMALL;
      case "medium":
        return Pet.Size.MEDIUM;
      case "large":
        return Pet.Size.LARGE;
      case "extra_large":
      case "extra-large":
        return Pet.Size.EXTRA_LARGE;
      default:
        throw new IllegalArgumentException("Invalid size: " + source);
    }
  }
}
