package com.coding2.the.max.petstore.catalog.exception;

public class PetNotFoundException extends RuntimeException {

  public PetNotFoundException(String petId) {
    super("Pet not found with ID: " + petId);
  }
}
