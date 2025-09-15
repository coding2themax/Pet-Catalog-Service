package com.coding2.the.max.petstore.catalog.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.coding2.the.max.petstore.catalog.api.controller.PetController;
import com.coding2.the.max.petstore.catalog.domain.service.PetService;
import com.coding2.the.max.petstore.catalog.exception.GlobalExceptionHandler;
import com.coding2.the.max.petstore.catalog.exception.PetNotFoundException;
import com.coding2.the.max.petstore.catalog.openapi.model.Pet;
import com.coding2.the.max.petstore.catalog.openapi.model.NewPet;

import reactor.core.publisher.Mono;

@WebFluxTest(controllers = PetController.class, includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = GlobalExceptionHandler.class))
class PetControllerTest {

        @Autowired
        private WebTestClient webTestClient;

        @MockitoBean
        private PetService petService;

        @Test
        void testCreatePet() {
                // Given
                NewPet request = new NewPet();
                request.setName("Buddy");
                request.setSpecies("dog");
                request.setBreed("Golden Retriever");
                request.setAge(BigDecimal.valueOf(24));
                request.setSize(NewPet.SizeEnum.LARGE);
                request.setGender(NewPet.GenderEnum.MALE);
                request.setPrice(BigDecimal.valueOf(1200.0));
                request.setDescription("Friendly and energetic Golden Retriever");
                request.setCharacteristics(Arrays.asList("friendly", "energetic"));
                request.setIsAvailable(true);
                request.setVaccinated(true);
                request.setSpayedNeutered(false);
                request.setImageUrl("https://images.petstore.com/pets/buddy-1.jpg");
                request.setGoodWithKids(true);
                request.setGoodWithPets(true);
                request.setEnergyLevel(NewPet.EnergyLevelEnum.MEDIUM);
                request.setHealthStatus(NewPet.HealthStatusEnum.GOOD);
                request.setId("123e4567-e89b-12d3-a456-426614174000");

                Pet mockPet = new Pet();
                mockPet.setId("123e4567-e89b-12d3-a456-426614174000");
                mockPet.setName("Buddy");
                mockPet.setSpecies("dog");
                mockPet.setBreed("Golden Retriever");
                mockPet.setAge(BigDecimal.valueOf(24));
                mockPet.setSize(Pet.SizeEnum.LARGE);
                mockPet.setGender(Pet.GenderEnum.MALE);
                mockPet.setPrice(BigDecimal.valueOf(1200.0));
                mockPet.setDescription("Friendly and energetic Golden Retriever");
                mockPet.setCharacteristics(Arrays.asList("friendly", "energetic"));
                mockPet.setIsAvailable(true);
                mockPet.setVaccinated(true);
                mockPet.setSpayedNeutered(false);

                when(petService.createPet(any(NewPet.class)))
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
                                .jsonPath("$.breed").isEqualTo("Golden Retriever")
                                .jsonPath("$.age").isEqualTo(24)
                                .jsonPath("$.size").isEqualTo("Large")
                                .jsonPath("$.gender").isEqualTo("Male")
                                .jsonPath("$.price").isEqualTo(1200.0)
                                .jsonPath("$.description").isEqualTo("Friendly and energetic Golden Retriever")
                                .jsonPath("$.isAvailable").isEqualTo(true)
                                .jsonPath("$.vaccinated").isEqualTo(true)
                                .jsonPath("$.spayedNeutered").isEqualTo(false);
        }

        @Test
        void testGetPetById() {
                // Given
                String petId = "123e4567-e89b-12d3-a456-426614174000";
                Pet mockPet = new Pet();
                mockPet.setId(petId);
                mockPet.setName("Buddy");
                mockPet.setSpecies("dog");
                mockPet.setBreed("Golden Retriever");
                mockPet.setAge(BigDecimal.valueOf(24));
                mockPet.setSize(Pet.SizeEnum.LARGE);
                mockPet.setGender(Pet.GenderEnum.MALE);
                mockPet.setPrice(BigDecimal.valueOf(1200.0));
                mockPet.setIsAvailable(true);

                when(petService.getPetById(petId))
                                .thenReturn(Mono.just(mockPet));

                // When & Then
                webTestClient.get()
                                .uri("/catalog/v1/pets/" + petId)
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody()
                                .jsonPath("$.id").isEqualTo(petId)
                                .jsonPath("$.name").isEqualTo("Buddy")
                                .jsonPath("$.species").isEqualTo("dog")
                                .jsonPath("$.breed").isEqualTo("Golden Retriever")
                                .jsonPath("$.isAvailable").isEqualTo(true);
        }

        @Test
        void testGetPetByIdNotFound() {
                // Given
                String petId = "non-existent-id";
                when(petService.getPetById(petId))
                                .thenReturn(Mono.error(new PetNotFoundException(petId)));

                // When & Then
                webTestClient.get()
                                .uri("/catalog/v1/pets/" + petId)
                                .exchange()
                                .expectStatus().isNotFound();
        }
}
