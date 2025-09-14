package com.coding2.the.max.petstore.catalog.domain.service;

import com.coding2.the.max.petstore.catalog.domain.entity.PetEntity;
import com.coding2.the.max.petstore.catalog.domain.repository.BreedRepository;
import com.coding2.the.max.petstore.catalog.dto.BreedsResponse;
import com.coding2.the.max.petstore.catalog.dto.SpeciesResponse;
import com.coding2.the.max.petstore.catalog.model.Breed;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CatalogPostGresService implements CatalogService {

  private final BreedRepository breedRepository;

  @Override
  public Mono<BreedsResponse> getBreeds(PetEntity.Species species) {
    Flux<Breed> breedsFlux = species != null
        ? breedRepository.findBySpecies(species)
        : breedRepository.findAll();

    return breedsFlux
        .map(breed -> BreedsResponse.Breed.builder()
            .name(breed.getName())
            .species(breed.getSpecies().toString().toLowerCase().replace("_", "-"))
            .typicalSize(breed.getTypicalSize())
            .characteristics(breed.getCharacteristics())
            .build())
        .collectList()
        .map(breedList -> BreedsResponse.builder().breeds(breedList).build());
  }

  @Override
  public Mono<SpeciesResponse> getSpecies() {
    return breedRepository.findAll()
        .groupBy(Breed::getSpecies)
        .flatMap(group -> group.count()
            .map(count -> SpeciesResponse.Species.builder()
                .name(group.key().toString().toLowerCase().replace("_", "-"))
                .displayName(getDisplayName(group.key()))
                .breedCount(count.intValue())
                .build()))
        .collectList()
        .map(speciesList -> SpeciesResponse.builder().species(speciesList).build());
  }

  private String getDisplayName(PetEntity.Species species) {
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
}
