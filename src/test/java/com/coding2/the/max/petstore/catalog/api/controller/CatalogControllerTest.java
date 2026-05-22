package com.coding2.the.max.petstore.catalog.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.coding2.the.max.petstore.catalog.domain.entity.PetEntity;
import com.coding2.the.max.petstore.catalog.domain.service.CatalogService;
import com.coding2.the.max.petstore.catalog.dto.BreedsResponse;
import com.coding2.the.max.petstore.catalog.dto.SpeciesResponse;
import com.coding2.the.max.petstore.catalog.exception.GlobalExceptionHandler;

import reactor.core.publisher.Mono;

@WebFluxTest(controllers = CatalogController.class, includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = GlobalExceptionHandler.class))
class CatalogControllerTest {

  @Autowired
  private WebTestClient webTestClient;

  @MockitoBean
  private CatalogService catalogService;

  @Test
  void testGetBreedsWithoutFilter() {
    // Given
    BreedsResponse.Breed breed1 = BreedsResponse.Breed.builder()
        .name("Golden Retriever")
        .species("dog")
        .typicalSize(PetEntity.Size.LARGE)
        .characteristics(Arrays.asList("friendly", "intelligent", "active"))
        .build();

    BreedsResponse.Breed breed2 = BreedsResponse.Breed.builder()
        .name("Persian")
        .species("cat")
        .typicalSize(PetEntity.Size.MEDIUM)
        .characteristics(Arrays.asList("quiet", "docile", "sweet"))
        .build();

    BreedsResponse mockResponse = BreedsResponse.builder()
        .breeds(Arrays.asList(breed1, breed2))
        .build();

    when(catalogService.getBreeds(null))
        .thenReturn(Mono.just(mockResponse));

    // When & Then
    webTestClient.get()
        .uri("/catalog/v1/breeds")
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.breeds").isArray()
        .jsonPath("$.breeds.length()").isEqualTo(2)
        .jsonPath("$.breeds[0].name").isEqualTo("Golden Retriever")
        .jsonPath("$.breeds[0].species").isEqualTo("dog")
        .jsonPath("$.breeds[0].typical_size").isEqualTo("large")
        .jsonPath("$.breeds[0].characteristics[0]").isEqualTo("friendly")
        .jsonPath("$.breeds[1].name").isEqualTo("Persian")
        .jsonPath("$.breeds[1].species").isEqualTo("cat")
        .jsonPath("$.breeds[1].typical_size").isEqualTo("medium");
  }

  @Test
  void testGetBreedsWithSpeciesFilter() {
    // Given
    BreedsResponse.Breed dogBreed = BreedsResponse.Breed.builder()
        .name("Labrador Retriever")
        .species("dog")
        .typicalSize(PetEntity.Size.LARGE)
        .characteristics(Arrays.asList("friendly", "outgoing", "active"))
        .build();

    BreedsResponse mockResponse = BreedsResponse.builder()
        .breeds(Arrays.asList(dogBreed))
        .build();

    when(catalogService.getBreeds(PetEntity.Species.DOG))
        .thenReturn(Mono.just(mockResponse));

    // When & Then
    webTestClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/catalog/v1/breeds")
            .queryParam("species", "DOG")
            .build())
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.breeds").isArray()
        .jsonPath("$.breeds.length()").isEqualTo(1)
        .jsonPath("$.breeds[0].name").isEqualTo("Labrador Retriever")
        .jsonPath("$.breeds[0].species").isEqualTo("dog")
        .jsonPath("$.breeds[0].typical_size").isEqualTo("large")
        .jsonPath("$.breeds[0].characteristics[0]").isEqualTo("friendly");
  }

  @Test
  void testGetSpecies() {
    // Given
    SpeciesResponse.Species dogSpecies = SpeciesResponse.Species.builder()
        .name("dog")
        .displayName("Dogs")
        .breedCount(5)
        .build();

    SpeciesResponse.Species catSpecies = SpeciesResponse.Species.builder()
        .name("cat")
        .displayName("Cats")
        .breedCount(3)
        .build();

    SpeciesResponse mockResponse = SpeciesResponse.builder()
        .species(Arrays.asList(dogSpecies, catSpecies))
        .build();

    when(catalogService.getSpecies())
        .thenReturn(Mono.just(mockResponse));

    // When & Then
    webTestClient.get()
        .uri("/catalog/v1/species")
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.species").isArray()
        .jsonPath("$.species.length()").isEqualTo(2)
        .jsonPath("$.species[0].name").isEqualTo("dog")
        .jsonPath("$.species[0].display_name").isEqualTo("Dogs")
        .jsonPath("$.species[0].breed_count").isEqualTo(5)
        .jsonPath("$.species[1].name").isEqualTo("cat")
        .jsonPath("$.species[1].display_name").isEqualTo("Cats")
        .jsonPath("$.species[1].breed_count").isEqualTo(3);
  }

  @Test
  void testGetBreedsEmptyResponse() {
    // Given
    BreedsResponse emptyResponse = BreedsResponse.builder()
        .breeds(Arrays.asList())
        .build();

    when(catalogService.getBreeds(any()))
        .thenReturn(Mono.just(emptyResponse));

    // When & Then
    webTestClient.get()
        .uri("/catalog/v1/breeds")
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.breeds").isArray()
        .jsonPath("$.breeds.length()").isEqualTo(0);
  }

  @Test
  void testGetSpeciesEmptyResponse() {
    // Given
    SpeciesResponse emptyResponse = SpeciesResponse.builder()
        .species(Arrays.asList())
        .build();

    when(catalogService.getSpecies())
        .thenReturn(Mono.just(emptyResponse));

    // When & Then
    webTestClient.get()
        .uri("/catalog/v1/species")
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.species").isArray()
        .jsonPath("$.species.length()").isEqualTo(0);
  }
}