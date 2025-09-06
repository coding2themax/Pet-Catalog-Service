package com.coding2.the.max.petstore.catalog.config;

import com.coding2.the.max.petstore.catalog.model.Pet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.PostgresDialect;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.lang.NonNull;

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
  static class SpeciesReadingConverter implements Converter<String, Pet.Species> {
    @Override
    public Pet.Species convert(@NonNull String source) {
      return switch (source.toLowerCase()) {
        case "dog" -> Pet.Species.DOG;
        case "cat" -> Pet.Species.CAT;
        case "bird" -> Pet.Species.BIRD;
        case "fish" -> Pet.Species.FISH;
        case "reptile" -> Pet.Species.REPTILE;
        case "small-mammal" -> Pet.Species.SMALL_MAMMAL;
        default -> throw new IllegalArgumentException("Unknown species: " + source);
      };
    }
  }

  @WritingConverter
  static class SpeciesWritingConverter implements Converter<Pet.Species, String> {
    @Override
    public String convert(@NonNull Pet.Species source) {
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
  static class SizeReadingConverter implements Converter<String, Pet.Size> {
    @Override
    public Pet.Size convert(@NonNull String source) {
      return switch (source.toLowerCase()) {
        case "small" -> Pet.Size.SMALL;
        case "medium" -> Pet.Size.MEDIUM;
        case "large" -> Pet.Size.LARGE;
        case "extra-large" -> Pet.Size.EXTRA_LARGE;
        default -> throw new IllegalArgumentException("Unknown size: " + source);
      };
    }
  }

  @WritingConverter
  static class SizeWritingConverter implements Converter<Pet.Size, String> {
    @Override
    public String convert(@NonNull Pet.Size source) {
      return switch (source) {
        case SMALL -> "small";
        case MEDIUM -> "medium";
        case LARGE -> "large";
        case EXTRA_LARGE -> "extra-large";
      };
    }
  }
}
