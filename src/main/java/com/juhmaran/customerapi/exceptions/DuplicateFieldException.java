package com.juhmaran.customerapi.exceptions;

/**
 * Conflict
 *
 * @author Juliane Maran
 * @since 02/04/2026
 */
public class DuplicateFieldException extends RuntimeException {
  public DuplicateFieldException(String message) {
    super(message);
  }
}
