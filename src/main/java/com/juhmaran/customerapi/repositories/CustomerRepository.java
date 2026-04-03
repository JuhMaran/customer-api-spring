package com.juhmaran.customerapi.repositories;

import com.juhmaran.customerapi.entities.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

/**
 * JPA Repository
 *
 * @author Juliane Maran
 * @since 02/04/2026
 */
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

  boolean existsByEmail(String email);

  Page<Customer> findAllByStatusTrue(Pageable pageable);

  boolean existsByEmailAndStatusTrue(String email);

  @Query("""
      SELECT c FROM Customer c
      WHERE c.status = true
      AND (
        (:query IS NOT NULL AND :query <> '' AND (
          LOWER(c.fullName) LIKE LOWER(CONCAT('%', :query, '%'))
          OR LOWER(c.email) LIKE LOWER(CONCAT('%', :query, '%'))
        ))
        OR
        (:phoneQuery IS NOT NULL AND :phoneQuery <> '' AND
          c.phoneSearch LIKE CONCAT('%', :phoneQuery, '%')
        )
      )
    """)
  Page<Customer> search(@Param("query") String query, @Param("phoneQuery") String phoneQuery, Pageable pageable);
}
