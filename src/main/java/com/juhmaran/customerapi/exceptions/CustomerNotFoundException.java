package com.juhmaran.customerapi.exceptions;

/**
 * Customer Not Found
 *
 * @author Juliane Maran
 * @since 02/04/2026
 */
public class CustomerNotFoundException extends RuntimeException {

  public CustomerNotFoundException(String message) {
    super(message);
  }

}
