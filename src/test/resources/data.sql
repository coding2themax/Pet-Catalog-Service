-- Pet Catalog Service - Test Data Initialization Script (BCNF / current schema)
-- Updated: August 17, 2025

-- Clear existing data
DELETE FROM pet_characteristics IF EXISTS;
DELETE FROM pet_images IF EXISTS;
DELETE FROM pets IF EXISTS;
DELETE FROM breeds IF EXISTS;
DELETE FROM characteristics IF EXISTS;
DELETE FROM health_info_special_needs IF EXISTS;
DELETE FROM health_info IF EXISTS;
DELETE FROM locations IF EXISTS;

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

-- Insert breeds (name, species, typical_size, characteristics) required for pets.breed_id FK
INSERT INTO breeds (name, species, typical_size, characteristics) VALUES
-- Dog breeds
('Golden Retriever','dog','large',ARRAY['friendly', 'intelligent', 'active']),
('Labrador Retriever','dog','large',ARRAY['friendly', 'outgoing', 'active']),
('Bulldog','dog','medium',ARRAY['docile', 'willful', 'friendly']),
('Poodle','dog','medium',ARRAY['intelligent', 'active', 'elegant']),
('Chihuahua','dog','small',ARRAY['sassy', 'graceful', 'charming']),
('Beagle','dog','medium',ARRAY['friendly', 'curious', 'merry']),
('French Bulldog','dog','small',ARRAY['adaptable', 'playful', 'smart']),
('German Shepherd','dog','large',ARRAY['confident', 'courageous', 'smart']),
-- Cat breeds
('Persian','cat','medium',ARRAY['quiet', 'docile', 'sweet']),
('Maine Coon','cat','large',ARRAY['gentle', 'friendly', 'intelligent']),
('Siamese','cat','medium',ARRAY['vocal', 'intelligent', 'active']),
('British Shorthair','cat','medium',ARRAY['calm', 'friendly', 'independent']),
-- Bird breeds
('Canary','bird','small',ARRAY['melodious', 'cheerful', 'social']),
('Parakeet','bird','small',ARRAY['playful', 'social', 'intelligent']),
('Macaw','bird','large',ARRAY['intelligent', 'social', 'colorful']),
('Cockatiel','bird','small',ARRAY['social', 'intelligent', 'melodious']),
-- Fish breeds
('Goldfish','fish','small',ARRAY['hardy', 'peaceful', 'colorful']),
('Betta','fish','small',ARRAY['colorful', 'territorial', 'hardy']),
('Angel Fish','fish','medium',ARRAY['graceful', 'peaceful', 'elegant']),
-- Reptile breeds
('Leopard Gecko','reptile','small',ARRAY['docile', 'easy-care', 'nocturnal']),
('Bearded Dragon','reptile','medium',ARRAY['calm', 'social', 'hardy']),
-- Small mammal breeds
('Holland Lop','small-mammal','small',ARRAY['gentle', 'calm', 'friendly']),
('Guinea Pig','small-mammal','small',ARRAY['social', 'gentle', 'vocal']),
('Hamster','small-mammal','small',ARRAY['active', 'curious', 'independent']);

-- Insert test pets (schema now: id, name, age, size, gender, price, description, availability, location_id, health_info_id, breed_id)
INSERT INTO pets (id, name, age, size, gender, price, description, availability, location_id, health_info_id, breed_id) VALUES
('PET001','Buddy',3,'large','male',800.00,'Friendly and energetic golden retriever. Great with kids and other pets. Loves playing fetch and going for walks.','available',1,1,(SELECT id FROM breeds WHERE name='Golden Retriever' AND species='dog')),
('PET002','Luna',2,'medium','female',600.00,'Beautiful Persian cat with long, silky fur. Very calm and affectionate. Perfect lap cat for quiet households.','available',1,2,(SELECT id FROM breeds WHERE name='Persian' AND species='cat')),
('PET003','Charlie',1,'medium','male',550.00,'Adorable beagle puppy with classic tri-color markings. Curious and playful, will make a wonderful family companion.','reserved',2,3,(SELECT id FROM breeds WHERE name='Beagle' AND species='dog')),
('PET004','Whiskers',5,'large','male',750.00,'Majestic Maine Coon with impressive size and gentle temperament. Very social and loves attention.','available',2,4,(SELECT id FROM breeds WHERE name='Maine Coon' AND species='cat')),
('PET005','Bella',4,'small','female',1200.00,'Charming French Bulldog with a sweet personality. Good in apartments and loves being around people.','sold',3,5,(SELECT id FROM breeds WHERE name='French Bulldog' AND species='dog')),
('PET006','Milo',1,'medium','male',650.00,'Adorable British Shorthair kitten with classic blue-gray coat. Very calm and easy-going personality.','available',3,6,(SELECT id FROM breeds WHERE name='British Shorthair' AND species='cat')),
('PET007','Rocky',6,'large','male',900.00,'Intelligent and loyal German Shepherd. Well-trained and great for active families. Excellent guard dog.','available',4,7,(SELECT id FROM breeds WHERE name='German Shepherd' AND species='dog')),
('PET008','Princess',8,'medium','female',300.00,'Sweet senior Siamese cat looking for a quiet retirement home. Very affectionate and well-behaved.','available',4,8,(SELECT id FROM breeds WHERE name='Siamese' AND species='cat')),
('PET009','Max',2,'large','male',700.00,'Energetic chocolate Lab who loves swimming and playing. Great with children and very trainable.','coming-soon',5,9,(SELECT id FROM breeds WHERE name='Labrador Retriever' AND species='dog')),
('PET010','Coco',1,'small','female',150.00,'Beautiful cockatiel with striking orange cheek patches. Very social and can learn to whistle tunes.','available',1,10,(SELECT id FROM breeds WHERE name='Cockatiel' AND species='bird')),
('PET011','Nemo',1,'small','male',25.00,'Classic orange goldfish perfect for beginners. Hardy and easy to care for.','available',2,11,(SELECT id FROM breeds WHERE name='Goldfish' AND species='fish')),
('PET012','Spike',3,'medium','male',200.00,'Docile bearded dragon with beautiful coloration. Great starter reptile, very handleable.','available',3,12,(SELECT id FROM breeds WHERE name='Bearded Dragon' AND species='reptile'));

-- Insert pet images
INSERT INTO pet_images (pet_id, url, alt_text, is_primary) VALUES
('PET001','https://images.unsplash.com/photo-1552053831-71594a27632d?w=600&h=400&fit=crop&auto=format','Buddy the Golden Retriever sitting in grass',true),
('PET001','https://images.unsplash.com/photo-1583337130417-3346a1be7dee?w=600&h=400&fit=crop&auto=format','Buddy playing fetch with a tennis ball',false),
('PET001','https://images.unsplash.com/photo-1606721977440-8d4f52b0c1e1?w=600&h=400&fit=crop&auto=format','Buddy portrait headshot',false),
('PET002','https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?w=600&h=400&fit=crop&auto=format','Luna the Persian cat lounging',true),
('PET002','https://images.unsplash.com/photo-1533743983669-94fa5c4338ec?w=600&h=400&fit=crop&auto=format','Luna close-up showing beautiful eyes',false),
('PET003','https://images.unsplash.com/photo-1544568100-847a948585b9?w=600&h=400&fit=crop&auto=format','Charlie the Beagle puppy playing',true),
('PET003','https://images.unsplash.com/photo-1601758228041-f3b2795255f1?w=600&h=400&fit=crop&auto=format','Charlie sleeping in his bed',false),
('PET004','https://images.unsplash.com/photo-1513245543132-31f507417b26?w=600&h=400&fit=crop&auto=format','Whiskers the Maine Coon full body shot',true),
('PET004','https://images.unsplash.com/photo-1574231164645-d6f0e8553590?w=600&h=400&fit=crop&auto=format','Whiskers showing off his fluffy tail',false),
('PET005','https://images.unsplash.com/photo-1583511655857-d19b40a7a54e?w=600&h=400&fit=crop&auto=format','Bella the French Bulldog portrait',true),
('PET006','https://images.unsplash.com/photo-1596854407944-bf87f6fdd49e?w=600&h=400&fit=crop&auto=format','Milo the British Shorthair kitten',true),
('PET007','https://images.unsplash.com/photo-1589941013453-ec89f33b5e95?w=600&h=400&fit=crop&auto=format','Rocky the German Shepherd standing alert',true),
('PET007','https://images.unsplash.com/photo-1605568427561-40dd23c2acea?w=600&h=400&fit=crop&auto=format','Rocky in training pose',false),
('PET008','https://images.unsplash.com/photo-1596492784531-6e6eb5ea9993?w=600&h=400&fit=crop&auto=format','Princess the Siamese cat resting',true),
('PET009','https://images.unsplash.com/photo-1518717758536-85ae29035b6d?w=600&h=400&fit=crop&auto=format','Max the Labrador Retriever by water',true),
('PET010','https://images.unsplash.com/photo-1574781330855-d0db2706b3d0?w=600&h=400&fit=crop&auto=format','Coco the Cockatiel on perch',true),
('PET011','https://images.unsplash.com/photo-1520637836862-4d197d17c958?w=600&h=400&fit=crop&auto=format','Nemo the Goldfish in aquarium',true),
('PET012','https://images.unsplash.com/photo-1612648953069-b49b2a23b2f4?w=600&h=400&fit=crop&auto=format','Spike the Bearded Dragon basking',true);

-- Insert characteristics (added missing 'peaceful')
INSERT INTO characteristics (name) VALUES
('friendly'),
('energetic'),
('good-with-kids'),
('good-with-pets'),
('house-trained'),
('calm'),
('affectionate'),
('quiet'),
('independent'),
('playful'),
('curious'),
('social'),
('trainable'),
('gentle'),
('majestic'),
('charming'),
('apartment-friendly'),
('low-maintenance'),
('easy-going'),
('beautiful'),
('intelligent'),
('loyal'),
('protective'),
('well-trained'),
('well-behaved'),
('senior-friendly'),
('water-loving'),
('musical'),
('colorful'),
('easy-care'),
('beginner-friendly'),
('docile'),
('handleable'),
('unique'),
('peaceful');

-- Map pets to characteristics
INSERT INTO pet_characteristics (pet_id, characteristic_id)
SELECT 'PET001', id FROM characteristics WHERE name IN ('friendly','energetic','good-with-kids','good-with-pets','house-trained');
INSERT INTO pet_characteristics (pet_id, characteristic_id)
SELECT 'PET002', id FROM characteristics WHERE name IN ('calm','affectionate','quiet','independent');
INSERT INTO pet_characteristics (pet_id, characteristic_id)
SELECT 'PET003', id FROM characteristics WHERE name IN ('playful','curious','social','trainable');
INSERT INTO pet_characteristics (pet_id, characteristic_id)
SELECT 'PET004', id FROM characteristics WHERE name IN ('gentle','social','majestic','good-with-kids');
INSERT INTO pet_characteristics (pet_id, characteristic_id)
SELECT 'PET005', id FROM characteristics WHERE name IN ('charming','apartment-friendly','low-maintenance');
INSERT INTO pet_characteristics (pet_id, characteristic_id)
SELECT 'PET006', id FROM characteristics WHERE name IN ('calm','easy-going','beautiful');
INSERT INTO pet_characteristics (pet_id, characteristic_id)
SELECT 'PET007', id FROM characteristics WHERE name IN ('intelligent','loyal','protective','well-trained');
INSERT INTO pet_characteristics (pet_id, characteristic_id)
SELECT 'PET008', id FROM characteristics WHERE name IN ('affectionate','quiet','well-behaved','senior-friendly');
INSERT INTO pet_characteristics (pet_id, characteristic_id)
SELECT 'PET009', id FROM characteristics WHERE name IN ('energetic','water-loving','good-with-kids','trainable');
INSERT INTO pet_characteristics (pet_id, characteristic_id)
SELECT 'PET010', id FROM characteristics WHERE name IN ('social','musical','colorful');
INSERT INTO pet_characteristics (pet_id, characteristic_id)
SELECT 'PET011', id FROM characteristics WHERE name IN ('easy-care','beginner-friendly','peaceful');
INSERT INTO pet_characteristics (pet_id, characteristic_id)
SELECT 'PET012', id FROM characteristics WHERE name IN ('docile','handleable','beginner-friendly','unique');

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

-- Age categories are derived via pet_age_categories view.
