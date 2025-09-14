package com.coding2.the.max.petstore.catalog.model;

import com.coding2.the.max.petstore.catalog.domain.entity.PetEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("breeds")
public class Breed {

  @Id
  private Long id;

  private String name;

  private PetEntity.Species species;

  @JsonProperty("typical_size")
  @Column("typical_size")
  private PetEntity.Size typicalSize;

  private List<String> characteristics;

  @JsonProperty("created_at")
  @CreatedDate
  @Column("created_at")
  private Instant createdAt;
}
