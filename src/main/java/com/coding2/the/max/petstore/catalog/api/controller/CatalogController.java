package com.coding2.the.max.petstore.catalog.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.coding2.the.max.petstore.catalog.domain.entity.PetEntity;
import com.coding2.the.max.petstore.catalog.domain.service.CatalogService;
import com.coding2.the.max.petstore.catalog.dto.BreedsResponse;
import com.coding2.the.max.petstore.catalog.dto.SpeciesResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/catalog/v1")
@RequiredArgsConstructor
@Slf4j
public class CatalogController {

  private final CatalogService catalogService;

  @GetMapping("/breeds")
  public Mono<ResponseEntity<BreedsResponse>> getBreeds(
      @RequestParam(required = false) PetEntity.Species species) {

    log.info("Getting breeds for species: {}", species);

    return catalogService.getBreeds(species)
        .map(ResponseEntity::ok);
  }

  @GetMapping("/species")
  public Mono<ResponseEntity<SpeciesResponse>> getSpecies() {
    log.info("Getting all species");

    return catalogService.getSpecies()
        .map(ResponseEntity::ok);
  }
}
