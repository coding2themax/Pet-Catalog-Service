package com.coding2.the.max.petstore.catalog.lifecycle;

public class DogAgeCategory implements AgeCategoryProvider {
  @Override
  public String getAgeCategory(int ageInMonths) {
    if (ageInMonths < 2)
      return "Newborn / Neonate";
    else if (ageInMonths < 6)
      return "Puppy";
    else if (ageInMonths < 24)
      return "Adolescent";
    else if (ageInMonths < 84)
      return "Adult";
    else if (ageInMonths < 120)
      return "Senior";
    else
      return "Geriatric";
  }
}
