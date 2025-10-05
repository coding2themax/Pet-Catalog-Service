package com.coding2.the.max.petstore.catalog.service;

import com.coding2.the.max.petstore.catalog.domain.entity.PetEntity;
import com.coding2.the.max.petstore.catalog.domain.repository.PetRepository;
import com.coding2.the.max.petstore.catalog.domain.service.PetService;
import com.coding2.the.max.petstore.catalog.dto.UpdatePetRequest;
import com.coding2.the.max.petstore.catalog.openapi.model.NewPet;
import com.coding2.the.max.petstore.catalog.openapi.model.Pet;

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

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class PetServiceImplTest {

  @Container
  @SuppressWarnings("resource")
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
      .withDatabaseName("testdb")
      .withUsername("test")
      .withPassword("test")
      .withReuse(false);

  static {
    postgres.start();
  }

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.r2dbc.url", () -> "r2dbc:postgresql://"
        + postgres.getHost() + ":" + postgres.getFirstMappedPort()
        + "/" + postgres.getDatabaseName());
    registry.add("spring.r2dbc.username", postgres::getUsername);
    registry.add("spring.r2dbc.password", postgres::getPassword);
  }

  @Autowired
  private PetService petService;
  @Autowired
  private DatabaseClient databaseClient;
  @Autowired
  private PetRepository petRepository;

  @AfterAll
  static void tearDown() {
    try {
      if (postgres != null && postgres.isRunning()) {
        postgres.close();
      }
    } catch (Exception e) {
      // Log the exception if needed, but don't fail the test cleanup
    }
  }

  @BeforeEach
  void setUp() {
    // Create the database schema and clear data before each test
    initializeDatabase();
    clearDatabase();
  }

  private void initializeDatabase() {
    // Create ENUMs first
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

    databaseClient.sql("""
        DO $$ BEGIN
            CREATE TYPE gender_enum AS ENUM ('male', 'female');
        EXCEPTION
            WHEN duplicate_object THEN null;
        END $$;
        """).fetch().rowsUpdated().block();

    databaseClient.sql("""
        DO $$ BEGIN
            CREATE TYPE availability_enum AS ENUM ('available', 'reserved', 'sold', 'coming-soon');
        EXCEPTION
            WHEN duplicate_object THEN null;
        END $$;
        """).fetch().rowsUpdated().block();

    // Create the locations table
    databaseClient.sql("""
        CREATE TABLE IF NOT EXISTS locations (
            id BIGSERIAL PRIMARY KEY,
            store_id VARCHAR(255) NOT NULL UNIQUE,
            store_name VARCHAR(255) NOT NULL,
            city VARCHAR(255) NOT NULL,
            state VARCHAR(255) NOT NULL,
            zip_code VARCHAR(20) NOT NULL,
            created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
            updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
        )
        """).fetch().rowsUpdated().block();

    // Create the health_info table
    databaseClient.sql("""
        CREATE TABLE IF NOT EXISTS health_info (
            id BIGSERIAL PRIMARY KEY,
            vaccinated BOOLEAN DEFAULT FALSE,
            spayed_neutered BOOLEAN DEFAULT FALSE,
            health_certificate BOOLEAN DEFAULT FALSE,
            last_vet_visit DATE,
            created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
            updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
        )
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

    // Create the pets table
    databaseClient.sql("""
        CREATE TABLE IF NOT EXISTS pets (
            id VARCHAR(255) PRIMARY KEY,
            name VARCHAR(255) NOT NULL,
            age INTEGER CHECK (age >= 0),
            size size_enum,
            gender gender_enum,
            price DECIMAL(10, 2) CHECK (price >= 0),
            description TEXT,
            availability availability_enum NOT NULL DEFAULT 'available',
            location_id BIGINT REFERENCES locations(id) ON DELETE SET NULL,
            health_info_id BIGINT REFERENCES health_info(id) ON DELETE SET NULL,
            breed_id BIGINT REFERENCES breeds(id) ON DELETE SET NULL,
            created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
            updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
        )
        """).fetch().rowsUpdated().block();

    // Create the pets_with_details view for complex queries that matches the actual
    // repository queries
    databaseClient.sql("""
        CREATE OR REPLACE VIEW pets_with_details AS
        SELECT
            p.*,
            b.name as breed_name,
            b.species,
            CASE
                WHEN p.age BETWEEN 0 AND 12 THEN 'puppy'
                WHEN p.age BETWEEN 13 AND 24 THEN 'young'
                WHEN p.age BETWEEN 25 AND 84 THEN 'adult'
                ELSE 'senior'
            END as age_category,
            h.vaccinated,
            h.spayed_neutered,
            h.health_certificate,
            l.store_name,
            l.city,
            l.state
        FROM pets p
        LEFT JOIN breeds b ON b.id = p.breed_id
        LEFT JOIN health_info h ON h.id = p.health_info_id
        LEFT JOIN locations l ON l.id = p.location_id
        """).fetch().rowsUpdated().block();

    // Create the complete details view used by PetCompleteDetailsRepository
    databaseClient.sql("""
        CREATE OR REPLACE VIEW pets_complete_details AS
        SELECT
            p.id,
            p.name,
            p.age,
            p.size,
            p.gender,
            p.price,
            p.description,
            p.availability,
            CASE WHEN p.availability = 'available' THEN true ELSE false END as is_available,
            b.name as breed_name,
            b.species,
            CASE
                WHEN p.age BETWEEN 0 AND 12 THEN 'puppy'
                WHEN p.age BETWEEN 13 AND 24 THEN 'young'
                WHEN p.age BETWEEN 25 AND 84 THEN 'adult'
                ELSE 'senior'
            END as age_category,
            h.vaccinated,
            h.spayed_neutered,
            h.health_certificate,
            l.store_name,
            l.city,
            l.state,
            p.created_at,
            p.updated_at
        FROM pets p
        LEFT JOIN breeds b ON b.id = p.breed_id
        LEFT JOIN health_info h ON h.id = p.health_info_id
        LEFT JOIN locations l ON l.id = p.location_id
        """).fetch().rowsUpdated().block();
  }

  private void clearDatabase() {
    // Clear data from tables in correct order (considering foreign keys)
    databaseClient.sql("DELETE FROM pets").fetch().rowsUpdated().block();
    databaseClient.sql("DELETE FROM health_info").fetch().rowsUpdated().block();
    databaseClient.sql("DELETE FROM locations").fetch().rowsUpdated().block();
    databaseClient.sql("DELETE FROM breeds").fetch().rowsUpdated().block();
  }

  @Test
  void testCreatePetSimple() {
    // Given - Setup test data without complex relationships
    NewPet newPet = new NewPet();
    newPet.setName("Buddy");
    newPet.setAge(BigDecimal.valueOf(24));
    newPet.setSize(NewPet.SizeEnum.LARGE);
    newPet.setGender(NewPet.GenderEnum.MALE);
    newPet.setPrice(BigDecimal.valueOf(500.00));
    newPet.setDescription("Friendly golden retriever");
    newPet.setIsAvailable(true);
    newPet.setCharacteristics(List.of("friendly", "energetic"));

    // When - Create pet
    StepVerifier.create(petService.createPet(newPet))
        .assertNext(createdPet -> {
          assertThat(createdPet).isNotNull();
          assertThat(createdPet.getName()).isEqualTo("Buddy");
          assertThat(createdPet.getAge()).isEqualTo(BigDecimal.valueOf(24));
          assertThat(createdPet.getSize()).isEqualTo(Pet.SizeEnum.LARGE);
          assertThat(createdPet.getGender()).isEqualTo(Pet.GenderEnum.MALE);
          assertThat(createdPet.getPrice()).isEqualTo(BigDecimal.valueOf(500.00));
          assertThat(createdPet.getDescription()).isEqualTo("Friendly golden retriever");
          assertThat(createdPet.getId()).isNotNull();
        })
        .verifyComplete();
  }

  @Test
  void testBasicRepositoryConnection() {
    // Test that we can at least connect to the repository
    StepVerifier.create(petRepository.count())
        .expectNext(0L)
        .verifyComplete();
  }

  @Test
  void testCreatePetDirect() {
    // Test creating a pet directly via repository
    PetEntity pet = PetEntity.builder()
        .id(UUID.randomUUID().toString())
        .name("TestPet")
        .age(2)
        .size(PetEntity.Size.MEDIUM)
        .gender(PetEntity.Gender.MALE)
        .price(BigDecimal.valueOf(100.0))
        .description("Test pet")
        .availability(PetEntity.Availability.AVAILABLE)
        .createdAt(Instant.now())
        .updatedAt(Instant.now())
        .build();

    StepVerifier.create(petRepository.save(pet))
        .assertNext(savedPet -> {
          assertThat(savedPet).isNotNull();
          assertThat(savedPet.getName()).isEqualTo("TestPet");
        })
        .verifyComplete();
  }

  @Test
  void testDeletePetNotFound() {
    // When - Delete non-existent pet
    StepVerifier.create(petService.deletePet("non-existent-id"))
        .expectError(RuntimeException.class)
        .verify();
  }

  @Test
  void testUpdatePetNotFound() {
    // Given - Non-existent pet ID
    UpdatePetRequest updateRequest = UpdatePetRequest.builder()
        .name("Updated Name")
        .build();

    // When - Update non-existent pet
    StepVerifier.create(petService.updatePet("non-existent-id", updateRequest))
        .expectError(RuntimeException.class)
        .verify();
  }
}