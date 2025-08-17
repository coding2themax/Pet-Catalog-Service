package com.coding2.the.max.petstore.catalog.service;

import com.coding2.the.max.petstore.catalog.dto.AvailabilityUpdateRequest;
import com.coding2.the.max.petstore.catalog.dto.CreatePetRequest;
import com.coding2.the.max.petstore.catalog.dto.PetSearchResponse;
import com.coding2.the.max.petstore.catalog.dto.UpdatePetRequest;
import com.coding2.the.max.petstore.catalog.model.Pet;

import reactor.core.publisher.Mono;

public interface PetService {

  Mono<PetSearchResponse> searchPets(Pet.Species species, String breed, Pet.Size size,
      String ageCategory, Double priceMin, Double priceMax,
      Pet.Availability availability, Pet.Gender gender,
      Boolean vaccinated, String sortBy, String sortOrder,
      Integer page, Integer limit);

  Mono<Pet> createPet(CreatePetRequest request);

  Mono<Pet> getPetById(String petId);

  Mono<Pet> updatePet(String petId, UpdatePetRequest request);

  Mono<Void> deletePet(String petId);

  Mono<Pet> updatePetAvailability(String petId, AvailabilityUpdateRequest request);

}