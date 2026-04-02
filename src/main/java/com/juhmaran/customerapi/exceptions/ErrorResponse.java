package com.juhmaran.customerapi.exceptions;

import lombok.Builder;

import java.time.LocalDateTime;

/**
 * customer-api-spring
 *
 * @author Juliane Maran
 * @since 02/04/2026
 */
@Builder
public record ErrorResponse(
  LocalDateTime timestamp,
  int status,
  String error,
  String message,
  String path
) {
}
