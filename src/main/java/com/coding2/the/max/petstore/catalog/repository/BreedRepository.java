package com.coding2.the.max.petstore.catalog.repository;

import com.coding2.the.max.petstore.catalog.model.Breed;
import com.coding2.the.max.petstore.catalog.model.Pet;
import org.springframework.data.r2dbc.repository.Query;
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
  @Query("SELECT id, name, species, typical_size, characteristics, created_at FROM breeds WHERE species = $1::species_enum")
  Flux<Breed> findBySpecies(Pet.Species species);

  /**
   * Find a breed by name and species
   * 
   * @param name    the breed name
   * @param species the species
   * @return Mono of the breed if found
   */
  @Query("SELECT id, name, species, typical_size, characteristics, created_at FROM breeds WHERE name = $1 AND species = $2::species_enum")
  reactor.core.publisher.Mono<Breed> findByNameAndSpecies(String name, Pet.Species species);
}
