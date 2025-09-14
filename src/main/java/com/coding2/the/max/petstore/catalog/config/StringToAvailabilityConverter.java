package com.coding2.the.max.petstore.catalog.config;

import jakarta.validation.constraints.NotBlank;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.coding2.the.max.petstore.catalog.domain.entity.PetEntity;

@Component
public class StringToAvailabilityConverter implements Converter<String, PetEntity.Availability> {

  @Override
  public PetEntity.Availability convert(@NonNull @NotBlank String source) {
    if (source == null || source.isEmpty()) {
      return null;
    }

    switch (source.toLowerCase().replace("-", "_")) {
      case "available":
        return PetEntity.Availability.AVAILABLE;
      case "reserved":
        return PetEntity.Availability.RESERVED;
      case "sold":
        return PetEntity.Availability.SOLD;
      case "coming_soon":
      case "coming-soon":
        return PetEntity.Availability.COMING_SOON;
      default:
        throw new IllegalArgumentException("Invalid availability: " + source);
    }
  }
}
