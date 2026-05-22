package com.coding2.the.max.petstore.catalog.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("pet_images")
public class PetImage {

  @Id
  private Long id;

  @JsonProperty("pet_id")
  @Column("pet_id")
  private String petId;

  private String url;
  @JsonProperty("alt_text")
  @Column("alt_text")
  private String altText;
  @JsonProperty("is_primary")
  @Column("is_primary")
  private Boolean isPrimary;

  @JsonProperty("created_at")
  @CreatedDate
  @Column("created_at")
  private Instant createdAt;
  @JsonProperty("updated_at")
  @LastModifiedDate
  @Column("updated_at")
  private Instant updatedAt;
}
