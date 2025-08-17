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

import java.math.BigDecimal;
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
  private Integer age;
  private Size size;
  private Gender gender;
  private BigDecimal price;
  private String description;
  private Availability availability;

  // Foreign key references
  @JsonProperty("location_id")
  @Column("location_id")
  private Long locationId;
  @JsonProperty("health_info_id")
  @Column("health_info_id")
  private Long healthInfoId;
  @JsonProperty("breed_id")
  @Column("breed_id")
  private Long breedId;

  // Transient fields for related data (populated via joins or separate queries)
  @Transient
  private List<String> characteristics;
  @JsonProperty("health_info")
  @Transient
  private HealthInfo healthInfo;
  @Transient
  private List<PetImage> images;
  @Transient
  private Location location;
  @Transient
  private Breed breed;

  // Derived fields from views/calculations
  @JsonProperty("age_category")
  @Transient
  private AgeCategory ageCategory;
  @Transient
  private Species species; // Derived from breed
  @JsonProperty("breed_name")
  @Transient
  private String breedName; // Derived from breed

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
