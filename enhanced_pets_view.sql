-- Enhanced pets view that includes all necessary data for the API
CREATE OR REPLACE VIEW pets_complete_details AS
SELECT
    -- Core pet data
    p.id,
    p.name,
    p.age,
    p.size,
    p.gender,
    p.price,
    p.description,
    p.availability,
    p.location_id,
    p.health_info_id,
    p.breed_id,
    p.created_at,
    p.updated_at,
    
    -- Breed information
    b.name AS breed_name,
    b.species,
    
    -- Age category
    pac.age_category,
    
    -- Health information (with safe defaults)
    COALESCE(h.vaccinated, false) AS vaccinated,
    COALESCE(h.spayed_neutered, false) AS spayed_neutered,
    COALESCE(h.health_certificate, false) AS health_certificate,
    
    -- Location information
    l.store_name,
    l.city,
    l.state,
    
    -- Computed fields for API convenience
    CASE 
        WHEN p.availability = 'available' THEN true 
        ELSE false 
    END AS is_available,
    
    -- Primary image URL (if needed)
    (
        SELECT pi.url 
        FROM pet_images pi 
        WHERE pi.pet_id = p.id 
        AND pi.is_primary = true 
        LIMIT 1
    ) AS primary_image_url

FROM pets p
LEFT JOIN breeds b ON b.id = p.breed_id
LEFT JOIN pet_age_categories pac ON pac.pet_id = p.id
LEFT JOIN health_info h ON h.id = p.health_info_id
LEFT JOIN locations l ON l.id = p.location_id;

-- Create an index on the view for better performance
CREATE INDEX IF NOT EXISTS idx_pets_complete_species ON pets (breed_id);
CREATE INDEX IF NOT EXISTS idx_pets_complete_availability ON pets (availability);
CREATE INDEX IF NOT EXISTS idx_pets_complete_price ON pets (price);
CREATE INDEX IF NOT EXISTS idx_pets_complete_created_at ON pets (created_at);