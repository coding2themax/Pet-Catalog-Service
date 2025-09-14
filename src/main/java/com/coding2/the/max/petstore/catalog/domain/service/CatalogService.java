package com.coding2.the.max.petstore.catalog.domain.service;

import com.coding2.the.max.petstore.catalog.domain.entity.PetEntity;
import com.coding2.the.max.petstore.catalog.dto.BreedsResponse;
import com.coding2.the.max.petstore.catalog.dto.SpeciesResponse;

import reactor.core.publisher.Mono;

public interface CatalogService {

  Mono<BreedsResponse> getBreeds(PetEntity.Species species);

  Mono<SpeciesResponse> getSpecies();

}