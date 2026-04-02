package com.juhmaran.customerapi.repositories;

import com.juhmaran.customerapi.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * customer-api-spring
 *
 * @author Juliane Maran
 * @since 02/04/2026
 */
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

  Optional<Customer> findByEmail(String email);

  boolean existsByEmail(String email);

}
