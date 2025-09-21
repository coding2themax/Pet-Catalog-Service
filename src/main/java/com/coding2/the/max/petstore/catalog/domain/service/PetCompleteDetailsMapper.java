package com.coding2.the.max.petstore.catalog.domain.service;

import org.springframework.stereotype.Component;
import com.coding2.the.max.petstore.catalog.domain.entity.PetCompleteDetailsEntity;
import com.coding2.the.max.petstore.catalog.openapi.model.Pet;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Mapper for converting PetCompleteDetailsEntity (from the enhanced view) to
 * Pet API model.
 * This mapper is much simpler since all necessary data is already available in
 * the view entity.
 */
@Component
public class PetCompleteDetailsMapper {

  public Pet toApiModel(PetCompleteDetailsEntity entity) {
    Pet pet = new Pet();

    // Core pet information
    pet.setId(entity.getId());
    pet.setName(entity.getName());
    pet.setAge(entity.getAge() != null ? BigDecimal.valueOf(entity.getAge()) : null);
    pet.setPrice(entity.getPrice());
    pet.setDescription(entity.getDescription());

    // Breed and species information (directly from view)
    pet.setBreed(entity.getBreedName());
    pet.setSpecies(entity.getSpeciesAsString());

    // Enum mappings
    pet.setSize(mapSize(entity.getSize()));
    pet.setGender(mapGender(entity.getGender()));

    // Availability (using the computed field from view)
    pet.setIsAvailable(entity.getIsAvailableBoolean());

    // Health information (directly from view with defaults already applied)
    pet.setVaccinated(entity.getVaccinated() != null ? entity.getVaccinated() : false);
    pet.setSpayedNeutered(entity.getSpayedNeutered() != null ? entity.getSpayedNeutered() : false);
    pet.setHealthStatus(mapHealthStatus(entity.getHealthCertificate()));

    // Image URL (directly from view)
    pet.setImageUrl(entity.getPrimaryImageUrl());

    // Set default values for required fields that might be missing
    pet.setCharacteristics(new ArrayList<>()); // TODO: Add characteristics if needed
    pet.setGoodWithKids(false); // TODO: Add to view if this data exists
    pet.setGoodWithPets(false); // TODO: Add to view if this data exists
    pet.setEnergyLevel(Pet.EnergyLevelEnum.MEDIUM); // TODO: Add to view if this data exists

    return pet;
  }

  private Pet.SizeEnum mapSize(com.coding2.the.max.petstore.catalog.domain.entity.PetEntity.Size size) {
    if (size == null)
      return null;
    return switch (size) {
      case SMALL -> Pet.SizeEnum.SMALL;
      case MEDIUM -> Pet.SizeEnum.MEDIUM;
      case LARGE -> Pet.SizeEnum.LARGE;
      case EXTRA_LARGE -> Pet.SizeEnum.EXTRA_LARGE;
    };
  }

  private Pet.GenderEnum mapGender(com.coding2.the.max.petstore.catalog.domain.entity.PetEntity.Gender gender) {
    if (gender == null)
      return null;
    return switch (gender) {
      case MALE -> Pet.GenderEnum.MALE;
      case FEMALE -> Pet.GenderEnum.FEMALE;
    };
  }

  private Pet.HealthStatusEnum mapHealthStatus(Boolean healthCertificate) {
    if (healthCertificate == null || !healthCertificate) {
      return Pet.HealthStatusEnum.GOOD;
    }
    return Pet.HealthStatusEnum.EXCELLENT;
  }
}