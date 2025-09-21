package com.coding2.the.max.petstore.catalog.domain.service;

import org.springframework.stereotype.Component;
import com.coding2.the.max.petstore.catalog.domain.entity.PetEntity;
import com.coding2.the.max.petstore.catalog.openapi.model.Pet;

import java.math.BigDecimal;
import java.util.ArrayList;

@Component
public class PetMapper {

  public Pet toApiModel(PetEntity entity) {
    Pet pet = new Pet();
    pet.setId(entity.getId());
    pet.setName(entity.getName());
    pet.setAge(entity.getAge() != null ? BigDecimal.valueOf(entity.getAge()) : null);
    pet.setBreed(entity.getBreedName());
    pet.setSpecies(entity.getSpecies() != null ? entity.getSpecies().name().toLowerCase().replace('_', '-') : null);
    pet.setSize(mapSize(entity.getSize()));
    pet.setGender(mapGender(entity.getGender()));
    pet.setPrice(entity.getPrice());
    pet.setDescription(entity.getDescription());
    pet.setIsAvailable(entity.getAvailability() == PetEntity.Availability.AVAILABLE);

    // Map health info if available
    if (entity.getHealthInfo() != null) {
      pet.setVaccinated(entity.getHealthInfo().getVaccinated());
      pet.setSpayedNeutered(entity.getHealthInfo().getSpayedNeutered());
      pet.setHealthStatus(mapHealthStatus(entity.getHealthInfo().getHealthCertificate()));
    }

    // Map characteristics
    pet.setCharacteristics(entity.getCharacteristics() != null ? entity.getCharacteristics() : new ArrayList<>());

    // Set primary image URL if available
    if (entity.getImages() != null && !entity.getImages().isEmpty()) {
      entity.getImages().stream()
          .filter(img -> img.getIsPrimary())
          .findFirst()
          .ifPresent(img -> pet.setImageUrl(img.getUrl()));
    }

    // Set default values for required fields that might be missing
    if (pet.getCharacteristics() == null) {
      pet.setCharacteristics(new ArrayList<>());
    }
    if (pet.getGoodWithKids() == null) {
      pet.setGoodWithKids(false);
    }
    if (pet.getGoodWithPets() == null) {
      pet.setGoodWithPets(false);
    }
    if (pet.getVaccinated() == null) {
      pet.setVaccinated(false);
    }
    if (pet.getSpayedNeutered() == null) {
      pet.setSpayedNeutered(false);
    }
    if (pet.getEnergyLevel() == null) {
      pet.setEnergyLevel(Pet.EnergyLevelEnum.MEDIUM);
    }
    if (pet.getHealthStatus() == null) {
      pet.setHealthStatus(Pet.HealthStatusEnum.GOOD);
    }

    return pet;
  }

  private Pet.SizeEnum mapSize(PetEntity.Size size) {
    if (size == null)
      return null;
    return switch (size) {
      case SMALL -> Pet.SizeEnum.SMALL;
      case MEDIUM -> Pet.SizeEnum.MEDIUM;
      case LARGE -> Pet.SizeEnum.LARGE;
      case EXTRA_LARGE -> Pet.SizeEnum.EXTRA_LARGE;
    };
  }

  private Pet.GenderEnum mapGender(PetEntity.Gender gender) {
    if (gender == null)
      return null;
    return switch (gender) {
      case MALE -> Pet.GenderEnum.MALE;
      case FEMALE -> Pet.GenderEnum.FEMALE;
    };
  }

  private Pet.HealthStatusEnum mapHealthStatus(Boolean healthCertificate) {
    if (healthCertificate == null || !healthCertificate) {
      return Pet.HealthStatusEnum.FAIR;
    }
    return Pet.HealthStatusEnum.EXCELLENT;
  }
}