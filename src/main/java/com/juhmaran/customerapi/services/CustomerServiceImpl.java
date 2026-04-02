package com.juhmaran.customerapi.services;

import com.juhmaran.customerapi.entities.Customer;
import com.juhmaran.customerapi.exceptions.CustomerNotFoundException;
import com.juhmaran.customerapi.exceptions.DuplicateFieldException;
import com.juhmaran.customerapi.mapper.CustomerMapper;
import com.juhmaran.customerapi.model.CustomerRequestDTO;
import com.juhmaran.customerapi.model.CustomerResponseDTO;
import com.juhmaran.customerapi.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Customer Service Implementation with Logs and Caching
 *
 * @author Juliane Maran
 * @Cacheable → armazena o resultado da consulta
 * @CacheEvict → limpa o cache quando há atualização ou exclusão
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
  @CacheEvict(value = {"customers", "customersPage"}, allEntries = true)
  public CustomerResponseDTO createCustomer(CustomerRequestDTO request) {
    log.info("Creating customer with email: {}", request.email());
    if (customerRepository.existsByEmail(request.email())) {
      throw new DuplicateFieldException("Email already registered");
    }
    Customer customer = mapper.toEntity(request);
    customer.setStatus(true);
    Customer saved = customerRepository.save(customer);
    log.info("Creating customer: {}", saved.getId());
    return mapper.toDTO(saved);
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(value = "customers", key = "#id")
  public CustomerResponseDTO getCustomerById(UUID id) {
    Customer customer = customerRepository.findById(id)
      .orElseThrow(() -> new CustomerNotFoundException("Cliente não encontrado"));
    return mapper.toDTO(customer);
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(value = "customersPage", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
  public Page<CustomerResponseDTO> getAllCustomers(Pageable pageable) {
    Page<Customer> customersPage = customerRepository.findAllByStatusTrue(pageable);
    return customersPage.map(mapper::toDTO);
  }

  @Override
  @Transactional
  @CacheEvict(
    value = {"customers", "customersPage"},
    allEntries = true // Garantir consistência
  )
  public void updateCustomer(UUID id, CustomerRequestDTO request) {
    Customer customer = customerRepository.findById(id)
      .orElseThrow(() -> new CustomerNotFoundException("Cliente não encontrado"));

    if (request.email() != null &&
      !request.email().equals(customer.getEmail()) &&
      customerRepository.existsByEmail(request.email())) {
      throw new DuplicateFieldException("Email already in use");
    }

    mapper.updateEntityFromDTO(request, customer);
    customerRepository.save(customer);
    log.info("Cliente atualizado: {}", id);
  }

  @Override
  @Transactional
  @CacheEvict(value = {"customers", "customersPage"}, allEntries = true)
  public void partialUpdateCustomer(UUID id, CustomerRequestDTO request) {
    Customer customer = customerRepository.findById(id)
      .orElseThrow(() -> new CustomerNotFoundException("Cliente não encontrado"));

    if (request.email() != null &&
      !request.email().equals(customer.getEmail()) &&
      customerRepository.existsByEmail(request.email())) {
      throw new DuplicateFieldException("Email já cadastrado");
    }

    if (request.fullName() != null) customer.setFullName(request.fullName());
    if (request.email() != null) customer.setEmail(request.email());
    if (request.phone() != null) customer.setPhone(request.phone());

    customerRepository.save(customer);
    log.info("Cliente parcialmente atualizado: {}", id);
  }

  @Override
  @Transactional
  @CacheEvict(value = {"customers", "customersPage"}, allEntries = true)
  public void reactivateCustomer(UUID id) {
    Customer customer = customerRepository.findById(id)
      .orElseThrow(() -> new CustomerNotFoundException("Cliente não encontrado"));

    if (Boolean.TRUE.equals(customer.getStatus())) {
      log.warn("Cliente já está ativo: {}", id);
      return;
    }

    if (customerRepository.existsByEmailAndStatusTrue(customer.getEmail())) {
      throw new DuplicateFieldException("Não é possível reativar: email já cadastrado por outro cliente ativo");
    }

    customer.setStatus(true);
    customerRepository.save(customer);
    log.info("Cliente reativado (status=true): {}", id);
  }

  @Override
  @Transactional
  @CacheEvict(value = {"customers", "customersPage"}, allEntries = true)
  public void deactivateCustomer(UUID id) {
    Customer customer = customerRepository.findById(id)
      .orElseThrow(() -> new CustomerNotFoundException("Cliente não encontrado"));
    customer.setStatus(false);
    customerRepository.save(customer);
    log.info("Cliente desativado (status=false): {}", id);
  }

}
