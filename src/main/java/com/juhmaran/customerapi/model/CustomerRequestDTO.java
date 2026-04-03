package com.juhmaran.customerapi.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * <p>
 * O DTO representa a <b>entrada de dados</b> da API.
 * </p>
 *
 * @author Juliane Maran
 * @since 02/04/2026
 */
@Builder
public record CustomerRequestDTO(

  @NotBlank(message = "Full name is required")
  @Size(min = 3, max = 150, message = "Full name must be between 3 and 150 characters")
  String fullName,

  @NotBlank(message = "Email is required")
  @Email(message = "Email must be valid")
  String email,

  @Pattern(
    regexp = "^\\+?[1-9]\\d{10,14}$",
    message = "Phone must follow E.164 format"
  )
  String phone
) {
}
