package com.coding2.the.max.petstore.catalog.service;

import com.coding2.the.max.petstore.catalog.dto.BreedsResponse;
import com.coding2.the.max.petstore.catalog.dto.SpeciesResponse;
import com.coding2.the.max.petstore.catalog.model.Pet;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CatalogService {

  // Static data for demo purposes
  private static final Map<Pet.Species, List<BreedInfo>> BREEDS_DATA = new HashMap<>();

  static {
    BREEDS_DATA.put(Pet.Species.DOG, Arrays.asList(
        new BreedInfo("Golden Retriever", Pet.Species.DOG, Pet.Size.LARGE,
            Arrays.asList("friendly", "intelligent", "active")),
        new BreedInfo("Labrador Retriever", Pet.Species.DOG, Pet.Size.LARGE,
            Arrays.asList("friendly", "outgoing", "active")),
        new BreedInfo("Bulldog", Pet.Species.DOG, Pet.Size.MEDIUM, Arrays.asList("docile", "willful", "friendly")),
        new BreedInfo("Poodle", Pet.Species.DOG, Pet.Size.MEDIUM, Arrays.asList("intelligent", "active", "elegant")),
        new BreedInfo("Chihuahua", Pet.Species.DOG, Pet.Size.SMALL, Arrays.asList("sassy", "graceful", "charming"))));

    BREEDS_DATA.put(Pet.Species.CAT, Arrays.asList(
        new BreedInfo("Persian", Pet.Species.CAT, Pet.Size.MEDIUM, Arrays.asList("quiet", "docile", "sweet")),
        new BreedInfo("Maine Coon", Pet.Species.CAT, Pet.Size.LARGE,
            Arrays.asList("gentle", "friendly", "intelligent")),
        new BreedInfo("Siamese", Pet.Species.CAT, Pet.Size.MEDIUM, Arrays.asList("vocal", "intelligent", "active")),
        new BreedInfo("British Shorthair", Pet.Species.CAT, Pet.Size.MEDIUM,
            Arrays.asList("calm", "friendly", "independent"))));

    BREEDS_DATA.put(Pet.Species.BIRD, Arrays.asList(
        new BreedInfo("Canary", Pet.Species.BIRD, Pet.Size.SMALL, Arrays.asList("melodious", "cheerful", "social")),
        new BreedInfo("Parakeet", Pet.Species.BIRD, Pet.Size.SMALL, Arrays.asList("playful", "social", "intelligent")),
        new BreedInfo("Macaw", Pet.Species.BIRD, Pet.Size.LARGE, Arrays.asList("intelligent", "social", "colorful"))));

    BREEDS_DATA.put(Pet.Species.FISH, Arrays.asList(
        new BreedInfo("Goldfish", Pet.Species.FISH, Pet.Size.SMALL, Arrays.asList("hardy", "peaceful", "colorful")),
        new BreedInfo("Betta", Pet.Species.FISH, Pet.Size.SMALL, Arrays.asList("colorful", "territorial", "hardy")),
        new BreedInfo("Angel Fish", Pet.Species.FISH, Pet.Size.MEDIUM,
            Arrays.asList("graceful", "peaceful", "elegant"))));

    BREEDS_DATA.put(Pet.Species.REPTILE, Arrays.asList(
        new BreedInfo("Leopard Gecko", Pet.Species.REPTILE, Pet.Size.SMALL,
            Arrays.asList("docile", "easy-care", "nocturnal")),
        new BreedInfo("Bearded Dragon", Pet.Species.REPTILE, Pet.Size.MEDIUM,
            Arrays.asList("calm", "social", "hardy"))));

    BREEDS_DATA.put(Pet.Species.SMALL_MAMMAL, Arrays.asList(
        new BreedInfo("Holland Lop", Pet.Species.SMALL_MAMMAL, Pet.Size.SMALL,
            Arrays.asList("gentle", "calm", "friendly")),
        new BreedInfo("Guinea Pig", Pet.Species.SMALL_MAMMAL, Pet.Size.SMALL,
            Arrays.asList("social", "gentle", "vocal")),
        new BreedInfo("Hamster", Pet.Species.SMALL_MAMMAL, Pet.Size.SMALL,
            Arrays.asList("active", "curious", "independent"))));
  }

  public Mono<BreedsResponse> getBreeds(Pet.Species species) {
    List<BreedInfo> breeds = species != null ? BREEDS_DATA.getOrDefault(species, Collections.emptyList())
        : BREEDS_DATA.values().stream().flatMap(List::stream).collect(Collectors.toList());

    List<BreedsResponse.Breed> breedList = breeds.stream()
        .map(breedInfo -> BreedsResponse.Breed.builder()
            .name(breedInfo.name)
            .species(breedInfo.species.toString().toLowerCase().replace("_", "-"))
            .typicalSize(breedInfo.typicalSize)
            .characteristics(breedInfo.characteristics)
            .build())
        .collect(Collectors.toList());

    return Mono.just(BreedsResponse.builder().breeds(breedList).build());
  }

  public Mono<SpeciesResponse> getSpecies() {
    List<SpeciesResponse.Species> speciesList = Arrays.stream(Pet.Species.values())
        .map(species -> {
          int breedCount = BREEDS_DATA.getOrDefault(species, Collections.emptyList()).size();
          return SpeciesResponse.Species.builder()
              .name(species.toString().toLowerCase().replace("_", "-"))
              .displayName(getDisplayName(species))
              .breedCount(breedCount)
              .build();
        })
        .collect(Collectors.toList());

    return Mono.just(SpeciesResponse.builder().species(speciesList).build());
  }

  private String getDisplayName(Pet.Species species) {
    switch (species) {
      case DOG:
        return "Dogs";
      case CAT:
        return "Cats";
      case BIRD:
        return "Birds";
      case FISH:
        return "Fish";
      case REPTILE:
        return "Reptiles";
      case SMALL_MAMMAL:
        return "Small Mammals";
      default:
        return species.toString();
    }
  }

  private static class BreedInfo {
    final String name;
    final Pet.Species species;
    final Pet.Size typicalSize;
    final List<String> characteristics;

    BreedInfo(String name, Pet.Species species, Pet.Size typicalSize, List<String> characteristics) {
      this.name = name;
      this.species = species;
      this.typicalSize = typicalSize;
      this.characteristics = characteristics;
    }
  }
}
