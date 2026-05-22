package com.coding2.the.max.petstore.catalog.service;

import com.coding2.the.max.petstore.catalog.domain.entity.PetEntity;
import com.coding2.the.max.petstore.catalog.domain.repository.PetRepository;
import com.coding2.the.max.petstore.catalog.domain.repository.PetCompleteDetailsRepository;
import com.coding2.the.max.petstore.catalog.domain.service.PetMapper;
import com.coding2.the.max.petstore.catalog.domain.service.PetCompleteDetailsMapper;
import com.coding2.the.max.petstore.catalog.domain.service.PetServiceImpl;
import com.coding2.the.max.petstore.catalog.openapi.model.NewPet;
import com.coding2.the.max.petstore.catalog.openapi.model.Pet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PetServiceImplMockTest {

  @Mock
  private PetRepository petRepository;

  @Mock
  private PetMapper petMapper;

  @Mock
  private PetCompleteDetailsMapper petCompleteDetailsMapper;

  @Mock
  private PetCompleteDetailsRepository petCompleteDetailsRepository;

  @InjectMocks
  private PetServiceImpl petService;

  @Test
  void testCreatePetWithMocks() {
    // Given
    NewPet newPet = new NewPet();
    newPet.setName("TestPet");
    newPet.setAge(BigDecimal.valueOf(2));
    newPet.setSize(NewPet.SizeEnum.MEDIUM);
    newPet.setGender(NewPet.GenderEnum.MALE);
    newPet.setPrice(BigDecimal.valueOf(100.0));
    newPet.setDescription("Test pet");

    PetEntity savedPet = PetEntity.builder()
        .id("test-id")
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

    Pet mappedPet = new Pet();
    mappedPet.setId("test-id");
    mappedPet.setName("TestPet");
    mappedPet.setSize(Pet.SizeEnum.MEDIUM);
    mappedPet.setGender(Pet.GenderEnum.MALE);

    when(petRepository.save(any(PetEntity.class))).thenReturn(Mono.just(savedPet));
    when(petMapper.toApiModel(any(PetEntity.class))).thenReturn(mappedPet);

    // When & Then
    StepVerifier.create(petService.createPet(newPet))
        .assertNext(result -> {
          assertThat(result).isNotNull();
          assertThat(result.getId()).isEqualTo("test-id");
          assertThat(result.getName()).isEqualTo("TestPet");
          assertThat(result.getSize()).isEqualTo(Pet.SizeEnum.MEDIUM);
          assertThat(result.getGender()).isEqualTo(Pet.GenderEnum.MALE);
        })
        .verifyComplete();
  }
}