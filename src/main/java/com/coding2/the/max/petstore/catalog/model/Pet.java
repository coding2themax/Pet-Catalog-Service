package com.coding2.the.max.petstore.catalog.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
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
@Table("pets")
public class Pet {

  @Id
  private String id;
  private String name;
  private Species species;
  private String breed;
  private Integer age;
  @JsonProperty("age_category")
  @Column("age_category")
  private String ageCategory;
  private Size size;
  private Gender gender;
  private Double price;
  private String description;
  // Mapped via pet_characteristics table
  @Transient
  private List<String> characteristics;
  @JsonProperty("health_info")
  // Referenced via pets.health_info_id
  @Transient
  private HealthInfo healthInfo;
  private Availability availability;
  // Mapped via pet_images table
  @Transient
  private List<PetImage> images;
  // Referenced via pets.location_id
  @Transient
  private Location location;
  @JsonProperty("created_at")
  @CreatedDate
  @Column("created_at")
  private Instant createdAt;
  @JsonProperty("updated_at")
  @LastModifiedDate
  @Column("updated_at")
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
