package com.coding2.the.max.petstore.catalog.domain.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.coding2.the.max.petstore.catalog.domain.entity.PetEntity;

public interface PetRepository extends ReactiveCrudRepository<PetEntity, String> {

}
