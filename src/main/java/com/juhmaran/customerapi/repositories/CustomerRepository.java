package com.juhmaran.customerapi.repositories;

import com.juhmaran.customerapi.entities.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

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

}
