package com.juhmaran.customerapi.services;

import com.juhmaran.customerapi.entities.Customer;
import com.juhmaran.customerapi.mapper.CustomerMapper;
import com.juhmaran.customerapi.model.CustomerDTO;
import com.juhmaran.customerapi.repositories.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * customer-api-spring
 *
 * @author Juliane Maran
 * @since 02/04/2026
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

  private final CustomerRepository customerRepository;
  private final CustomerMapper mapper;

  @Override
  public CustomerDTO createCustomer(CustomerDTO customerDTO) {
    if (customerRepository.existsByEmail(customerDTO.email())) {
      throw new IllegalArgumentException("Email already exists");
    }
    Customer customer = mapper.customerDTOToCustomer(customerDTO);
    return mapper.customerToCustomerDTO(customerRepository.save(customer));
  }

  @Override
  public CustomerDTO updateCustomer(UUID id, CustomerDTO customerDTO) {
    Customer customer = customerRepository.findById(id)
      .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

    customer.setFullName(customerDTO.fullName());
    customer.setEmail(customerDTO.email());
    customer.setPhone(customerDTO.phone());
    customer.setStatus(customerDTO.status());

    return mapper.customerToCustomerDTO(customerRepository.save(customer));
  }

  @Override
  public void deleteCustomer(UUID id) {
    if (!customerRepository.existsById(id))
      throw new EntityNotFoundException("Customer not found");
    customerRepository.deleteById(id);
  }

  @Override
  public CustomerDTO getCustomerById(UUID id) {
    Customer customer = customerRepository.findById(id)
      .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
    return mapper.customerToCustomerDTO(customer);
  }

  @Override
  public List<CustomerDTO> getAllCustomers() {
    return customerRepository.findAll()
      .stream()
      .map(mapper::customerToCustomerDTO)
      .collect(Collectors.toList());
  }

}
