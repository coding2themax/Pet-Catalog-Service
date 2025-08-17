package com.coding2.the.max.petstore.catalog.service;

import com.coding2.the.max.petstore.catalog.dto.*;
import com.coding2.the.max.petstore.catalog.model.Pet;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PetServiceLocalData implements PetService {

  // In-memory storage for demo purposes
  private final Map<String, Pet> petStorage = new ConcurrentHashMap<>();

  @Override
  public Mono<PetSearchResponse> searchPets(Pet.Species species, String breed, Pet.Size size,
      String ageCategory, Double priceMin, Double priceMax,
      Pet.Availability availability, Pet.Gender gender,
      Boolean vaccinated, String sortBy, String sortOrder,
      Integer page, Integer limit) {

    return Flux.fromIterable(petStorage.values())
        .filter(pet -> species == null || pet.getSpecies().equals(species))
        .filter(pet -> breed == null || pet.getBreed().toLowerCase().contains(breed.toLowerCase()))
        .filter(pet -> size == null || pet.getSize().equals(size))
        .filter(pet -> ageCategory == null
            || (pet.getAgeCategory() != null && pet.getAgeCategory().equalsIgnoreCase(ageCategory)))
        .filter(pet -> priceMin == null || pet.getPrice() >= priceMin)
        .filter(pet -> priceMax == null || pet.getPrice() <= priceMax)
        .filter(pet -> availability == null || pet.getAvailability().equals(availability))
        .filter(pet -> gender == null || pet.getGender().equals(gender))
        .filter(pet -> vaccinated == null ||
            (pet.getHealthInfo() != null && vaccinated.equals(pet.getHealthInfo().getVaccinated())))
        .sort(getComparator(sortBy, sortOrder))
        .collectList()
        .map(pets -> {
          int start = (page - 1) * limit;
          int end = Math.min(start + limit, pets.size());
          List<Pet> paginatedPets = start < pets.size() ? pets.subList(start, end) : Collections.emptyList();

          Pagination pagination = Pagination.builder()
              .page(page)
              .limit(limit)
              .totalItems((long) pets.size())
              .totalPages((int) Math.ceil((double) pets.size() / limit))
              .hasNext(end < pets.size())
              .hasPrev(page > 1)
              .build();

          Map<String, String> filtersApplied = new HashMap<>();
          if (species != null)
            filtersApplied.put("species", species.toString());
          if (breed != null)
            filtersApplied.put("breed", breed);
          if (size != null)
            filtersApplied.put("size", size.toString());

          return PetSearchResponse.builder()
              .pets(paginatedPets)
              .pagination(pagination)
              .filtersApplied(filtersApplied)
              .build();
        });
  }

  @Override
  public Mono<Pet> createPet(CreatePetRequest request) {
    Pet pet = Pet.builder()
        .id(UUID.randomUUID().toString())
        .name(request.getName())
        .species(request.getSpecies())
        .breed(request.getBreed())
        .age(request.getAge())
        .size(request.getSize())
        .gender(request.getGender())
        .price(request.getPrice())
        .description(request.getDescription())
        .characteristics(request.getCharacteristics())
        .healthInfo(request.getHealthInfo())
        .availability(Pet.Availability.AVAILABLE)
        .images(request.getImages())
        .location(request.getLocation())
        .createdAt(Instant.now())
        .updatedAt(Instant.now())
        .build();

    // Determine age category based on age
    pet.setAgeCategory(determineAgeCategory(request.getAge(), request.getSpecies()));

    petStorage.put(pet.getId(), pet);
    return Mono.just(pet);
  }

  @Override
  public Mono<Pet> getPetById(String petId) {
    Pet pet = petStorage.get(petId);
    return pet != null ? Mono.just(pet) : Mono.empty();
  }

  @Override
  public Mono<Pet> updatePet(String petId, UpdatePetRequest request) {
    return getPetById(petId)
        .map(existingPet -> {
          if (request.getName() != null)
            existingPet.setName(request.getName());
          if (request.getAge() != null) {
            existingPet.setAge(request.getAge());
            existingPet.setAgeCategory(determineAgeCategory(request.getAge(), existingPet.getSpecies()));
          }
          if (request.getPrice() != null)
            existingPet.setPrice(request.getPrice());
          if (request.getDescription() != null)
            existingPet.setDescription(request.getDescription());
          if (request.getCharacteristics() != null)
            existingPet.setCharacteristics(request.getCharacteristics());
          if (request.getHealthInfo() != null)
            existingPet.setHealthInfo(request.getHealthInfo());
          if (request.getImages() != null)
            existingPet.setImages(request.getImages());
          existingPet.setUpdatedAt(Instant.now());

          petStorage.put(petId, existingPet);
          return existingPet;
        });
  }

  @Override
  public Mono<Void> deletePet(String petId) {
    return getPetById(petId)
        .doOnNext(_ -> petStorage.remove(petId))
        .then();
  }

  @Override
  public Mono<Pet> updatePetAvailability(String petId, AvailabilityUpdateRequest request) {
    return getPetById(petId)
        .map(existingPet -> {
          existingPet.setAvailability(request.getAvailability());
          existingPet.setUpdatedAt(Instant.now());
          petStorage.put(petId, existingPet);
          return existingPet;
        });
  }

  private String determineAgeCategory(Integer ageInMonths, Pet.Species species) {
    if (ageInMonths == null || species == null)
      return null;
    if (species == Pet.Species.DOG) {
      if (ageInMonths <= 12)
        return "puppy";
      if (ageInMonths <= 24)
        return "young";
      if (ageInMonths <= 84)
        return "adult";
      return "senior";
    } else if (species == Pet.Species.CAT) {
      if (ageInMonths <= 6)
        return "puppy"; // kitten mapped to puppy for legacy
      if (ageInMonths <= 24)
        return "young";
      if (ageInMonths <= 96)
        return "adult";
      return "senior";
    } else {
      if (ageInMonths <= 6)
        return "puppy";
      if (ageInMonths <= 18)
        return "young";
      if (ageInMonths <= 60)
        return "adult";
      return "senior";
    }
  }

  private Comparator<Pet> getComparator(String sortBy, String sortOrder) {
    Comparator<Pet> comparator;

    switch (sortBy != null ? sortBy : "date_added") {
      case "price":
        comparator = Comparator.comparing(Pet::getPrice);
        break;
      case "age":
        comparator = Comparator.comparing(Pet::getAge);
        break;
      case "popularity":
        // For demo purposes, sort by creation date as popularity
        comparator = Comparator.comparing(Pet::getCreatedAt);
        break;
      case "date_added":
      default:
        comparator = Comparator.comparing(Pet::getCreatedAt);
        break;
    }

    return "asc".equalsIgnoreCase(sortOrder) ? comparator : comparator.reversed();
  }
}
