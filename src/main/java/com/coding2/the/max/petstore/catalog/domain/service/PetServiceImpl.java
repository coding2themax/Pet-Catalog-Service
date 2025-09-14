package com.coding2.the.max.petstore.catalog.domain.service;

import com.coding2.the.max.petstore.catalog.domain.entity.PetEntity;
import com.coding2.the.max.petstore.catalog.dto.*;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.*;

@Service
public class PetServiceImpl implements PetService {

  @Override
  public Mono<PetSearchResponse> searchPets(PetEntity.Species species, String breed, PetEntity.Size size,
      String ageCategory, Double priceMin, Double priceMax,
      PetEntity.Availability availability, PetEntity.Gender gender,
      Boolean vaccinated, String sortBy, String sortOrder,
      Integer page, Integer limit) {

    // For now, return empty search results
    Pagination pagination = Pagination.builder()
        .page(page != null ? page : 1)
        .limit(limit != null ? limit : 10)
        .totalItems(0L)
        .totalPages(0)
        .hasNext(false)
        .hasPrev(false)
        .build();

    return Mono.just(PetSearchResponse.builder()
        .pets(Collections.emptyList())
        .pagination(pagination)
        .filtersApplied(new HashMap<>())
        .build());
  }

  @Override
  public Mono<PetResponseDTO> createPet(CreatePetRequest request) {
    String id = UUID.randomUUID().toString();

    PetResponseDTO response = PetResponseDTO.builder()
        .id(id)
        .name(request.getName())
        .species(request.getSpecies())
        .breed(request.getBreed())
        .age(request.getAge())
        .size(request.getSize())
        .gender(request.getGender())
        .price(BigDecimal.valueOf(request.getPrice()))
        .description(request.getDescription())
        .characteristics(request.getCharacteristics())
        .isAvailable(true)
        .vaccinated(request.getHealthInfo() != null ? request.getHealthInfo().getVaccinated() : null)
        .spayedNeutered(request.getHealthInfo() != null ? request.getHealthInfo().getSpayedNeutered() : null)
        .build();

    return Mono.just(response);
  }

  @Override
  public Mono<PetResponseDTO> getPetById(String petId) {
    // For testing purposes, return empty if not found
    return Mono.empty();
  }

  @Override
  public Mono<PetResponseDTO> updatePet(String petId, UpdatePetRequest request) {
    return Mono.empty();
  }

  @Override
  public Mono<Void> deletePet(String petId) {
    return Mono.empty().then();
  }

  @Override
  public Mono<PetResponseDTO> updatePetAvailability(String petId, AvailabilityUpdateRequest request) {
    return Mono.empty();
  }
}
