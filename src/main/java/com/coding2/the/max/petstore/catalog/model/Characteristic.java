package com.coding2.the.max.petstore.catalog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("characteristics")
public class Characteristic {

  @Id
  private Long id;

  private String name;

  @CreatedDate
  @Column("created_at")
  private Instant createdAt;
}
