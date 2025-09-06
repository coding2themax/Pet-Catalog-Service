package com.coding2.the.max.petstore.catalog.service;

import com.coding2.the.max.petstore.catalog.dto.BreedsResponse;
import com.coding2.the.max.petstore.catalog.dto.SpeciesResponse;
import com.coding2.the.max.petstore.catalog.model.Pet;

import reactor.core.publisher.Mono;

public interface CatalogService {

  Mono<BreedsResponse> getBreeds(Pet.Species species);

  Mono<SpeciesResponse> getSpecies();

}