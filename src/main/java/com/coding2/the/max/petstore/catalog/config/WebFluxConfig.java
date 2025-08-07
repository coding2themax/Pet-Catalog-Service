package com.coding2.the.max.petstore.catalog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
@EnableWebFlux
public class WebFluxConfig implements WebFluxConfigurer {

  private final StringToSpeciesConverter stringToSpeciesConverter;
  private final StringToSizeConverter stringToSizeConverter;
  private final StringToAvailabilityConverter stringToAvailabilityConverter;
  private final StringToGenderConverter stringToGenderConverter;
  private final StringToAgeCategoryConverter stringToAgeCategoryConverter;

  public WebFluxConfig(StringToSpeciesConverter stringToSpeciesConverter,
      StringToSizeConverter stringToSizeConverter,
      StringToAvailabilityConverter stringToAvailabilityConverter,
      StringToGenderConverter stringToGenderConverter,
      StringToAgeCategoryConverter stringToAgeCategoryConverter) {
    this.stringToSpeciesConverter = stringToSpeciesConverter;
    this.stringToSizeConverter = stringToSizeConverter;
    this.stringToAvailabilityConverter = stringToAvailabilityConverter;
    this.stringToGenderConverter = stringToGenderConverter;
    this.stringToAgeCategoryConverter = stringToAgeCategoryConverter;
  }

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    return mapper;
  }

  @Override
  public void addFormatters(@NonNull FormatterRegistry registry) {
    registry.addConverter(stringToSpeciesConverter);
    registry.addConverter(stringToSizeConverter);
    registry.addConverter(stringToAvailabilityConverter);
    registry.addConverter(stringToGenderConverter);
    registry.addConverter(stringToAgeCategoryConverter);
  }

  @Override
  public void configureHttpMessageCodecs(@NonNull ServerCodecConfigurer configurer) {
    ObjectMapper mapper = objectMapper();
    configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(mapper));
    configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(mapper));
    configurer.defaultCodecs().maxInMemorySize(1024 * 1024); // 1MB
  }

  @Override
  public void addCorsMappings(@NonNull CorsRegistry registry) {
    registry.addMapping("/catalog/v1/**")
        .allowedOrigins("*")
        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
        .allowedHeaders("*")
        .maxAge(3600);
  }
}