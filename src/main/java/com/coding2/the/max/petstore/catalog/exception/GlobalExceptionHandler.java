package com.coding2.the.max.petstore.catalog.exception;

import com.coding2.the.max.petstore.catalog.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(PetNotFoundException.class)
  public Mono<ResponseEntity<ErrorResponse>> handlePetNotFoundException(PetNotFoundException ex) {
    log.error("Pet not found: {}", ex.getMessage());

    ErrorResponse error = ErrorResponse.builder()
        .error("PET_NOT_FOUND")
        .message(ex.getMessage())
        .timestamp(Instant.now())
        .requestId(UUID.randomUUID().toString())
        .build();

    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(error));
  }

  @ExceptionHandler(WebExchangeBindException.class)
  public Mono<ResponseEntity<ErrorResponse>> handleValidationException(WebExchangeBindException ex) {
    log.error("Validation error: {}", ex.getMessage());

    Map<String, String> details = new HashMap<>();
    BindingResult bindingResult = ex.getBindingResult();

    for (FieldError fieldError : bindingResult.getFieldErrors()) {
      details.put(fieldError.getField(), fieldError.getDefaultMessage());
    }

    ErrorResponse error = ErrorResponse.builder()
        .error("VALIDATION_ERROR")
        .message("Invalid request parameters")
        .details(details)
        .timestamp(Instant.now())
        .requestId(UUID.randomUUID().toString())
        .build();

    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public Mono<ResponseEntity<ErrorResponse>> handleIllegalArgumentException(IllegalArgumentException ex) {
    log.error("Illegal argument: {}", ex.getMessage());

    ErrorResponse error = ErrorResponse.builder()
        .error("INVALID_PARAMETERS")
        .message(ex.getMessage())
        .timestamp(Instant.now())
        .requestId(UUID.randomUUID().toString())
        .build();

    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error));
  }

  @ExceptionHandler(Exception.class)
  public Mono<ResponseEntity<ErrorResponse>> handleGenericException(Exception ex) {
    log.error("Unexpected error: ", ex);

    ErrorResponse error = ErrorResponse.builder()
        .error("INTERNAL_ERROR")
        .message("An unexpected error occurred")
        .timestamp(Instant.now())
        .requestId(UUID.randomUUID().toString())
        .build();

    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error));
  }
}
