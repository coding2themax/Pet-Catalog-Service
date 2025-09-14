package com.coding2.the.max.petstore.catalog.config;

import jakarta.validation.constraints.NotBlank;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.coding2.the.max.petstore.catalog.domain.entity.PetEntity;

@Component
public class StringToSpeciesConverter implements Converter<String, PetEntity.Species> {

  @Override
  public PetEntity.Species convert(@NonNull @NotBlank String source) {
    if (source == null || source.isEmpty()) {
      return null;
    }

    switch (source.toLowerCase().replace("-", "_")) {
      case "dog":
        return PetEntity.Species.DOG;
      case "cat":
        return PetEntity.Species.CAT;
      case "bird":
        return PetEntity.Species.BIRD;
      case "fish":
        return PetEntity.Species.FISH;
      case "reptile":
        return PetEntity.Species.REPTILE;
      case "small_mammal":
      case "small-mammal":
        return PetEntity.Species.SMALL_MAMMAL;
      default:
        throw new IllegalArgumentException("Invalid species: " + source);
    }
  }
}
