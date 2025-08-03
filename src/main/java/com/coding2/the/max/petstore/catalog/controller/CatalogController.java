package com.coding2.the.max.petstore.catalog.controller;

import com.coding2.the.max.petstore.catalog.dto.BreedsResponse;
import com.coding2.the.max.petstore.catalog.dto.SpeciesResponse;
import com.coding2.the.max.petstore.catalog.model.Pet;
import com.coding2.the.max.petstore.catalog.service.CatalogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/catalog/v1")
@RequiredArgsConstructor
@Slf4j
public class CatalogController {

  private final CatalogService catalogService;

  @GetMapping("/breeds")
  public Mono<ResponseEntity<BreedsResponse>> getBreeds(
      @RequestParam(required = false) Pet.Species species) {

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
