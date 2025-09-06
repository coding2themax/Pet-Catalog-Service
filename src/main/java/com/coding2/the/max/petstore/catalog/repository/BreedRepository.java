package com.coding2.the.max.petstore.catalog.repository;

import com.coding2.the.max.petstore.catalog.model.Breed;
import com.coding2.the.max.petstore.catalog.model.Pet;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface BreedRepository extends ReactiveCrudRepository<Breed, Long> {

  /**
   * Find all breeds for a specific species
   * 
   * @param species the species to filter by
   * @return Flux of breeds matching the species
   */
  Flux<Breed> findBySpecies(Pet.Species species);

  /**
   * Find a breed by name and species
   * 
   * @param name    the breed name
   * @param species the species
   * @return Mono of the breed if found
   */
  reactor.core.publisher.Mono<Breed> findByNameAndSpecies(String name, Pet.Species species);
}
