package com.juhmaran.customerapi.controllers;

import com.juhmaran.customerapi.model.CustomerDTO;
import com.juhmaran.customerapi.services.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * customer-api-spring
 *
 * @author Juliane Maran
 * @since 02/04/2026
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

  private final CustomerService customerService;

  @PostMapping
  public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO request) {
    return ResponseEntity.ok(customerService.createCustomer(request));
  }

  @PutMapping("/{customerId}")
  public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable UUID customerId,
                                                    @Valid @RequestBody CustomerDTO request) {
    return ResponseEntity.ok(customerService.updateCustomer(customerId, request));
  }

  @DeleteMapping("/{customerId}")
  public ResponseEntity<Void> deleteCustomer(@PathVariable UUID customerId) {
    customerService.deleteCustomer(customerId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{customerId}")
  public ResponseEntity<CustomerDTO> getCustomer(@PathVariable UUID customerId) {
    return ResponseEntity.ok(customerService.getCustomerById(customerId));
  }

  @GetMapping
  public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
    return ResponseEntity.ok(customerService.getAllCustomers());
  }

}
