-- Pet Catalog Service - Test Data Initialization Script
-- Created: August 6, 2025
-- This script populates the database with sample data for testing and development

-- Clear existing data (in reverse order of dependencies)
DELETE FROM health_info_special_needs;
DELETE FROM pet_characteristics;
DELETE FROM pet_images;
DELETE FROM pets;
DELETE FROM health_info;
DELETE FROM locations;

-- Reset sequences
ALTER SEQUENCE location_seq RESTART WITH 1;
ALTER SEQUENCE health_info_seq RESTART WITH 1;
ALTER SEQUENCE pet_image_seq RESTART WITH 1;

-- Insert test locations
INSERT INTO locations (store_id, store_name, city, state, zip_code) VALUES
('STORE001', 'Downtown Pet Store', 'New York', 'NY', '10001'),
('STORE002', 'Suburban Pet Paradise', 'Los Angeles', 'CA', '90210'),
('STORE003', 'Pet Corner', 'Chicago', 'IL', '60601'),
('STORE004', 'Mountain View Pets', 'Denver', 'CO', '80202'),
('STORE005', 'Coastal Animal House', 'Miami', 'FL', '33101');

-- Insert test health information
INSERT INTO health_info (vaccinated, spayed_neutered, health_certificate, last_vet_visit) VALUES
(true, true, true, '2025-07-15'),
(true, false, true, '2025-07-20'),
(false, false, false, '2025-06-10'),
(true, true, true, '2025-07-25'),
(true, false, true, '2025-07-01'),
(false, true, false, '2025-06-15'),
(true, true, true, '2025-07-30'),
(true, false, true, '2025-07-10'),
(false, false, true, '2025-06-20'),
(true, true, true, '2025-07-18'),
(true, false, false, '2025-06-25'),
(false, true, true, '2025-07-05');

-- Insert test pets
INSERT INTO pets (id, name, species, breed, age, age_category, size, gender, price, description, availability, location_id, health_info_id) VALUES
('PET001', 'Buddy', 'dog', 'Golden Retriever', 3, 'adult', 'large', 'male', 800.00, 'Friendly and energetic golden retriever. Great with kids and other pets. Loves playing fetch and going for walks.', 'available', 1, 1),
('PET002', 'Luna', 'cat', 'Persian', 2, 'young', 'medium', 'female', 600.00, 'Beautiful Persian cat with long, silky fur. Very calm and affectionate. Perfect lap cat for quiet households.', 'available', 1, 2),
('PET003', 'Charlie', 'dog', 'Beagle', 1, 'puppy', 'medium', 'male', 550.00, 'Adorable beagle puppy with classic tri-color markings. Curious and playful, will make a wonderful family companion.', 'reserved', 2, 3),
('PET004', 'Whiskers', 'cat', 'Maine Coon', 5, 'adult', 'large', 'male', 750.00, 'Majestic Maine Coon with impressive size and gentle temperament. Very social and loves attention.', 'available', 2, 4),
('PET005', 'Bella', 'dog', 'French Bulldog', 4, 'adult', 'small', 'female', 1200.00, 'Charming French Bulldog with a sweet personality. Good in apartments and loves being around people.', 'sold', 3, 5),
('PET006', 'Milo', 'cat', 'British Shorthair', 1, 'young', 'medium', 'male', 650.00, 'Adorable British Shorthair kitten with classic blue-gray coat. Very calm and easy-going personality.', 'available', 3, 6),
('PET007', 'Rocky', 'dog', 'German Shepherd', 6, 'adult', 'large', 'male', 900.00, 'Intelligent and loyal German Shepherd. Well-trained and great for active families. Excellent guard dog.', 'available', 4, 7),
('PET008', 'Princess', 'cat', 'Siamese', 8, 'senior', 'medium', 'female', 300.00, 'Sweet senior Siamese cat looking for a quiet retirement home. Very affectionate and well-behaved.', 'available', 4, 8),
('PET009', 'Max', 'dog', 'Labrador Retriever', 2, 'young', 'large', 'male', 700.00, 'Energetic chocolate Lab who loves swimming and playing. Great with children and very trainable.', 'coming-soon', 5, 9),
('PET010', 'Coco', 'bird', 'Cockatiel', 1, 'young', 'small', 'female', 150.00, 'Beautiful cockatiel with striking orange cheek patches. Very social and can learn to whistle tunes.', 'available', 1, 10),
('PET011', 'Nemo', 'fish', 'Goldfish', 1, 'young', 'small', 'male', 25.00, 'Classic orange goldfish perfect for beginners. Hardy and easy to care for.', 'available', 2, 11),
('PET012', 'Spike', 'reptile', 'Bearded Dragon', 3, 'adult', 'medium', 'male', 200.00, 'Docile bearded dragon with beautiful coloration. Great starter reptile, very handleable.', 'available', 3, 12);

-- Insert pet images
INSERT INTO pet_images (pet_id, url, alt_text, is_primary) VALUES
('PET001', 'https://example.com/images/buddy-1.jpg', 'Buddy the Golden Retriever sitting in grass', true),
('PET001', 'https://example.com/images/buddy-2.jpg', 'Buddy playing fetch with a tennis ball', false),
('PET001', 'https://example.com/images/buddy-3.jpg', 'Buddy portrait headshot', false),
('PET002', 'https://example.com/images/luna-1.jpg', 'Luna the Persian cat lounging', true),
('PET002', 'https://example.com/images/luna-2.jpg', 'Luna close-up showing beautiful eyes', false),
('PET003', 'https://example.com/images/charlie-1.jpg', 'Charlie the Beagle puppy playing', true),
('PET003', 'https://example.com/images/charlie-2.jpg', 'Charlie sleeping in his bed', false),
('PET004', 'https://example.com/images/whiskers-1.jpg', 'Whiskers the Maine Coon full body shot', true),
('PET004', 'https://example.com/images/whiskers-2.jpg', 'Whiskers showing off his fluffy tail', false),
('PET005', 'https://example.com/images/bella-1.jpg', 'Bella the French Bulldog portrait', true),
('PET006', 'https://example.com/images/milo-1.jpg', 'Milo the British Shorthair kitten', true),
('PET007', 'https://example.com/images/rocky-1.jpg', 'Rocky the German Shepherd standing alert', true),
('PET007', 'https://example.com/images/rocky-2.jpg', 'Rocky in training pose', false),
('PET008', 'https://example.com/images/princess-1.jpg', 'Princess the Siamese cat resting', true),
('PET009', 'https://example.com/images/max-1.jpg', 'Max the Labrador Retriever by water', true),
('PET010', 'https://example.com/images/coco-1.jpg', 'Coco the Cockatiel on perch', true),
('PET011', 'https://example.com/images/nemo-1.jpg', 'Nemo the Goldfish in aquarium', true),
('PET012', 'https://example.com/images/spike-1.jpg', 'Spike the Bearded Dragon basking', true);

-- Insert pet characteristics
INSERT INTO pet_characteristics (pet_id, characteristic) VALUES
('PET001', 'friendly'),
('PET001', 'energetic'),
('PET001', 'good-with-kids'),
('PET001', 'good-with-pets'),
('PET001', 'house-trained'),
('PET002', 'calm'),
('PET002', 'affectionate'),
('PET002', 'quiet'),
('PET002', 'independent'),
('PET003', 'playful'),
('PET003', 'curious'),
('PET003', 'social'),
('PET003', 'trainable'),
('PET004', 'gentle'),
('PET004', 'social'),
('PET004', 'majestic'),
('PET004', 'good-with-kids'),
('PET005', 'charming'),
('PET005', 'apartment-friendly'),
('PET005', 'low-maintenance'),
('PET006', 'calm'),
('PET006', 'easy-going'),
('PET006', 'beautiful'),
('PET007', 'intelligent'),
('PET007', 'loyal'),
('PET007', 'protective'),
('PET007', 'well-trained'),
('PET008', 'affectionate'),
('PET008', 'quiet'),
('PET008', 'well-behaved'),
('PET008', 'senior-friendly'),
('PET009', 'energetic'),
('PET009', 'water-loving'),
('PET009', 'good-with-kids'),
('PET009', 'trainable'),
('PET010', 'social'),
('PET010', 'musical'),
('PET010', 'colorful'),
('PET011', 'easy-care'),
('PET011', 'beginner-friendly'),
('PET011', 'peaceful'),
('PET012', 'docile'),
('PET012', 'handleable'),
('PET012', 'beginner-friendly'),
('PET012', 'unique');

-- Insert health info special needs
INSERT INTO health_info_special_needs (health_info_id, special_need) VALUES
(3, 'needs-vaccination-schedule'),
(3, 'requires-regular-deworming'),
(6, 'dietary-restrictions'),
(8, 'senior-care-required'),
(8, 'arthritis-medication'),
(9, 'puppy-vaccination-series'),
(11, 'special-diet-required'),
(12, 'UV-lighting-required'),
(12, 'temperature-controlled-environment');

-- Add some comments for data explanation
-- Sample data includes:
-- - 5 store locations across different US cities
-- - 12 pets of various species (dogs, cats, birds, fish, reptiles)
-- - Different availability statuses (available, reserved, sold, coming-soon)
-- - Various ages from puppies to seniors
-- - Multiple characteristics and special needs
-- - Image references for each pet
-- - Realistic health information and vet visit dates
