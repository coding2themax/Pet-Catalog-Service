package com.coding2.the.max.petstore.catalog.domain.service;

import com.coding2.the.max.petstore.catalog.domain.entity.PetEntity;
import com.coding2.the.max.petstore.catalog.dto.AvailabilityUpdateRequest;
import com.coding2.the.max.petstore.catalog.dto.UpdatePetRequest;
import com.coding2.the.max.petstore.catalog.openapi.model.Pet;
import com.coding2.the.max.petstore.catalog.openapi.model.NewPet;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PetService {

    Flux<Pet> searchPets(PetEntity.Species species, String breed, PetEntity.Size size,
            String ageCategory, Double priceMin, Double priceMax,
            PetEntity.Availability availability, PetEntity.Gender gender,
            Boolean vaccinated, String sortBy, String sortOrder,
            Integer page, Integer limit);

    // Mono<Pet> createPet(CreatePetRequest request);
    Mono<Pet> createPet(NewPet newPet);

    Mono<Pet> getPetById(String petId);

    Mono<Pet> updatePet(String petId, UpdatePetRequest request);

    Mono<Void> deletePet(String petId);

    Mono<Pet> updatePetAvailability(String petId, AvailabilityUpdateRequest request);

    /**
     * List pets with optional filters and pagination.
     *
     * @param species     filter by species (optional)
     * @param isAvailable filter by availability flag (optional)
     * @param minPrice    minimum price filter (optional)
     * @param maxPrice    maximum price filter (optional)
     * @param offset      number of items to skip for pagination (optional)
     * @param limit       maximum number of items to return (optional)
     * @return a response containing the list of pets and pagination info
     */
    Flux<Pet> listPets(
            Integer limit,
            Integer offset,
            PetEntity.Species species,
            Boolean isAvailable,
            Double minPrice,
            Double maxPrice);

    Flux<Pet> getAllFlux();

}