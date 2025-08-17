package com.coding2.the.max.petstore.catalog.config;

import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.coding2.the.max.petstore.catalog.dto.CreatePetRequest;
import com.coding2.the.max.petstore.catalog.model.HealthInfo;
import com.coding2.the.max.petstore.catalog.model.Location;
import com.coding2.the.max.petstore.catalog.model.Pet;
import com.coding2.the.max.petstore.catalog.model.PetImage;
import com.coding2.the.max.petstore.catalog.service.PetServiceLocalData;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Profile("dev")
@Component
@RequiredArgsConstructor
@Slf4j
public class SampleDataInitializer implements CommandLineRunner {

        private final PetServiceLocalData petService;

        @Override
        public void run(String... args) throws Exception {
                log.info("Initializing sample pet data...");

                // Sample Pet 1: Golden Retriever
                CreatePetRequest pet1 = CreatePetRequest.builder()
                                .name("Buddy")
                                .species(Pet.Species.DOG)
                                .breed("Golden Retriever")
                                .age(24)
                                .size(Pet.Size.LARGE)
                                .gender(Pet.Gender.MALE)
                                .price(1200.0)
                                .description("Friendly and energetic Golden Retriever, great with kids")
                                .characteristics(Arrays.asList("friendly", "energetic", "good-with-kids",
                                                "house-trained"))
                                .healthInfo(HealthInfo.builder()
                                                .vaccinated(true)
                                                .spayedNeutered(false)
                                                .healthCertificate(true)
                                                .lastVetVisit(LocalDate.of(2025, 7, 15))
                                                .build())
                                .location(Location.builder()
                                                .storeId("store-123")
                                                .storeName("PetStore Downtown")
                                                .city("San Francisco")
                                                .state("CA")
                                                .zipCode("94105")
                                                .build())
                                .images(Arrays.asList(PetImage.builder()
                                                .url("https://images.petstore.com/pets/buddy-1.jpg")
                                                .altText("Golden Retriever sitting in grass")
                                                .isPrimary(true)
                                                .build()))
                                .build();

                // Sample Pet 2: Persian Cat
                CreatePetRequest pet2 = CreatePetRequest.builder()
                                .name("Luna")
                                .species(Pet.Species.CAT)
                                .breed("Persian")
                                .age(18)
                                .size(Pet.Size.MEDIUM)
                                .gender(Pet.Gender.FEMALE)
                                .price(800.0)
                                .description("Beautiful Persian cat with long silky fur")
                                .characteristics(Arrays.asList("quiet", "docile", "sweet", "indoor-cat"))
                                .healthInfo(HealthInfo.builder()
                                                .vaccinated(true)
                                                .spayedNeutered(true)
                                                .healthCertificate(true)
                                                .lastVetVisit(LocalDate.of(2025, 6, 20))
                                                .build())
                                .location(Location.builder()
                                                .storeId("store-456")
                                                .storeName("PetStore Uptown")
                                                .city("San Francisco")
                                                .state("CA")
                                                .zipCode("94110")
                                                .build())
                                .images(Arrays.asList(PetImage.builder()
                                                .url("https://images.petstore.com/pets/luna-1.jpg")
                                                .altText("Persian cat with blue eyes")
                                                .isPrimary(true)
                                                .build()))
                                .build();

                // Sample Pet 3: Parakeet
                CreatePetRequest pet3 = CreatePetRequest.builder()
                                .name("Charlie")
                                .species(Pet.Species.BIRD)
                                .breed("Parakeet")
                                .age(6)
                                .size(Pet.Size.SMALL)
                                .gender(Pet.Gender.MALE)
                                .price(150.0)
                                .description("Colorful and social parakeet that loves to sing")
                                .characteristics(Arrays.asList("playful", "social", "intelligent", "vocal"))
                                .healthInfo(HealthInfo.builder()
                                                .vaccinated(false)
                                                .healthCertificate(true)
                                                .lastVetVisit(LocalDate.of(2025, 7, 1))
                                                .build())
                                .location(Location.builder()
                                                .storeId("store-789")
                                                .storeName("PetStore Birds & More")
                                                .city("San Francisco")
                                                .state("CA")
                                                .zipCode("94102")
                                                .build())
                                .images(Arrays.asList(PetImage.builder()
                                                .url("https://images.petstore.com/pets/charlie-1.jpg")
                                                .altText("Colorful parakeet on perch")
                                                .isPrimary(true)
                                                .build()))
                                .build();

                // Create sample pets
                petService.createPet(pet1).subscribe(pet -> log.info("Created sample pet: {}", pet.getName()));
                petService.createPet(pet2).subscribe(pet -> log.info("Created sample pet: {}", pet.getName()));
                petService.createPet(pet3).subscribe(pet -> log.info("Created sample pet: {}", pet.getName()));

                log.info("Sample data initialization completed");
        }
}
