package com.coding2.the.max.petstore.catalog.lifecycle;

/**
 * Determines a bird's life stage based on age in YEARS.
 * Generic layering (can vary widely by species; adjust if species-specific
 * rules emerge):
 * < 1 year -> Hatchling / Juvenile
 * 1 - <3 -> Young
 * 3 - <7 -> Adult
 * 7 - <12 -> Mature
 * 12+ -> Senior
 */
public class BirdAgeCategory implements AgeCategoryProvider {

  @Override
  public String getAgeCategory(int ageInYears) {
    if (ageInYears < 0) {
      throw new IllegalArgumentException("Age cannot be negative");
    }
    if (ageInYears < 1)
      return "Hatchling / Juvenile";
    if (ageInYears < 3)
      return "Young";
    if (ageInYears < 7)
      return "Adult";
    if (ageInYears < 12)
      return "Mature";
    return "Senior";
  }
}
