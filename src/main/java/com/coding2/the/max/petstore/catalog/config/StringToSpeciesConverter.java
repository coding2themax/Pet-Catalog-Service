package com.coding2.the.max.petstore.catalog.config;

import com.coding2.the.max.petstore.catalog.model.Pet;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToSpeciesConverter implements Converter<String, Pet.Species> {

  @Override
  public Pet.Species convert(String source) {
    if (source == null || source.isEmpty()) {
      return null;
    }

    switch (source.toLowerCase().replace("-", "_")) {
      case "dog":
        return Pet.Species.DOG;
      case "cat":
        return Pet.Species.CAT;
      case "bird":
        return Pet.Species.BIRD;
      case "fish":
        return Pet.Species.FISH;
      case "reptile":
        return Pet.Species.REPTILE;
      case "small_mammal":
      case "small-mammal":
        return Pet.Species.SMALL_MAMMAL;
      default:
        throw new IllegalArgumentException("Invalid species: " + source);
    }
  }
}
