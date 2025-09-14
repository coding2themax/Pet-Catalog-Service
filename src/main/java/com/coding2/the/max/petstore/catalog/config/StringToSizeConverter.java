package com.coding2.the.max.petstore.catalog.config;

import jakarta.validation.constraints.NotBlank;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.coding2.the.max.petstore.catalog.domain.entity.PetEntity;

@Component
public class StringToSizeConverter implements Converter<String, PetEntity.Size> {

  @Override
  public PetEntity.Size convert(@NonNull @NotBlank String source) {
    if (source == null || source.isEmpty()) {
      return null;
    }

    switch (source.toLowerCase().replace("-", "_")) {
      case "small":
        return PetEntity.Size.SMALL;
      case "medium":
        return PetEntity.Size.MEDIUM;
      case "large":
        return PetEntity.Size.LARGE;
      case "extra_large":
      case "extra-large":
        return PetEntity.Size.EXTRA_LARGE;
      default:
        throw new IllegalArgumentException("Invalid size: " + source);
    }
  }
}
