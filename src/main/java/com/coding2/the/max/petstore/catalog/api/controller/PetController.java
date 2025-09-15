package com.coding2.the.max.petstore.catalog.api.controller;

import com.coding2.the.max.petstore.catalog.domain.entity.PetEntity;
import com.coding2.the.max.petstore.catalog.domain.service.PetService;
import com.coding2.the.max.petstore.catalog.dto.*;
import com.coding2.the.max.petstore.catalog.exception.PetNotFoundException;
import com.coding2.the.max.petstore.catalog.openapi.api.PetsApi;
import com.coding2.the.max.petstore.catalog.openapi.model.ListPets200Response;
import com.coding2.the.max.petstore.catalog.openapi.model.Pet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

@RestController
@RequestMapping("/catalog/v1")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PetController implements PetsApi {

  private final PetService petService;

  @Override
  public Mono<ResponseEntity<ListPets200Response>> listPets(@Min(1) @Max(100) @Valid Integer limit,
      @Min(0) @Valid Integer offset, @Valid String species, @Valid Boolean isAvailable,
      @DecimalMin("0") @Valid BigDecimal minPrice, @DecimalMin("0") @Valid BigDecimal maxPrice,
      ServerWebExchange exchange) {
    PetEntity.Species speciesEnum = toSpeciesEnum(species);
    Double min = (minPrice != null) ? minPrice.doubleValue() : null;
    Double max = (maxPrice != null) ? maxPrice.doubleValue() : null;

    return petService.listPets(limit, offset, speciesEnum, isAvailable, min, max)
        .collectList()
        .map(items -> {
          ListPets200Response response = new ListPets200Response();
          response.setItems(items);
          response.setTotal(items != null ? items.size() : 0);
          return ResponseEntity.ok(response);
        });
  }

  private PetEntity.Species toSpeciesEnum(String value) {
    if (value == null || value.isBlank())
      return null;
    String normalized = value.trim().replace('-', '_').replace(' ', '_').toUpperCase();
    try {
      return PetEntity.Species.valueOf(normalized);
    } catch (IllegalArgumentException ex) {
      log.warn("Unknown species filter received: {}", value);
      return null;
    }
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
