package com.juhmaran.customerapi.controllers;

import com.juhmaran.customerapi.model.CustomerRequestDTO;
import com.juhmaran.customerapi.model.CustomerResponseDTO;
import com.juhmaran.customerapi.services.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Customer Controller
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
  public ResponseEntity<Void> createCustomer(@Valid @RequestBody CustomerRequestDTO dto) {
    CustomerResponseDTO created = customerService.createCustomer(dto);

    HttpHeaders headers = new HttpHeaders();
    headers.add("Location", "/api/v1/customers/" + created.id());

    return new ResponseEntity<>(headers, HttpStatus.CREATED);
  }

  @GetMapping("/{customerId}")
  public ResponseEntity<CustomerResponseDTO> getById(@PathVariable UUID customerId) {
    return ResponseEntity.ok(customerService.getCustomerById(customerId));
  }

  @GetMapping
  public ResponseEntity<Page<CustomerResponseDTO>> listAllCustomers(
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = "10") int size) {

    Page<CustomerResponseDTO> customers =
      customerService.getAllCustomers(PageRequest.of(page, size));

    return ResponseEntity.ok(customers);
  }

  @PutMapping("/{customerId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateCustomer(@PathVariable UUID customerId,
                             @Valid @RequestBody CustomerRequestDTO dto) {
    customerService.updateCustomer(customerId, dto);
  }

  @PatchMapping("/{customerId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void partialUpdateCustomer(@PathVariable UUID customerId,
                                    @RequestBody CustomerRequestDTO dto) {
    customerService.partialUpdateCustomer(customerId, dto);
  }

  @PatchMapping("/{customerId}/deactivate")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deactivateCustomer(@PathVariable UUID customerId) {
    customerService.deactivateCustomer(customerId);
  }

  @PatchMapping("/{customerId}/reactivate")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void reactivateCustomer(@PathVariable UUID customerId) {
    customerService.reactivateCustomer(customerId);
  }

  @GetMapping("/search")
  public ResponseEntity<Page<CustomerResponseDTO>> searchCustomers(
    @RequestParam(value = "fullName", required = false) String fullName,
    @RequestParam(value = "email", required = false) String email,
    @RequestParam(value = "phone", required = false) String phone,
    @RequestParam(value = "status", required = false) Boolean status,
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = "10") int size) {

    Page<CustomerResponseDTO> result =
      customerService.searchCustomers(fullName, email, phone, status, PageRequest.of(page, size));

    return ResponseEntity.ok(result);
  }

}
