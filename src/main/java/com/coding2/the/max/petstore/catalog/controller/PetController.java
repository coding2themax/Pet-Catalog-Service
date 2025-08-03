package com.coding2.the.max.petstore.catalog.controller;

import com.coding2.the.max.petstore.catalog.dto.*;
import com.coding2.the.max.petstore.catalog.exception.PetNotFoundException;
import com.coding2.the.max.petstore.catalog.model.Pet;
import com.coding2.the.max.petstore.catalog.service.PetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

@RestController
@RequestMapping("/catalog/v1/pets")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PetController {

  private final PetService petService;

  @GetMapping
  public Mono<ResponseEntity<PetSearchResponse>> searchPets(
      @RequestParam(required = false) Pet.Species species,
      @RequestParam(required = false) String breed,
      @RequestParam(required = false) Pet.Size size,
      @RequestParam(name = "age_range", required = false) Pet.AgeCategory ageRange,
      @RequestParam(name = "price_min", required = false) Double priceMin,
      @RequestParam(name = "price_max", required = false) Double priceMax,
      @RequestParam(required = false) Pet.Availability availability,
      @RequestParam(required = false) Pet.Gender gender,
      @RequestParam(required = false) Boolean vaccinated,
      @RequestParam(name = "sort_by", required = false, defaultValue = "date_added") @Pattern(regexp = "price|age|date_added|popularity") String sortBy,
      @RequestParam(name = "sort_order", required = false, defaultValue = "desc") @Pattern(regexp = "asc|desc") String sortOrder,
      @RequestParam(required = false, defaultValue = "1") Integer page,
      @RequestParam(required = false, defaultValue = "20") Integer limit) {

    log.info("Searching pets with filters - species: {}, breed: {}, size: {}", species, breed, size);

    return petService.searchPets(species, breed, size, ageRange, priceMin, priceMax,
        availability, gender, vaccinated, sortBy, sortOrder, page, limit)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.ok(PetSearchResponse.builder().build()));
  }

  @PostMapping
  public Mono<ResponseEntity<Pet>> createPet(@Valid @RequestBody CreatePetRequest request) {
    log.info("Creating new pet: {}", request.getName());

    return petService.createPet(request)
        .map(pet -> ResponseEntity.status(HttpStatus.CREATED).body(pet));
  }

  @GetMapping("/{petId}")
  public Mono<ResponseEntity<Pet>> getPetById(
      @PathVariable @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$") String petId) {

    log.info("Getting pet by ID: {}", petId);

    return petService.getPetById(petId)
        .map(ResponseEntity::ok)
        .switchIfEmpty(Mono.error(new PetNotFoundException(petId)));
  }

  @PutMapping("/{petId}")
  public Mono<ResponseEntity<Pet>> updatePet(
      @PathVariable @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$") String petId,
      @Valid @RequestBody UpdatePetRequest request) {

    log.info("Updating pet: {}", petId);

    return petService.updatePet(petId, request)
        .map(ResponseEntity::ok)
        .switchIfEmpty(Mono.error(new PetNotFoundException(petId)));
  }

  @DeleteMapping("/{petId}")
  public Mono<ResponseEntity<Void>> deletePet(
      @PathVariable @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$") String petId) {

    log.info("Deleting pet: {}", petId);

    return petService.deletePet(petId)
        .then(Mono.just(ResponseEntity.noContent().<Void>build()))
        .switchIfEmpty(Mono.error(new PetNotFoundException(petId)));
  }

  @PatchMapping("/{petId}/availability")
  public Mono<ResponseEntity<Pet>> updatePetAvailability(
      @PathVariable @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$") String petId,
      @Valid @RequestBody AvailabilityUpdateRequest request) {

    log.info("Updating availability for pet: {} to {}", petId, request.getAvailability());

    return petService.updatePetAvailability(petId, request)
        .map(ResponseEntity::ok)
        .switchIfEmpty(Mono.error(new PetNotFoundException(petId)));
  }
}
