package com.coding2.the.max.petstore.catalog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories(basePackages = "com.coding2.the.max.petstore.catalog.repository")
public class R2dbcConfig {
}
