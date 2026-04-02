package com.juhmaran.customerapi.exceptions;

/**
 * customer-api-spring
 *
 * @author Juliane Maran
 * @since 02/04/2026
 */
public class DuplicateFieldException extends RuntimeException {
  public DuplicateFieldException(String message) {
    super(message);
  }
}
