package com.coding2.the.max.petstore.catalog.api.controller;

import java.math.BigDecimal;
import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.coding2.the.max.petstore.catalog.domain.entity.PetEntity;
import com.coding2.the.max.petstore.catalog.domain.service.PetService;
import com.coding2.the.max.petstore.catalog.openapi.api.PetsApi;
import com.coding2.the.max.petstore.catalog.openapi.model.ListPets200Response;
import com.coding2.the.max.petstore.catalog.openapi.model.NewPet;
import com.coding2.the.max.petstore.catalog.openapi.model.Pet;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/catalog/v1")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PetController implements PetsApi {

  private final PetService petService;

  @Override
  public Mono<ResponseEntity<Pet>> createPet(@Valid Mono<NewPet> newPet, ServerWebExchange exchange) {
    return newPet
        .flatMap(petService::createPet)
        .map(created -> ResponseEntity
            .created(URI.create("/catalog/v1/pets/" + created.getId()))
            .body(created));
  }

  @Override
  public Mono<ResponseEntity<Pet>> getPet(String id, ServerWebExchange exchange) {
    return petService.getPetById(id)
        .map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<ListPets200Response>> listPets(@Min(1) @Max(100) @Valid Integer limit,
      @Min(0) @Valid Integer offset, @Valid String species, @Valid Boolean isAvailable,
      @DecimalMin("0") @Valid BigDecimal minPrice, @DecimalMin("0") @Valid BigDecimal maxPrice,
      ServerWebExchange exchange) {

    return petService.getAllFlux()
        .collectList()
        .map(items -> {
          ListPets200Response response = new ListPets200Response();
          response.setItems(items);
          response.setTotal(items != null ? items.size() : 0);
          return ResponseEntity.ok(response);
        });
  }

}
