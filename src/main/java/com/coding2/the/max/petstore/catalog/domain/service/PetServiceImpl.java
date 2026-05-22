package com.coding2.the.max.petstore.catalog.domain.service;

import org.springframework.stereotype.Service;

import com.coding2.the.max.petstore.catalog.domain.entity.PetEntity;
import com.coding2.the.max.petstore.catalog.domain.repository.PetCompleteDetailsRepository;
import com.coding2.the.max.petstore.catalog.domain.repository.PetRepository;
import com.coding2.the.max.petstore.catalog.dto.AvailabilityUpdateRequest;
import com.coding2.the.max.petstore.catalog.dto.UpdatePetRequest;
import com.coding2.the.max.petstore.catalog.openapi.model.NewPet;
import com.coding2.the.max.petstore.catalog.openapi.model.Pet;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

  private final PetRepository petRepository;
  private final PetMapper petMapper;
  private final PetCompleteDetailsMapper petCompleteDetailsMapper;
  private final PetCompleteDetailsRepository petCompleteDetailsRepository;

  @Override
  public Flux<Pet> searchPets(PetEntity.Species species, String breed, PetEntity.Size size,
      String ageCategory, Double priceMin, Double priceMax,
      PetEntity.Availability availability, PetEntity.Gender gender,
      Boolean vaccinated, String sortBy, String sortOrder,
      Integer page, Integer limit) {
    // Convert availability to boolean for repository query
    Boolean isAvailable = availability != null ? availability == PetEntity.Availability.AVAILABLE : null;

    // Calculate offset from page and limit
    Integer offset = (page != null && limit != null) ? page * limit : 0;

    // Use PetRepository to search with filters
    return petRepository.findPetsWithFilters(
        limit != null ? limit : 20, // Default limit of 20
        offset,
        species != null ? species.toString() : null,
        isAvailable,
        priceMin,
        priceMax).map(petMapper::toApiModel);
  }

  @Override
  public Mono<Pet> getPetById(String petId) {
    return petRepository.findByIdWithDetails(petId)
        .map(petMapper::toApiModel);
  }

  @Override
  public Mono<Pet> createPet(NewPet newPet) {
    // Map NewPet to PetEntity
    PetEntity petEntity = PetEntity.builder()
        .id(newPet.getId() != null ? newPet.getId() : UUID.randomUUID().toString())
        .name(newPet.getName())
        .age(newPet.getAge() != null ? newPet.getAge().intValue() : null)
        .size(mapSizeFromApi(newPet.getSize()))
        .gender(mapGenderFromApi(newPet.getGender()))
        .price(newPet.getPrice())
        .description(newPet.getDescription())
        .availability(newPet.getIsAvailable() != null && newPet.getIsAvailable() ? PetEntity.Availability.AVAILABLE
            : PetEntity.Availability.COMING_SOON)
        .characteristics(newPet.getCharacteristics())
        .build();

    // Save to database and map back to API model
    return petRepository.save(petEntity)
        .flatMap(saved -> petRepository.findByIdWithDetails(saved.getId()))
        .map(petMapper::toApiModel);
  }

  private PetEntity.Size mapSizeFromApi(com.coding2.the.max.petstore.catalog.openapi.model.NewPet.SizeEnum size) {
    if (size == null)
      return null;
    return switch (size) {
      case SMALL -> PetEntity.Size.SMALL;
      case MEDIUM -> PetEntity.Size.MEDIUM;
      case LARGE -> PetEntity.Size.LARGE;
      case EXTRA_LARGE -> PetEntity.Size.EXTRA_LARGE;
    };
  }

  private PetEntity.Gender mapGenderFromApi(
      com.coding2.the.max.petstore.catalog.openapi.model.NewPet.GenderEnum gender) {
    if (gender == null)
      return null;
    return switch (gender) {
      case MALE -> PetEntity.Gender.MALE;
      case FEMALE -> PetEntity.Gender.FEMALE;
    };
  }

  @Override
  public Mono<Pet> updatePet(String petId, UpdatePetRequest request) {
    return petRepository.findById(petId)
        .switchIfEmpty(Mono.error(new RuntimeException("Pet not found with id: " + petId)))
        .flatMap(existingPet -> {
          // Update only the fields that are provided in the request
          if (request.getName() != null) {
            existingPet.setName(request.getName());
          }
          if (request.getAge() != null) {
            existingPet.setAge(request.getAge());
          }
          if (request.getPrice() != null) {
            existingPet.setPrice(java.math.BigDecimal.valueOf(request.getPrice()));
          }
          if (request.getDescription() != null) {
            existingPet.setDescription(request.getDescription());
          }
          if (request.getCharacteristics() != null) {
            existingPet.setCharacteristics(request.getCharacteristics());
          }

          return petRepository.save(existingPet);
        })
        .flatMap(saved -> petRepository.findByIdWithDetails(saved.getId()))
        .map(petMapper::toApiModel);
  }

  @Override
  public Mono<Void> deletePet(String petId) {
    return petRepository.existsById(petId)
        .flatMap(exists -> {
          if (!exists) {
            return Mono.error(new RuntimeException("Pet not found with id: " + petId));
          }
          return petRepository.deleteById(petId);
        });
  }

  @Override
  public Mono<Pet> updatePetAvailability(String petId, AvailabilityUpdateRequest request) {
    return petRepository.findById(petId)
        .switchIfEmpty(Mono.error(new RuntimeException("Pet not found with id: " + petId)))
        .flatMap(existingPet -> {
          existingPet.setAvailability(request.getAvailability());
          return petRepository.save(existingPet);
        })
        .flatMap(saved -> petRepository.findByIdWithDetails(saved.getId()))
        .map(petMapper::toApiModel);
  }

  @Override
  public Flux<Pet> listPets(Integer limit, Integer offset, PetEntity.Species species,
      Boolean isAvailable, Double minPrice, Double maxPrice) {
    return petCompleteDetailsRepository
        .findPetsWithFilters(limit, offset, species.toString(), isAvailable, minPrice, maxPrice)
        .map(petCompleteDetailsMapper::toApiModel);
  }

  @Override
  public Flux<Pet> getAllFlux() {
    return petCompleteDetailsRepository.findAll().map(petCompleteDetailsMapper::toApiModel);
  }
}
