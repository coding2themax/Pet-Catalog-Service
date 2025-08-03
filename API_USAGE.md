# Pet Catalog Service

A reactive Spring Boot microservice for managing pet inventory, breeds, characteristics, and search functionality using Spring WebFlux.

## Features

- **Reactive Architecture**: Built with Spring WebFlux for non-blocking I/O
- **OpenAPI 3.0 Specification**: Fully documented API
- **Pet Management**: CRUD operations for pet catalog
- **Advanced Search**: Filter pets by species, breed, size, age, price, and more
- **Pagination**: Efficient handling of large datasets
- **Validation**: Input validation using Bean Validation
- **Error Handling**: Comprehensive error responses
- **Sample Data**: Pre-loaded demo data for testing

## API Endpoints

### Pet Management

- `GET /catalog/v1/pets` - Search and filter pets with pagination
- `POST /catalog/v1/pets` - Create a new pet
- `GET /catalog/v1/pets/{petId}` - Get pet details by ID
- `PUT /catalog/v1/pets/{petId}` - Update pet information
- `DELETE /catalog/v1/pets/{petId}` - Remove pet from catalog
- `PATCH /catalog/v1/pets/{petId}/availability` - Update pet availability

### Catalog Information

- `GET /catalog/v1/breeds` - Get available breeds (optionally filter by species)
- `GET /catalog/v1/species` - Get all available species

## Quick Start

### Prerequisites

- Java 21 or higher
- Maven 3.6 or higher

### Running the Service

1. **Clone and build the project:**

   ```bash
   cd Pet-Catalog-Service
   mvn clean compile
   ```

2. **Run the application:**

   ```bash
   mvn spring-boot:run
   ```

3. **The service will start on port 3001:**
   ```
   http://localhost:3001
   ```

### Sample API Calls

1. **Search all pets:**

   ```bash
   curl -X GET "http://localhost:3001/catalog/v1/pets"
   ```

2. **Search pets by species:**

   ```bash
   curl -X GET "http://localhost:3001/catalog/v1/pets?species=dog"
   ```

3. **Search with filters and pagination:**

   ```bash
   curl -X GET "http://localhost:3001/catalog/v1/pets?species=dog&size=large&page=1&limit=10"
   ```

4. **Get a specific pet:**

   ```bash
   curl -X GET "http://localhost:3001/catalog/v1/pets/{petId}"
   ```

5. **Create a new pet:**

   ```bash
   curl -X POST "http://localhost:3001/catalog/v1/pets" \
     -H "Content-Type: application/json" \
     -d '{
       "name": "Max",
       "species": "dog",
       "breed": "Labrador",
       "age": 36,
       "size": "large",
       "gender": "male",
       "price": 1000.0,
       "description": "Friendly Labrador"
     }'
   ```

6. **Get all breeds:**

   ```bash
   curl -X GET "http://localhost:3001/catalog/v1/breeds"
   ```

7. **Get breeds for dogs:**

   ```bash
   curl -X GET "http://localhost:3001/catalog/v1/breeds?species=dog"
   ```

8. **Get all species:**
   ```bash
   curl -X GET "http://localhost:3001/catalog/v1/species"
   ```

## Search Parameters

The `/pets` endpoint supports the following query parameters:

- `species`: Filter by pet species (dog, cat, bird, fish, reptile, small-mammal)
- `breed`: Filter by pet breed (partial match)
- `size`: Filter by size (small, medium, large, extra-large)
- `age_range`: Filter by age category (puppy, young, adult, senior)
- `price_min`: Minimum price filter
- `price_max`: Maximum price filter
- `availability`: Filter by status (available, reserved, sold, coming-soon)
- `gender`: Filter by gender (male, female)
- `vaccinated`: Filter by vaccination status (true/false)
- `sort_by`: Sort field (price, age, date_added, popularity)
- `sort_order`: Sort order (asc, desc)
- `page`: Page number (default: 1)
- `limit`: Items per page (default: 20, max: 100)

## Project Structure

```
src/
├── main/java/com/coding2/the/max/petstore/catalog/
│   ├── controller/          # REST Controllers
│   │   ├── PetController.java
│   │   └── CatalogController.java
│   ├── service/             # Business Logic
│   │   ├── PetService.java
│   │   └── CatalogService.java
│   ├── model/               # Domain Models
│   │   ├── Pet.java
│   │   ├── HealthInfo.java
│   │   ├── PetImage.java
│   │   └── Location.java
│   ├── dto/                 # Data Transfer Objects
│   │   ├── CreatePetRequest.java
│   │   ├── UpdatePetRequest.java
│   │   ├── PetSearchResponse.java
│   │   └── ...
│   ├── exception/           # Exception Handling
│   │   ├── GlobalExceptionHandler.java
│   │   └── PetNotFoundException.java
│   └── config/              # Configuration
│       ├── WebFluxConfig.java
│       └── SampleDataInitializer.java
└── resources/
    ├── application.properties
    └── oas.yaml            # OpenAPI Specification
```

## Technology Stack

- **Spring Boot 3.5.4**: Framework
- **Spring WebFlux**: Reactive web stack
- **Spring Validation**: Input validation
- **Jackson**: JSON serialization
- **Lombok**: Code generation
- **JUnit 5**: Testing framework
- **Maven**: Build tool

## Sample Data

The service includes sample data that is automatically loaded on startup:

1. **Buddy** - Golden Retriever (Dog)
2. **Luna** - Persian (Cat)
3. **Charlie** - Parakeet (Bird)

## Error Handling

The service provides comprehensive error responses with the following structure:

```json
{
  "error": "ERROR_CODE",
  "message": "Human readable error message",
  "details": {
    "field": "validation error"
  },
  "timestamp": "2025-08-03T10:30:00Z",
  "request_id": "unique-request-id"
}
```

## OpenAPI Documentation

The complete API specification is available in the `oas.yaml` file, which includes:

- All endpoint definitions
- Request/response schemas
- Validation rules
- Error responses
- Example payloads

You can use tools like Swagger UI or Postman to import and explore the API specification.
