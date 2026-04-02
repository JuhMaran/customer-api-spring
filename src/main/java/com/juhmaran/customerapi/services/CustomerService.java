package com.juhmaran.customerapi.services;

import com.juhmaran.customerapi.model.CustomerDTO;

import java.util.List;
import java.util.UUID;

/**
 * customer-api-spring
 *
 * @author Juliane Maran
 * @since 02/04/2026
 */
public interface CustomerService {

  CustomerDTO createCustomer(CustomerDTO customerDTO);

  CustomerDTO updateCustomer(UUID id, CustomerDTO customerDTO);

  void deleteCustomer(UUID id);

  CustomerDTO getCustomerById(UUID id);

  List<CustomerDTO> getAllCustomers();

}
