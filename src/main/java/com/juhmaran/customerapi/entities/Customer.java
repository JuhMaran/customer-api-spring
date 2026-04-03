package com.juhmaran.customerapi.entities;

import jakarta.persistence.*;
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
 * @author Juliane Maran
 * @since 02/04/2026
 */
@Entity
@Table(name = "customer", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

  @Id
  @GeneratedValue(generator = "UUID")
  @UuidGenerator
  @Column(length = 36, columnDefinition = "CHAR(36)", updatable = false, nullable = false)
  @JdbcTypeCode(SqlTypes.CHAR)
  private UUID id;

  @Version
  private Long version;

  @Column(nullable = false, length = 150)
  private String fullName;

  @Column(nullable = false, unique = true)
  private String email;

  // Telefone oficial (E.164)
  @Column(length = 16)
  private String phone;

  // Campo auxiliar para busca (somente números)
  @Column(name = "phone_search", length = 15)
  private String phoneSearch;

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime registrationDate;

  @UpdateTimestamp
  private LocalDateTime lastUpdate;

  @Builder.Default
  private Boolean status = true;

  // Normalização automática
  @PrePersist
  @PreUpdate
  private void normalizePhone() {
    if (this.phone != null) {
      this.phoneSearch = this.phone.replaceAll("\\D", "");
    }
  }

}
