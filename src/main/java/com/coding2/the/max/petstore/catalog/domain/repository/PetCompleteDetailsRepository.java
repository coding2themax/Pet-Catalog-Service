package com.coding2.the.max.petstore.catalog.domain.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.coding2.the.max.petstore.catalog.domain.entity.PetCompleteDetailsEntity;

/**
 * Repository interface for the pets_complete_details view.
 * This provides simplified queries since all necessary data is pre-joined in
 * the view.
 */
public interface PetCompleteDetailsRepository extends ReactiveCrudRepository<PetCompleteDetailsEntity, String> {

  /**
   * Find pets with optional filters and pagination.
   * Uses the enhanced view for simplified querying.
   */
  @Query("""
      SELECT * FROM pets_complete_details
      WHERE (:species IS NULL OR species = :species)
      AND (:isAvailable IS NULL OR is_available = :isAvailable)
      AND (:minPrice IS NULL OR price >= :minPrice)
      AND (:maxPrice IS NULL OR price <= :maxPrice)
      ORDER BY created_at DESC
      LIMIT :limit OFFSET :offset
      """)
  Flux<PetCompleteDetailsEntity> findPetsWithFilters(
      Integer limit,
      Integer offset,
      String species,
      Boolean isAvailable,
      Double minPrice,
      Double maxPrice);

  /**
   * Count pets with filters for pagination metadata.
   */
  @Query("""
      SELECT COUNT(*) FROM pets_complete_details
      WHERE (:species IS NULL OR species = :species)
      AND (:isAvailable IS NULL OR is_available = :isAvailable)
      AND (:minPrice IS NULL OR price >= :minPrice)
      AND (:maxPrice IS NULL OR price <= :maxPrice)
      """)
  Mono<Long> countPetsWithFilters(
      String species,
      Boolean isAvailable,
      Double minPrice,
      Double maxPrice);

  /**
   * Find a pet by ID with all details.
   */
  @Query("SELECT * FROM pets_complete_details WHERE id = :id")
  Mono<PetCompleteDetailsEntity> findByIdWithAllDetails(String id);
}