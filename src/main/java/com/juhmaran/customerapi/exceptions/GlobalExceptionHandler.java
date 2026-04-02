package com.juhmaran.customerapi.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Exception Handler
 *
 * @author Juliane Maran
 * @since 02/04/2026
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  // 404 Not Found
  @ExceptionHandler(CustomerNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(CustomerNotFoundException ex, HttpServletRequest request) {
    ErrorResponse error = buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    log.warn("404 Not Found: {}", error);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  // 400 Bad Request - DTOs inválidos
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
    String message = ex.getBindingResult().getFieldErrors()
      .stream()
      .map(f -> f.getField() + ": " + f.getDefaultMessage())
      .collect(Collectors.joining("; "));
    ErrorResponse error = buildError(HttpStatus.BAD_REQUEST, message, request);
    log.warn("400 Bad Request - DTO inválido: {}", error);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  // 400 Bad Request - Validações de path/params
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
    String message = ex.getConstraintViolations()
      .stream()
      .map(v -> v.getPropertyPath() + ": " + v.getMessage())
      .collect(Collectors.joining("; "));
    ErrorResponse error = buildError(HttpStatus.BAD_REQUEST, message, request);
    log.warn("400 Bad Request - Constraint violation: {}", error);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  // 400 Bad Request - Regras de negócio simples
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
    ErrorResponse error = buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    log.warn("400 Bad Request - Regra de negócio: {}", error);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  // 409 Conflict - Email já cadastrado
  @ExceptionHandler(DuplicateFieldException.class)
  public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(DuplicateFieldException ex, HttpServletRequest request) {
    ErrorResponse error = buildError(HttpStatus.CONFLICT, ex.getMessage(), request);
    log.warn("409 Conflict - Email duplicado: {}", error);
    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
  }

  // 500 Internal Server Error - Erros inesperados
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleAll(Exception ex, HttpServletRequest request) {
    ErrorResponse error = buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro inesperado", request);
    log.error("500 Internal Server Error: {}", error, ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }

  // Helper method para construir ErrorResponse
  private ErrorResponse buildError(HttpStatus status, String message, HttpServletRequest request) {
    return ErrorResponse.builder()
      .timestamp(LocalDateTime.now())
      .status(status.value())
      .error(status.getReasonPhrase())
      .message(message)
      .path(request.getRequestURI())
      .build();
  }

}
