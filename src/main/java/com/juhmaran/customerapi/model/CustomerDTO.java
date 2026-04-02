package com.juhmaran.customerapi.model;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * <p>
 * O DTO representa a <b>entrada/saída de dados</b> da API.
 * </p>
 *
 * @author Juliane Maran
 * @since 02/04/2026
 */
public record CustomerDTO(
  UUID id,

  // O NotBlank garante que a entrada do usuário não seja vazia
  // O Size especifica o tamanho mínimo e máximo do campo
  @NotBlank(message = "Full name is required")
  @Size(min = 3, max = 150, message = "Full name must be between 3 and 150 characters")
  String fullName,

  // O NotBlank garante que a entrada do usuário não seja vazia
  @NotBlank(message = "Email is required")
  @Email(message = "Email must be valid")
  String email,

  // Mantém a validação do número de telefone no padrão internacional E.164
  @Pattern(regexp = "^\\+?[1-9]\\d{10,14}$", message = "Phone must follow E.164 format")
  String phone,

  LocalDateTime registrationDate,
  LocalDateTime lastUpdate,

  // NotNull impede que o campo seja null
  @NotNull(message = "Status cannot be null")
  Boolean status
) {
}
