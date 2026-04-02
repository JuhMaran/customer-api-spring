package com.juhmaran.customerapi.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidade que representa um Cliente.
 *
 * <p>
 * A entidade representa o modelo de persistência. <br>
 * Incluir validação na entidade acopla regras de API diretamente ao banco,
 * dificultando reutilizar a entidade me contextos diferentes.
 * </p>
 *
 * @author Juliane Maran
 * @since 02/04/2026
 */
@Entity
@Table(name = "customer")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

  /**
   * Identificador único do cliente (UUID).
   * <p>Gerado automaticamente pelo Hibernate.</p>
   */
  @Id
  @GeneratedValue(generator = "UUID")
  @UuidGenerator
  @Column(length = 36, columnDefinition = "CHAR(36)", updatable = false, nullable = false)
  @JdbcTypeCode(SqlTypes.CHAR)
  private UUID id;

  /**
   * Versão do registro, usada para controle de concorrência otimista.
   */
  @Version
  private Integer version;

  /**
   * Nome completo do cliente.
   * <p>Regras de validação:</p>
   * <ul>
   *     <li>Obrigatório</li>
   *     <li>Mínimo 3 caracteres, máximo 150 caracteres</li>
   * </ul>
   */
  @Column(name = "full_name", nullable = false)
  private String fullName;

  /**
   * E-mail do cliente.
   * <p>Regras de validação:</p>
   * <ul>
   *     <li>Obrigatório</li>
   *     <li>Deve ser único</li>
   *     <li>Formato válido de e-mail</li>
   * </ul>
   */
  @Column(unique = true, nullable = false)
  private String email;

  /**
   * Número de telefone do cliente no formato internacional E.164.
   * <p>Regras de validação:</p>
   * <ul>
   *     <li>Deve conter de 11 a 15 dígitos.</li>
   *     <li>O caractere '+' é opcional no início.</li>
   *     <li>O código do país (1 a 3 dígitos) é opcional.</li>
   * </ul>
   * <p>Exemplos válidos:</p>
   * <ul>
   *     <li>+5511998765432 → +55 11 99876-5432 (Brasil)</li>
   *     <li>11998765432 → 11 99876-5432 (Brasil, sem código do país)</li>
   * </ul>
   */
  @Column(length = 20)
  private String phone;

  /**
   * Data de cadastro do cliente.
   * <p>Gerada automaticamente na criação do registro e não pode ser atualizada.</p>
   */
  @CreationTimestamp
  @Column(name = "registration_date", nullable = false, updatable = false)
  private LocalDateTime registrationDate;

  /**
   * Data da última atualização do registro.
   * <p>Atualizada automaticamente sempre que o registro for modificado.</p>
   */
  @UpdateTimestamp
  @Column(name = "last_update", nullable = false)
  private LocalDateTime lastUpdate;

  /**
   * Status do cliente.
   * <ul>
   *     <li>Ativo → true</li>
   *     <li>Inativo → false</li>
   * </ul>
   */
  @Column(nullable = false)
  @Builder.Default
  private Boolean status = true;

}
