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
        new SizeWritingConverter());
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
}
