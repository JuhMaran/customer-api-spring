package com.juhmaran.customerapi.services;

import com.juhmaran.customerapi.model.CustomerRequestDTO;
import com.juhmaran.customerapi.model.CustomerResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Interface Customer Service
 *
 * @author Juliane Maran
 * @since 02/04/2026
 */
public interface CustomerService {

  CustomerResponseDTO createCustomer(CustomerRequestDTO request);

  CustomerResponseDTO getCustomerById(UUID id);

  Page<CustomerResponseDTO> getAllCustomers(Pageable pageable);

  void updateCustomer(UUID id, CustomerRequestDTO request);

  void partialUpdateCustomer(UUID id, CustomerRequestDTO request);

  void reactivateCustomer(UUID id);

  void deactivateCustomer(UUID id);

}
