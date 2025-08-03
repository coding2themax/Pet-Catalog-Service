package com.coding2.the.max.petstore.catalog.config;

import com.coding2.the.max.petstore.catalog.model.Pet;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToAvailabilityConverter implements Converter<String, Pet.Availability> {

  @Override
  public Pet.Availability convert(String source) {
    if (source == null || source.isEmpty()) {
      return null;
    }

    switch (source.toLowerCase().replace("-", "_")) {
      case "available":
        return Pet.Availability.AVAILABLE;
      case "reserved":
        return Pet.Availability.RESERVED;
      case "sold":
        return Pet.Availability.SOLD;
      case "coming_soon":
      case "coming-soon":
        return Pet.Availability.COMING_SOON;
      default:
        throw new IllegalArgumentException("Invalid availability: " + source);
    }
  }
}
