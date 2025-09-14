package com.coding2.the.max.petstore.catalog.service;

import com.coding2.the.max.petstore.catalog.dto.BreedsResponse;
import com.coding2.the.max.petstore.catalog.dto.SpeciesResponse;
import com.coding2.the.max.petstore.catalog.model.Pet;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class CatalogPostGresServiceTest {

  @Container
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
      .withDatabaseName("testdb")
      .withUsername("test")
      .withPassword("test")
      .withReuse(false);

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.r2dbc.url", () -> "r2dbc:postgresql://"
        + postgres.getHost() + ":" + postgres.getFirstMappedPort()
        + "/" + postgres.getDatabaseName());
    registry.add("spring.r2dbc.username", postgres::getUsername);
    registry.add("spring.r2dbc.password", postgres::getPassword);
  }

  @Autowired
  private CatalogPostGresService catalogService;
  @Autowired
  private DatabaseClient databaseClient;

  @AfterAll
  static void tearDown() {
    if (postgres != null && postgres.isRunning()) {
      postgres.close();
    }
  }

  @BeforeEach
  void setUp() {
    // Create the database schema and clear data before each test
    initializeDatabase();
    clearDatabase();
  }

  private void initializeDatabase() {
    // Create ENUMs first - matching the original schema
    databaseClient.sql("""
        DO $$ BEGIN
            CREATE TYPE species_enum AS ENUM ('dog', 'cat', 'bird', 'fish', 'reptile', 'small-mammal');
        EXCEPTION
            WHEN duplicate_object THEN null;
        END $$;
        """).fetch().rowsUpdated().block();

    databaseClient.sql("""
        DO $$ BEGIN
            CREATE TYPE size_enum AS ENUM ('small', 'medium', 'large', 'extra-large');
        EXCEPTION
            WHEN duplicate_object THEN null;
        END $$;
        """).fetch().rowsUpdated().block();

    // Create the breeds table
    databaseClient.sql("""
        CREATE TABLE IF NOT EXISTS breeds (
            id BIGSERIAL PRIMARY KEY,
            name VARCHAR(255) NOT NULL,
            species species_enum NOT NULL,
            typical_size size_enum,
            characteristics TEXT[],
            created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
        )
        """).fetch().rowsUpdated().block();
  }

  private void clearDatabase() {
    // Clear data from breeds table
    databaseClient.sql("DELETE FROM breeds").fetch().rowsUpdated().block();
  }

  @Test
  void testGetBreedsWithoutSpeciesFilter() {
    // Given - Create test data directly in database
    insertTestBreed("Golden Retriever", "dog", "large", new String[] { "friendly", "intelligent", "active" });
    insertTestBreed("Persian", "cat", "medium", new String[] { "calm", "affectionate", "quiet" });
    insertTestBreed("Labrador", "dog", "large", new String[] { "loyal", "outgoing", "active" });

    // When - Get all breeds without species filter
    StepVerifier.create(catalogService.getBreeds(null))
        .assertNext(response -> {
          assertThat(response).isNotNull();
          assertThat(response.getBreeds()).hasSize(3);

          // Verify breed names are present
          List<String> breedNames = response.getBreeds().stream()
              .map(BreedsResponse.Breed::getName)
              .toList();
          assertThat(breedNames).containsExactlyInAnyOrder(
              "Golden Retriever", "Persian", "Labrador");

          // Verify species conversion (enum to lowercase with dashes)
          List<String> speciesNames = response.getBreeds().stream()
              .map(BreedsResponse.Breed::getSpecies)
              .distinct()
              .toList();
          assertThat(speciesNames).containsExactlyInAnyOrder("dog", "cat");
        })
        .verifyComplete();
  }

  @Test
  void testGetBreedsWithSpeciesFilter() {
    // Given - Create test data directly in database
    insertTestBreed("Golden Retriever", "dog", "large", new String[] { "friendly", "intelligent" });
    insertTestBreed("Beagle", "dog", "medium", new String[] { "curious", "friendly" });
    insertTestBreed("Persian", "cat", "medium", new String[] { "calm", "affectionate" });

    // When - Get breeds filtered by DOG species
    StepVerifier.create(catalogService.getBreeds(Pet.Species.DOG))
        .assertNext(response -> {
          assertThat(response).isNotNull();
          assertThat(response.getBreeds()).hasSize(2);

          // Verify only dog breeds are returned
          List<String> breedNames = response.getBreeds().stream()
              .map(BreedsResponse.Breed::getName)
              .toList();
          assertThat(breedNames).containsExactlyInAnyOrder(
              "Golden Retriever", "Beagle");

          // Verify all returned breeds have species "dog"
          response.getBreeds().forEach(breed -> assertThat(breed.getSpecies()).isEqualTo("dog"));
        })
        .verifyComplete();
  }

  @Test
  void testGetBreedsWithEmptyDatabase() {
    // When - Get breeds from empty database
    StepVerifier.create(catalogService.getBreeds(null))
        .assertNext(response -> {
          assertThat(response).isNotNull();
          assertThat(response.getBreeds()).isEmpty();
        })
        .verifyComplete();
  }

  @Test
  void testGetBreedsWithSpeciesNotFound() {
    // Given - Create test data with only DOG breeds
    insertTestBreed("Golden Retriever", "dog", "large", new String[] { "friendly" });

    // When - Filter by BIRD species (not present in data)
    StepVerifier.create(catalogService.getBreeds(Pet.Species.BIRD))
        .assertNext(response -> {
          assertThat(response).isNotNull();
          assertThat(response.getBreeds()).isEmpty();
        })
        .verifyComplete();
  }

  @Test
  void testGetSpecies() {
    // Given - Create test data with multiple species
    insertTestBreed("Golden Retriever", "dog", "large", new String[] { "friendly" });
    insertTestBreed("Beagle", "dog", "medium", new String[] { "curious" });
    insertTestBreed("Persian", "cat", "medium", new String[] { "calm" });
    insertTestBreed("Canary", "bird", "small", new String[] { "vocal" });

    // When - Get species summary
    StepVerifier.create(catalogService.getSpecies())
        .assertNext(response -> {
          assertThat(response).isNotNull();
          assertThat(response.getSpecies()).hasSize(3);

          // Verify species are present with correct counts
          List<SpeciesResponse.Species> species = response.getSpecies();

          // Find dog species
          SpeciesResponse.Species dogSpecies = species.stream()
              .filter(s -> s.getName().equals("dog"))
              .findFirst()
              .orElseThrow();
          assertThat(dogSpecies.getDisplayName()).isEqualTo("Dogs");
          assertThat(dogSpecies.getBreedCount()).isEqualTo(2);

          // Find cat species
          SpeciesResponse.Species catSpecies = species.stream()
              .filter(s -> s.getName().equals("cat"))
              .findFirst()
              .orElseThrow();
          assertThat(catSpecies.getDisplayName()).isEqualTo("Cats");
          assertThat(catSpecies.getBreedCount()).isEqualTo(1);

          // Find bird species
          SpeciesResponse.Species birdSpecies = species.stream()
              .filter(s -> s.getName().equals("bird"))
              .findFirst()
              .orElseThrow();
          assertThat(birdSpecies.getDisplayName()).isEqualTo("Birds");
          assertThat(birdSpecies.getBreedCount()).isEqualTo(1);
        })
        .verifyComplete();
  }

  @Test
  void testGetSpeciesWithEmptyDatabase() {
    // When - Get species from empty database
    StepVerifier.create(catalogService.getSpecies())
        .assertNext(response -> {
          assertThat(response).isNotNull();
          assertThat(response.getSpecies()).isEmpty();
        })
        .verifyComplete();
  }

  @Test
  void testGetSpeciesWithSingleBreedPerSpecies() {
    // Given - Create test data with one breed per species
    insertTestBreed("Golden Retriever", "dog", "large", new String[] { "friendly" });
    insertTestBreed("Persian", "cat", "medium", new String[] { "calm" });
    insertTestBreed("Goldfish", "fish", "small", new String[] { "peaceful" });
    insertTestBreed("Iguana", "reptile", "medium", new String[] { "docile" });
    insertTestBreed("Hamster", "small-mammal", "small", new String[] { "active" });

    // When - Get species summary
    StepVerifier.create(catalogService.getSpecies())
        .assertNext(response -> {
          assertThat(response).isNotNull();
          assertThat(response.getSpecies()).hasSize(5);

          // Verify all species have count of 1
          response.getSpecies().forEach(species -> assertThat(species.getBreedCount()).isEqualTo(1));

          // Verify display names are correct
          List<String> displayNames = response.getSpecies().stream()
              .map(SpeciesResponse.Species::getDisplayName)
              .toList();
          assertThat(displayNames).containsExactlyInAnyOrder(
              "Dogs", "Cats", "Fish", "Reptiles", "Small Mammals");
        })
        .verifyComplete();
  }

  @Test
  void testGetBreedsVerifyCharacteristicsAndSize() {
    // Given - Create test data with specific characteristics and sizes
    insertTestBreed("Chihuahua", "dog", "small", new String[] { "energetic", "alert", "loyal" });
    insertTestBreed("Great Dane", "dog", "extra-large", new String[] { "gentle", "friendly", "patient" });

    // When - Get all breeds
    StepVerifier.create(catalogService.getBreeds(null))
        .assertNext(response -> {
          assertThat(response).isNotNull();
          assertThat(response.getBreeds()).hasSize(2);

          // Find Chihuahua
          BreedsResponse.Breed chihuahua = response.getBreeds().stream()
              .filter(breed -> breed.getName().equals("Chihuahua"))
              .findFirst()
              .orElseThrow();
          assertThat(chihuahua.getTypicalSize()).isEqualTo(Pet.Size.SMALL);
          assertThat(chihuahua.getCharacteristics())
              .containsExactlyInAnyOrder("energetic", "alert", "loyal");

          // Find Great Dane
          BreedsResponse.Breed greatDane = response.getBreeds().stream()
              .filter(breed -> breed.getName().equals("Great Dane"))
              .findFirst()
              .orElseThrow();
          assertThat(greatDane.getTypicalSize()).isEqualTo(Pet.Size.EXTRA_LARGE);
          assertThat(greatDane.getCharacteristics())
              .containsExactlyInAnyOrder("gentle", "friendly", "patient");
        })
        .verifyComplete();
  }

  private void insertTestBreed(String name, String species, String size, String[] characteristics) {
    // Use lowercase values directly as defined in PostgreSQL enum
    String speciesValue = species.toLowerCase();
    String sizeValue = size.toLowerCase();

    databaseClient.sql("""
        INSERT INTO breeds (name, species, typical_size, characteristics)
        VALUES ($1, $2::species_enum, $3::size_enum, $4)
        """)
        .bind("$1", name)
        .bind("$2", speciesValue) // Use lowercase value
        .bind("$3", sizeValue) // Use lowercase value
        .bind("$4", characteristics)
        .fetch()
        .rowsUpdated()
        .block();
  }
}
