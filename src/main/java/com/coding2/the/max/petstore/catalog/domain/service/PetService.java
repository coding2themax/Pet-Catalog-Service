package com.coding2.the.max.petstore.catalog.domain.service;

import com.coding2.the.max.petstore.catalog.domain.entity.PetEntity;
import com.coding2.the.max.petstore.catalog.dto.AvailabilityUpdateRequest;
import com.coding2.the.max.petstore.catalog.dto.CreatePetRequest;
import com.coding2.the.max.petstore.catalog.dto.PetResponseDTO;
import com.coding2.the.max.petstore.catalog.dto.PetSearchResponse;
import com.coding2.the.max.petstore.catalog.dto.UpdatePetRequest;

import reactor.core.publisher.Mono;

public interface PetService {

  Mono<PetSearchResponse> searchPets(PetEntity.Species species, String breed, PetEntity.Size size,
      String ageCategory, Double priceMin, Double priceMax,
      PetEntity.Availability availability, PetEntity.Gender gender,
      Boolean vaccinated, String sortBy, String sortOrder,
      Integer page, Integer limit);

  Mono<PetResponseDTO> createPet(CreatePetRequest request);

  Mono<PetResponseDTO> getPetById(String petId);

  Mono<PetResponseDTO> updatePet(String petId, UpdatePetRequest request);

  Mono<Void> deletePet(String petId);

  Mono<PetResponseDTO> updatePetAvailability(String petId, AvailabilityUpdateRequest request);

}