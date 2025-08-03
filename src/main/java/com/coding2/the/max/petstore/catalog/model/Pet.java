package com.coding2.the.max.petstore.catalog.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pet {

  private String id;
  private String name;
  private Species species;
  private String breed;
  private Integer age;
  @JsonProperty("age_category")
  private AgeCategory ageCategory;
  private Size size;
  private Gender gender;
  private Double price;
  private String description;
  private List<String> characteristics;
  @JsonProperty("health_info")
  private HealthInfo healthInfo;
  private Availability availability;
  private List<PetImage> images;
  private Location location;
  @JsonProperty("created_at")
  private Instant createdAt;
  @JsonProperty("updated_at")
  private Instant updatedAt;

  public enum Species {
    @JsonProperty("dog")
    DOG,
    @JsonProperty("cat")
    CAT,
    @JsonProperty("bird")
    BIRD,
    @JsonProperty("fish")
    FISH,
    @JsonProperty("reptile")
    REPTILE,
    @JsonProperty("small-mammal")
    SMALL_MAMMAL
  }

  public enum AgeCategory {
    @JsonProperty("puppy")
    PUPPY,
    @JsonProperty("young")
    YOUNG,
    @JsonProperty("adult")
    ADULT,
    @JsonProperty("senior")
    SENIOR
  }

  public enum Size {
    @JsonProperty("small")
    SMALL,
    @JsonProperty("medium")
    MEDIUM,
    @JsonProperty("large")
    LARGE,
    @JsonProperty("extra-large")
    EXTRA_LARGE
  }

  public enum Gender {
    @JsonProperty("male")
    MALE,
    @JsonProperty("female")
    FEMALE
  }

  public enum Availability {
    @JsonProperty("available")
    AVAILABLE,
    @JsonProperty("reserved")
    RESERVED,
    @JsonProperty("sold")
    SOLD,
    @JsonProperty("coming-soon")
    COMING_SOON
  }
}
