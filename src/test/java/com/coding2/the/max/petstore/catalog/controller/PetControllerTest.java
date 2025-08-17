package com.coding2.the.max.petstore.catalog.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.coding2.the.max.petstore.catalog.dto.CreatePetRequest;
import com.coding2.the.max.petstore.catalog.model.AgeCategory;
import com.coding2.the.max.petstore.catalog.model.HealthInfo;
import com.coding2.the.max.petstore.catalog.model.Location;
import com.coding2.the.max.petstore.catalog.model.Pet;
import com.coding2.the.max.petstore.catalog.model.PetImage;
import com.coding2.the.max.petstore.catalog.service.PetService;

import reactor.core.publisher.Mono;

@WebFluxTest(PetController.class)
class PetControllerTest {

        @Autowired
        private WebTestClient webTestClient;

        @MockitoBean
        private PetService petService;

        @Test
        void testCreatePet() {
                // Given
                CreatePetRequest request = CreatePetRequest.builder()
                                .name("Buddy")
                                .species(Pet.Species.DOG)
                                .breed("Golden Retriever")
                                .age(24)
                                .size(Pet.Size.LARGE)
                                .gender(Pet.Gender.MALE)
                                .price(1200.0)
                                .description("Friendly and energetic Golden Retriever")
                                .characteristics(Arrays.asList("friendly", "energetic"))
                                .healthInfo(HealthInfo.builder()
                                                .vaccinated(true)
                                                .spayedNeutered(false)
                                                .healthCertificate(true)
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

                Pet mockPet = Pet.builder()
                                .id("123e4567-e89b-12d3-a456-426614174000")
                                .name("Buddy")
                                .species(Pet.Species.DOG)
                                .breedName("Golden Retriever")
                                .age(24)
                                .ageCategory(AgeCategory.YOUNG)
                                .size(Pet.Size.LARGE)
                                .gender(Pet.Gender.MALE)
                                .price(BigDecimal.valueOf(1200.0))
                                .description("Friendly and energetic Golden Retriever")
                                .characteristics(Arrays.asList("friendly", "energetic"))
                                .availability(Pet.Availability.AVAILABLE)
                                .build();

                when(petService.createPet(any(CreatePetRequest.class)))
                                .thenReturn(Mono.just(mockPet));

                // When & Then
                webTestClient.post()
                                .uri("/catalog/v1/pets")
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(request)
                                .exchange()
                                .expectStatus().isCreated()
                                .expectBody()
                                .jsonPath("$.id").isEqualTo("123e4567-e89b-12d3-a456-426614174000")
                                .jsonPath("$.name").isEqualTo("Buddy")
                                .jsonPath("$.species").isEqualTo("dog")
                                .jsonPath("$.breed_name").isEqualTo("Golden Retriever")
                                .jsonPath("$.price").isEqualTo(1200.0);
        }

        @Test
        void testGetPetById() {
                // Given
                String petId = "123e4567-e89b-12d3-a456-426614174000";
                Pet mockPet = Pet.builder()
                                .id(petId)
                                .name("Buddy")
                                .species(Pet.Species.DOG)
                                .breedName("Golden Retriever")
                                .availability(Pet.Availability.AVAILABLE)
                                .build();

                when(petService.getPetById(petId))
                                .thenReturn(Mono.just(mockPet));

                // When & Then
                webTestClient.get()
                                .uri("/catalog/v1/pets/" + petId)
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody()
                                .jsonPath("$.id").isEqualTo(petId)
                                .jsonPath("$.name").isEqualTo("Buddy");
        }
}
