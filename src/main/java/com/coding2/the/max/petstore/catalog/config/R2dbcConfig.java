package com.coding2.the.max.petstore.catalog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.PostgresDialect;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.lang.NonNull;

import com.coding2.the.max.petstore.catalog.domain.entity.PetEntity;

@Configuration
@EnableR2dbcRepositories(basePackages = "com.coding2.the.max.petstore.catalog.repository")
public class R2dbcConfig {

  @Bean
  public R2dbcCustomConversions r2dbcCustomConversions() {
    return R2dbcCustomConversions.of(PostgresDialect.INSTANCE,
        new SpeciesReadingConverter(),
        new SpeciesWritingConverter(),
        new SizeReadingConverter(),
        new SizeWritingConverter(),
        new GenderReadingConverter(),
        new GenderWritingConverter(),
        new AvailabilityReadingConverter(),
        new AvailabilityWritingConverter());
  }

  @ReadingConverter
  static class SpeciesReadingConverter implements Converter<String, PetEntity.Species> {
    @Override
    public PetEntity.Species convert(@NonNull String source) {
      return switch (source.toLowerCase()) {
        case "dog" -> PetEntity.Species.DOG;
        case "cat" -> PetEntity.Species.CAT;
        case "bird" -> PetEntity.Species.BIRD;
        case "fish" -> PetEntity.Species.FISH;
        case "reptile" -> PetEntity.Species.REPTILE;
        case "small-mammal" -> PetEntity.Species.SMALL_MAMMAL;
        default -> throw new IllegalArgumentException("Unknown species: " + source);
      };
    }
  }

  @WritingConverter
  static class SpeciesWritingConverter implements Converter<PetEntity.Species, String> {
    @Override
    public String convert(@NonNull PetEntity.Species source) {
      return switch (source) {
        case DOG -> "dog";
        case CAT -> "cat";
        case BIRD -> "bird";
        case FISH -> "fish";
        case REPTILE -> "reptile";
        case SMALL_MAMMAL -> "small-mammal";
      };
    }
  }

  @ReadingConverter
  static class SizeReadingConverter implements Converter<String, PetEntity.Size> {
    @Override
    public PetEntity.Size convert(@NonNull String source) {
      return switch (source.toLowerCase()) {
        case "small" -> PetEntity.Size.SMALL;
        case "medium" -> PetEntity.Size.MEDIUM;
        case "large" -> PetEntity.Size.LARGE;
        case "extra-large" -> PetEntity.Size.EXTRA_LARGE;
        default -> throw new IllegalArgumentException("Unknown size: " + source);
      };
    }
  }

  @WritingConverter
  static class SizeWritingConverter implements Converter<PetEntity.Size, String> {
    @Override
    public String convert(@NonNull PetEntity.Size source) {
      return switch (source) {
        case SMALL -> "small";
        case MEDIUM -> "medium";
        case LARGE -> "large";
        case EXTRA_LARGE -> "extra-large";
      };
    }
  }

  @ReadingConverter
  static class GenderReadingConverter implements Converter<String, PetEntity.Gender> {
    @Override
    public PetEntity.Gender convert(@NonNull String source) {
      return switch (source.toLowerCase()) {
        case "male" -> PetEntity.Gender.MALE;
        case "female" -> PetEntity.Gender.FEMALE;
        default -> throw new IllegalArgumentException("Unknown gender: " + source);
      };
    }
  }

  @WritingConverter
  static class GenderWritingConverter implements Converter<PetEntity.Gender, String> {
    @Override
    public String convert(@NonNull PetEntity.Gender source) {
      return switch (source) {
        case MALE -> "male";
        case FEMALE -> "female";
      };
    }
  }

  @ReadingConverter
  static class AvailabilityReadingConverter implements Converter<String, PetEntity.Availability> {
    @Override
    public PetEntity.Availability convert(@NonNull String source) {
      return switch (source.toLowerCase()) {
        case "available" -> PetEntity.Availability.AVAILABLE;
        case "reserved" -> PetEntity.Availability.RESERVED;
        case "sold" -> PetEntity.Availability.SOLD;
        case "coming-soon" -> PetEntity.Availability.COMING_SOON;
        default -> throw new IllegalArgumentException("Unknown availability: " + source);
      };
    }
  }

  @WritingConverter
  static class AvailabilityWritingConverter implements Converter<PetEntity.Availability, String> {
    @Override
    public String convert(@NonNull PetEntity.Availability source) {
      return switch (source) {
        case AVAILABLE -> "available";
        case RESERVED -> "reserved";
        case SOLD -> "sold";
        case COMING_SOON -> "coming-soon";
      };
    }
  }
}
