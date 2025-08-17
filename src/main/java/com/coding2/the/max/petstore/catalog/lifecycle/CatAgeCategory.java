package com.coding2.the.max.petstore.catalog.lifecycle;

/**
 * Determines a cat's life stage based on its age in YEARS (not months).
 * Categories (inclusive lower bounds, exclusive upper bounds except last):
 * < 1 year -> Kitten
 * 1 - <3 -> Young Adult
 * 3 - <11 -> Adult
 * 11 - <15 -> Senior
 * 15+ -> Geriatric
 */
public class CatAgeCategory implements AgeCategoryProvider {

  @Override
  public String getAgeCategory(int ageInYears) {
    if (ageInYears < 0) {
      throw new IllegalArgumentException("Age cannot be negative");
    }
    if (ageInYears < 1)
      return "Kitten";
    if (ageInYears < 3)
      return "Young Adult";
    if (ageInYears < 11)
      return "Adult";
    if (ageInYears < 15)
      return "Senior";
    return "Geriatric";
  }
}
