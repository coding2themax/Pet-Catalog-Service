-- Pet Catalog Service Database Schema for PostgreSQL
-- Created: August 3, 2025

-- Drop tables if they exist (for development/testing)
DROP TABLE IF EXISTS pet_images CASCADE;
DROP TABLE IF EXISTS pet_characteristics CASCADE;
DROP TABLE IF EXISTS health_info_special_needs CASCADE;
DROP TABLE IF EXISTS health_info CASCADE;
DROP TABLE IF EXISTS locations CASCADE;
DROP TABLE IF EXISTS pets CASCADE;

-- Create sequences for auto-incrementing IDs (if needed)
CREATE SEQUENCE IF NOT EXISTS pet_seq START 1;
CREATE SEQUENCE IF NOT EXISTS location_seq START 1;
CREATE SEQUENCE IF NOT EXISTS health_info_seq START 1;
CREATE SEQUENCE IF NOT EXISTS pet_image_seq START 1;

-- Create ENUM types for Pet model enums
CREATE TYPE species_enum AS ENUM ('dog', 'cat', 'bird', 'fish', 'reptile', 'small-mammal');
CREATE TYPE age_category_enum AS ENUM ('puppy', 'young', 'adult', 'senior');
CREATE TYPE size_enum AS ENUM ('small', 'medium', 'large', 'extra-large');
CREATE TYPE gender_enum AS ENUM ('male', 'female');
CREATE TYPE availability_enum AS ENUM ('available', 'reserved', 'sold', 'coming-soon');

-- Locations table
CREATE TABLE locations (
    id BIGSERIAL PRIMARY KEY,
    store_id VARCHAR(255) NOT NULL UNIQUE,
    store_name VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    state VARCHAR(255) NOT NULL,
    zip_code VARCHAR(20) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Health Info table
CREATE TABLE health_info (
    id BIGSERIAL PRIMARY KEY,
    vaccinated BOOLEAN DEFAULT FALSE,
    spayed_neutered BOOLEAN DEFAULT FALSE,
    health_certificate BOOLEAN DEFAULT FALSE,
    last_vet_visit DATE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Pets table (main entity)
CREATE TABLE pets (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    species species_enum NOT NULL,
    breed VARCHAR(255),
    age INTEGER CHECK (age >= 0),
    age_category age_category_enum,
    size size_enum,
    gender gender_enum,
    price DECIMAL(10, 2) CHECK (price >= 0),
    description TEXT,
    availability availability_enum NOT NULL DEFAULT 'available',
    location_id BIGINT REFERENCES locations(id) ON DELETE SET NULL,
    health_info_id BIGINT REFERENCES health_info(id) ON DELETE SET NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Pet Images table (one-to-many relationship with pets)
CREATE TABLE pet_images (
    id BIGSERIAL PRIMARY KEY,
    pet_id VARCHAR(255) NOT NULL REFERENCES pets(id) ON DELETE CASCADE,
    url VARCHAR(2048) NOT NULL,
    alt_text VARCHAR(255),
    is_primary BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Pet Characteristics table (many-to-many relationship via array, but normalized for better querying)
CREATE TABLE pet_characteristics (
    id BIGSERIAL PRIMARY KEY,
    pet_id VARCHAR(255) NOT NULL REFERENCES pets(id) ON DELETE CASCADE,
    characteristic VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Health Info Special Needs table (one-to-many relationship with health_info)
CREATE TABLE health_info_special_needs (
    id BIGSERIAL PRIMARY KEY,
    health_info_id BIGINT NOT NULL REFERENCES health_info(id) ON DELETE CASCADE,
    special_need VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better performance
CREATE INDEX idx_pets_species ON pets(species);
CREATE INDEX idx_pets_age_category ON pets(age_category);
CREATE INDEX idx_pets_size ON pets(size);
CREATE INDEX idx_pets_gender ON pets(gender);
CREATE INDEX idx_pets_availability ON pets(availability);
CREATE INDEX idx_pets_location_id ON pets(location_id);
CREATE INDEX idx_pets_price ON pets(price);
CREATE INDEX idx_pets_created_at ON pets(created_at);

CREATE INDEX idx_pet_images_pet_id ON pet_images(pet_id);
CREATE INDEX idx_pet_images_is_primary ON pet_images(is_primary);

CREATE INDEX idx_pet_characteristics_pet_id ON pet_characteristics(pet_id);
CREATE INDEX idx_pet_characteristics_characteristic ON pet_characteristics(characteristic);

CREATE INDEX idx_health_info_special_needs_health_info_id ON health_info_special_needs(health_info_id);

CREATE INDEX idx_locations_store_id ON locations(store_id);
CREATE INDEX idx_locations_city ON locations(city);
CREATE INDEX idx_locations_state ON locations(state);

-- Add unique constraint to ensure only one primary image per pet
CREATE UNIQUE INDEX idx_pet_primary_image ON pet_images(pet_id) WHERE is_primary = true;

-- Create triggers for updating updated_at timestamps
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_pets_updated_at BEFORE UPDATE ON pets
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_locations_updated_at BEFORE UPDATE ON locations
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_health_info_updated_at BEFORE UPDATE ON health_info
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_pet_images_updated_at BEFORE UPDATE ON pet_images
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Sample data insertion (optional - remove if not needed)
-- INSERT INTO locations (store_id, store_name, city, state, zip_code) VALUES
-- ('STORE001', 'Downtown Pet Store', 'New York', 'NY', '10001'),
-- ('STORE002', 'Suburban Pet Paradise', 'Los Angeles', 'CA', '90210'),
-- ('STORE003', 'Pet Corner', 'Chicago', 'IL', '60601');

-- INSERT INTO health_info (vaccinated, spayed_neutered, health_certificate) VALUES
-- (true, true, true),
-- (true, false, true),
-- (false, false, false);

-- Comments explaining design decisions:
-- 1. Used ENUM types for the Java enums to ensure data integrity
-- 2. Normalized the List<String> fields (characteristics, specialNeeds) into separate tables for better querying
-- 3. Used BIGSERIAL for auto-incrementing surrogate keys where appropriate
-- 4. Kept the Pet.id as VARCHAR to allow for custom ID formats
-- 5. Added proper foreign key relationships with appropriate cascade options
-- 6. Added indexes on commonly queried fields for performance
-- 7. Used TIMESTAMP WITH TIME ZONE for proper timezone handling
-- 8. Added check constraints for data validation (age >= 0, price >= 0)
-- 9. Added unique constraint for primary images to ensure business rule compliance
-- 10. Created triggers for automatic updated_at timestamp management
