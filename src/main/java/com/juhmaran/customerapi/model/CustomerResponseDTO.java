package com.juhmaran.customerapi.model;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * <p>
 * O DTO representa a <b>saída de dados</b> da API.
 * </p>
 *
 * @author Juliane Maran
 * @since 02/04/2026
 */
@Builder
public record CustomerResponseDTO(
  UUID id,
  String fullName,
  String email,
  String phone,
  LocalDateTime registrationDate,
  LocalDateTime lastUpdate,
  Boolean status
) {
}
