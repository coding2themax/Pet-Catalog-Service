package com.coding2.the.max.petstore.catalog.domain.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.coding2.the.max.petstore.catalog.domain.entity.PetEntity;

@Repository
public interface PetRepository extends ReactiveCrudRepository<PetEntity, String> {

    @Query("""
            SELECT p.*,
                   b.name as breed_name,
                   b.species,
                   pac.age_category,
                   h.vaccinated,
                   h.spayed_neutered,
                   h.health_certificate,
                   l.store_name,
                   l.city,
                   l.state
            FROM pets_with_details p
            LEFT JOIN health_info h ON h.id = p.health_info_id
            LEFT JOIN locations l ON l.id = p.location_id
            WHERE (:species IS NULL OR p.species = :species)
            AND (:isAvailable IS NULL OR
                 ((:isAvailable = true AND p.availability = 'available') OR
                  (:isAvailable = false AND p.availability != 'available')))
            AND (:minPrice IS NULL OR p.price >= :minPrice)
            AND (:maxPrice IS NULL OR p.price <= :maxPrice)
            ORDER BY p.created_at DESC
            LIMIT :limit OFFSET :offset
            """)
    Flux<PetEntity> findPetsWithFilters(Integer limit, Integer offset, String species,
            Boolean isAvailable, Double minPrice, Double maxPrice);

    @Query("""
            SELECT p.*,
                   b.name as breed_name,
                   b.species,
                   pac.age_category,
                   h.vaccinated,
                   h.spayed_neutered,
                   h.health_certificate,
                   l.store_name,
                   l.city,
                   l.state
            FROM pets_with_details p
            LEFT JOIN health_info h ON h.id = p.health_info_id
            LEFT JOIN locations l ON l.id = p.location_id
            WHERE p.id = :id
            """)
    Mono<PetEntity> findByIdWithDetails(String id);
}
