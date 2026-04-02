package com.juhmaran.customerapi.services;

import com.juhmaran.customerapi.entities.Customer;
import com.juhmaran.customerapi.exceptions.CustomerNotFoundException;
import com.juhmaran.customerapi.exceptions.EmailAlreadyExistsException;
import com.juhmaran.customerapi.mapper.CustomerMapper;
import com.juhmaran.customerapi.model.CustomerRequestDTO;
import com.juhmaran.customerapi.model.CustomerResponseDTO;
import com.juhmaran.customerapi.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Customer Service Implementation
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
  @Transactional
  public CustomerResponseDTO createCustomer(CustomerRequestDTO request) {
    if (customerRepository.existsByEmail(request.email())) {
      throw new EmailAlreadyExistsException("Email já cadastrado");
    }
    Customer customer = mapper.toEntity(request);
    Customer saved = customerRepository.save(customer);
    log.info("Cliente criado: {}", saved.getId());
    return mapper.toDTO(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public CustomerResponseDTO getCustomerById(UUID id) {
    Customer customer = customerRepository.findById(id)
      .orElseThrow(() -> new CustomerNotFoundException("Cliente não encontrado"));
    return mapper.toDTO(customer);
  }

  @Override
  @Transactional(readOnly = true)
  public List<CustomerResponseDTO> getAllCustomers() {
    return mapper.toDTOs(customerRepository.findAll());
  }

  @Override
  @Transactional
  public void updateCustomer(UUID id, CustomerRequestDTO request) {
    Customer customer = customerRepository.findById(id)
      .orElseThrow(() -> new CustomerNotFoundException("Cliente não encontrado"));

    // Verifica se o email existe para outro cliente
    if (request.email() != null &&
      !request.email().equals(customer.getEmail()) && // permite manter o mesmo email sem erro
      customerRepository.existsByEmail(request.email())) {
      throw new EmailAlreadyExistsException("Email já cadastrado"); // lança Exception
    }

    mapper.updateEntityFromDTO(request, customer);
    customerRepository.save(customer);
    log.info("Cliente atualizado: {}", id);
  }

  @Override
  @Transactional
  public void partialUpdateCustomer(UUID id, CustomerRequestDTO request) {
    Customer customer = customerRepository.findById(id)
      .orElseThrow(() -> new CustomerNotFoundException("Cliente não encontrado"));

    if (request.email() != null &&
      !request.email().equals(customer.getEmail()) &&
      customerRepository.existsByEmail(request.email())) {
      throw new EmailAlreadyExistsException("Email já cadastrado");
    }

    if (request.fullName() != null) customer.setFullName(request.fullName());
    if (request.email() != null) customer.setEmail(request.email());
    if (request.phone() != null) customer.setPhone(request.phone());

    customerRepository.save(customer);
    log.info("Cliente parcialmente atualizado: {}", id);
  }

  @Override
  @Transactional
  public void deleteCustomer(UUID id) {
    if (!customerRepository.existsById(id)) {
      throw new CustomerNotFoundException("Cliente não encontrado");
    }
    customerRepository.deleteById(id);
    log.info("Cliente deletado: {}", id);
  }

}
