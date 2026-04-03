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

  Page<Customer> findAllByStatusFalse(Pageable pageable);

  @Query("""
    SELECT c FROM Customer c
    WHERE (:status IS NULL OR c.status = :status)
    AND (:fullName IS NULL OR LOWER(c.fullName) LIKE LOWER(CONCAT('%', :fullName, '%')))
    AND (:email IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%')))
    AND (:phone IS NULL OR c.phoneSearch LIKE CONCAT('%', :phone, '%'))
    """)
  Page<Customer> searchAdvanced(
    @Param("fullName") String fullName,
    @Param("email") String email,
    @Param("phone") String phone,
    @Param("status") Boolean status,
    Pageable pageable
  );

}
