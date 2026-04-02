package com.juhmaran.customerapi.services;

import com.juhmaran.customerapi.model.CustomerRequestDTO;
import com.juhmaran.customerapi.model.CustomerResponseDTO;

import java.util.List;
import java.util.UUID;

/**
 * customer-api-spring
 *
 * @author Juliane Maran
 * @since 02/04/2026
 */
public interface CustomerService {

  CustomerResponseDTO createCustomer(CustomerRequestDTO request);

  CustomerResponseDTO getCustomerById(UUID id);

  List<CustomerResponseDTO> getAllCustomers();

  void updateCustomer(UUID id, CustomerRequestDTO request);

  void partialUpdateCustomer(UUID id, CustomerRequestDTO request);

  void deleteCustomer(UUID id);

  void deactivateCustomer(UUID id);

}
