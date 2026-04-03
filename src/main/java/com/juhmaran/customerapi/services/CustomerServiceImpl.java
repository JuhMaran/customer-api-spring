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
 * @since 02/04/2026
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

  public static final String CUSTOMER_NOT_FOUND = "Customer not found";
  public static final String EMAIL_ALREADY_REGISTERED = "Email already in use";
  public static final String EMAIL_ALREADY_ACTIVE = "Cannot reactivate: email already in use by another active customer";

  private final CustomerRepository customerRepository;
  private final CustomerMapper mapper;

  @Override
  @Transactional
  @CacheEvict(
    value = {"customers", "customersPage", "customersSearch"},
    allEntries = true
  )
  public CustomerResponseDTO createCustomer(CustomerRequestDTO request) {
    log.info("Creating customer with email: {}", request.email());

    if (customerRepository.existsByEmail(request.email())) {
      throw new DuplicateFieldException(EMAIL_ALREADY_REGISTERED);
    }

    Customer customer = mapper.toEntity(request);
    customer.setStatus(true);

    Customer saved = customerRepository.save(customer);

    log.info("Customer created. ID: {}", saved.getId());
    return mapper.toDTO(saved);
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(value = "customers", key = "#id")
  public CustomerResponseDTO getCustomerById(UUID id) {
    log.debug("Fetching customer by id: {}", id);

    Customer customer = customerRepository.findById(id)
      .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_NOT_FOUND));

    return mapper.toDTO(customer);
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(
    value = "customersPage",
    key = "#pageable.pageNumber + '-' + #pageable.pageSize"
  )
  public Page<CustomerResponseDTO> getAllCustomers(Pageable pageable) {
    log.debug("Fetching all active customers. Page: {}, Size: {}",
      pageable.getPageNumber(), pageable.getPageSize());

    Page<Customer> customersPage =
      customerRepository.findAllByStatusTrue(pageable);

    return customersPage.map(mapper::toDTO);
  }

  @Override
  @Transactional
  @CacheEvict(
    value = {"customers", "customersPage", "customersSearch"},
    allEntries = true
  )
  public void updateCustomer(UUID id, CustomerRequestDTO request) {
    log.info("Updating customer: {}", id);

    Customer customer = customerRepository.findById(id)
      .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_NOT_FOUND));

    if (request.email() != null &&
      !request.email().equals(customer.getEmail()) &&
      customerRepository.existsByEmail(request.email())) {
      throw new DuplicateFieldException(EMAIL_ALREADY_REGISTERED);
    }

    mapper.updateEntityFromDTO(request, customer);
    customerRepository.save(customer);

    log.info("Customer updated: {}", id);
  }

  @Override
  @Transactional
  @CacheEvict(
    value = {"customers", "customersPage", "customersSearch"},
    allEntries = true
  )
  public void partialUpdateCustomer(UUID id, CustomerRequestDTO request) {
    log.info("Partially updating customer: {}", id);

    Customer customer = customerRepository.findById(id)
      .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_NOT_FOUND));

    if (request.email() != null &&
      !request.email().equals(customer.getEmail()) &&
      customerRepository.existsByEmail(request.email())) {
      throw new DuplicateFieldException(EMAIL_ALREADY_REGISTERED);
    }

    if (request.fullName() != null) customer.setFullName(request.fullName());
    if (request.email() != null) customer.setEmail(request.email());
    if (request.phone() != null) customer.setPhone(request.phone());

    customerRepository.save(customer);

    log.info("Customer partially updated: {}", id);
  }

  @Override
  @Transactional
  @CacheEvict(
    value = {"customers", "customersPage", "customersSearch"},
    allEntries = true
  )
  public void reactivateCustomer(UUID id) {
    log.info("Reactivating customer: {}", id);

    Customer customer = customerRepository.findById(id)
      .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_NOT_FOUND));

    if (Boolean.TRUE.equals(customer.getStatus())) {
      log.warn("Customer already active: {}", id);
      return;
    }

    if (customerRepository.existsByEmailAndStatusTrue(customer.getEmail())) {
      throw new DuplicateFieldException(EMAIL_ALREADY_ACTIVE);
    }

    customer.setStatus(true);
    customerRepository.save(customer);

    log.info("Customer reactivated: {}", id);
  }

  @Override
  @Transactional
  @CacheEvict(
    value = {"customers", "customersPage", "customersSearch"},
    allEntries = true
  )
  public void deactivateCustomer(UUID id) {
    log.info("Deactivating customer: {}", id);

    Customer customer = customerRepository.findById(id)
      .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_NOT_FOUND));

    customer.setStatus(false);
    customerRepository.save(customer);

    log.info("Customer deactivated: {}", id);
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(
    value = "customersSearch",
    key = "#fullName + '-' + #email + '-' + #phone + '-' + #status + '-' + #pageable.pageNumber + '-' + #pageable.pageSize"
  )
  public Page<CustomerResponseDTO> searchCustomers(
    String fullName,
    String email,
    String phone,
    Boolean status,
    Pageable pageable) {

    String normalizedName = (fullName == null) ? null : fullName.trim().toLowerCase();
    String normalizedEmail = (email == null) ? null : email.trim().toLowerCase();
    String normalizedPhone = (phone == null) ? null : phone.replaceAll("\\D", "");

    log.info("Search - fullName: '{}', email: '{}', phone: '{}', status: {}, page: {}, size: {}",
      normalizedName, normalizedEmail, normalizedPhone, status,
      pageable.getPageNumber(), pageable.getPageSize());

    boolean noFilters =
      (normalizedName == null || normalizedName.isBlank()) &&
        (normalizedEmail == null || normalizedEmail.isBlank()) &&
        (normalizedPhone == null || normalizedPhone.isBlank());

    if (noFilters) {
      return filterByStatus(status, pageable);
    }

    Page<Customer> result = customerRepository.searchAdvanced(
      normalizedName,
      normalizedEmail,
      normalizedPhone,
      status,
      pageable
    );

    if (result.isEmpty()) {
      throw new CustomerNotFoundException(
        "No customers found for the provided search criteria"
      );
    }

    return result.map(mapper::toDTO);
  }

  private Page<CustomerResponseDTO> filterByStatus(Boolean status, Pageable pageable) {

    if (status == null || status) {
      return customerRepository.findAllByStatusTrue(pageable)
        .map(mapper::toDTO);
    }

    return customerRepository.findAllByStatusFalse(pageable)
      .map(mapper::toDTO);
  }

}
