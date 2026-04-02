package com.juhmaran.customerapi.repositories;

import com.juhmaran.customerapi.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * customer-api-spring
 *
 * @author Juliane Maran
 * @since 02/04/2026
 */
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

  boolean existsByEmail(String email);

  // Lista apenas clientes ativos
  List<Customer> findAllByStatusTrue();

  // Verifica se existe outro cliente ativo com o mesmo email
  boolean existsByEmailAndStatusTrue(String email);

}
