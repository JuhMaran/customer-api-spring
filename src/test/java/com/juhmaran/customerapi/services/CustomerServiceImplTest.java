package com.juhmaran.customerapi.services;

import com.juhmaran.customerapi.entities.Customer;
import com.juhmaran.customerapi.exceptions.CustomerNotFoundException;
import com.juhmaran.customerapi.exceptions.DuplicateFieldException;
import com.juhmaran.customerapi.mapper.CustomerMapper;
import com.juhmaran.customerapi.model.CustomerRequestDTO;
import com.juhmaran.customerapi.model.CustomerResponseDTO;
import com.juhmaran.customerapi.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class CustomerServiceImplTest {

  @Mock
  CustomerRepository customerRepository;

  @Mock
  CustomerMapper customerMapper;

  @InjectMocks
  CustomerServiceImpl customerService;

  CustomerRequestDTO validRequest;
  Customer customerEntity;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    validRequest = CustomerRequestDTO.builder()
      .fullName("Ju Maran")
      .email("ju@test.com")
      .phone("+5511999999999")
      .build();

    customerEntity = Customer.builder()
      .id(UUID.randomUUID())
      .fullName(validRequest.fullName())
      .email(validRequest.email())
      .phone(validRequest.phone())
      .status(true)
      .registrationDate(LocalDateTime.now())
      .lastUpdate(LocalDateTime.now())
      .build();
  }

  @Test
  @DisplayName("Should create a new customer successfully")
  void shouldCreateCustomerSuccessfully() {
    when(customerRepository.existsByEmail(validRequest.email())).thenReturn(false);
    when(customerMapper.toEntity(validRequest)).thenReturn(customerEntity);
    when(customerRepository.save(customerEntity)).thenReturn(customerEntity);
    when(customerMapper.toDTO(customerEntity)).thenReturn(
      CustomerResponseDTO.builder()
        .id(customerEntity.getId())
        .fullName(customerEntity.getFullName())
        .email(customerEntity.getEmail())
        .phone(customerEntity.getPhone())
        .status(customerEntity.getStatus())
        .registrationDate(customerEntity.getRegistrationDate())
        .lastUpdate(customerEntity.getLastUpdate())
        .build()
    );

    CustomerResponseDTO responseDTO = customerService.createCustomer(validRequest);

    assertThat(responseDTO).isNotNull();
    assertThat(responseDTO.id()).isEqualTo(customerEntity.getId());
    verify(customerRepository).save(customerEntity);
  }

  @Test
  @DisplayName("Should throw DuplicateFieldException if email already exists")
  void shouldThrowWhenEmailAlreadyExists() {
    when(customerRepository.existsByEmail(validRequest.email())).thenReturn(true);

    assertThrows(DuplicateFieldException.class, () -> customerService.createCustomer(validRequest));
    verify(customerRepository, never()).save(any());
  }

  @Test
  @DisplayName("Should get customer by ID successfully")
  void shouldGetCustomerByIdSuccessfully() {
    when(customerRepository.findById(customerEntity.getId())).thenReturn(Optional.of(customerEntity));
    when(customerMapper.toDTO(customerEntity)).thenReturn(
      CustomerResponseDTO.builder()
        .id(customerEntity.getId())
        .fullName(customerEntity.getFullName())
        .email(customerEntity.getEmail())
        .phone(customerEntity.getPhone())
        .status(customerEntity.getStatus())
        .registrationDate(customerEntity.getRegistrationDate())
        .lastUpdate(customerEntity.getLastUpdate())
        .build()
    );

    CustomerResponseDTO response = customerService.getCustomerById(customerEntity.getId());

    assertThat(response.fullName()).isEqualTo(customerEntity.getFullName());
  }

  @Test
  @DisplayName("Should throw CustomerNotFoundException if customer not found by ID")
  void shouldThrowWhenCustomerNotFound() {
    when(customerRepository.findById(any())).thenReturn(Optional.empty());

    assertThrows(CustomerNotFoundException.class, () ->
      customerService.getCustomerById(UUID.randomUUID()));
  }

  @Test
  @DisplayName("Should get all active customers")
  void shouldGetAllActiveCustomers() {
    Page<Customer> page = new PageImpl<>(List.of(customerEntity));
    when(customerRepository.findAllByStatusTrue(PageRequest.of(0, 10))).thenReturn(page);
    when(customerMapper.toDTO(customerEntity)).thenReturn(
      CustomerResponseDTO.builder()
        .id(customerEntity.getId())
        .fullName(customerEntity.getFullName())
        .email(customerEntity.getEmail())
        .phone(customerEntity.getPhone())
        .status(customerEntity.getStatus())
        .registrationDate(customerEntity.getRegistrationDate())
        .lastUpdate(customerEntity.getLastUpdate())
        .build()
    );

    Page<CustomerResponseDTO> result = customerService.getAllCustomers(PageRequest.of(0, 10));

    assertThat(result.getContent()).hasSize(1);
    assertThat(result.getContent().get(0).email()).isEqualTo(customerEntity.getEmail());
  }

  @Test
  @DisplayName("Should update customer successfully")
  void shouldUpdateCustomerSuccessfully() {
    CustomerRequestDTO updateRequest = CustomerRequestDTO.builder()
      .fullName("Updated Name")
      .email("updated@test.com")
      .phone("+5511888888888")
      .build();

    when(customerRepository.findById(customerEntity.getId())).thenReturn(Optional.of(customerEntity));
    when(customerRepository.existsByEmail(updateRequest.email())).thenReturn(false);

    // Mock do mapper para atualizar a entidade corretamente
    doAnswer(invocation -> {
      CustomerRequestDTO dto = invocation.getArgument(0);
      Customer entity = invocation.getArgument(1);

      if (dto.fullName() != null) entity.setFullName(dto.fullName());
      if (dto.email() != null) entity.setEmail(dto.email());
      if (dto.phone() != null) entity.setPhone(dto.phone());

      return null;
    }).when(customerMapper).updateEntityFromDTO(any(CustomerRequestDTO.class), any(Customer.class));

    customerService.updateCustomer(customerEntity.getId(), updateRequest);

    ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);
    verify(customerRepository).save(captor.capture());
    assertThat(captor.getValue().getFullName()).isEqualTo("Updated Name");
    assertThat(captor.getValue().getEmail()).isEqualTo("updated@test.com");
    assertThat(captor.getValue().getPhone()).isEqualTo("+5511888888888");
  }

  @Test
  @DisplayName("Should not allow updating to an email that already exists")
  void shouldThrowOnDuplicateEmailDuringUpdate() {
    CustomerRequestDTO updateRequest = CustomerRequestDTO.builder()
      .fullName("Updated Name")
      .email("existing@test.com")
      .phone("+5511888888888")
      .build();

    when(customerRepository.findById(customerEntity.getId())).thenReturn(Optional.of(customerEntity));
    when(customerRepository.existsByEmail(updateRequest.email())).thenReturn(true);

    assertThrows(DuplicateFieldException.class, () ->
      customerService.updateCustomer(customerEntity.getId(), updateRequest));
  }

  @Test
  @DisplayName("Should partially update customer successfully")
  void shouldPartialUpdateCustomerSuccessfully() {
    CustomerRequestDTO updateRequest = CustomerRequestDTO.builder()
      .fullName("Partial Updated Name")
      .email(null) // não atualiza
      .phone("+5511777777777")
      .build();

    when(customerRepository.findById(customerEntity.getId())).thenReturn(Optional.of(customerEntity));

    customerService.partialUpdateCustomer(customerEntity.getId(), updateRequest);

    ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);
    verify(customerRepository).save(captor.capture());
    Customer updated = captor.getValue();

    assertThat(updated.getFullName()).isEqualTo("Partial Updated Name");
    assertThat(updated.getPhone()).isEqualTo("+5511777777777");
    assertThat(updated.getEmail()).isEqualTo(customerEntity.getEmail()); // não foi alterado
  }

  @Test
  @DisplayName("Should deactivate and reactivate customer correctly")
  void shouldDeactivateAndReactivateCustomer() {
    when(customerRepository.findById(customerEntity.getId())).thenReturn(Optional.of(customerEntity));
    when(customerRepository.existsByEmailAndStatusTrue(customerEntity.getEmail())).thenReturn(false);

    customerService.deactivateCustomer(customerEntity.getId());
    assertThat(customerEntity.getStatus()).isFalse();

    customerService.reactivateCustomer(customerEntity.getId());
    assertThat(customerEntity.getStatus()).isTrue();
  }

  @Test
  @DisplayName("Should throw DuplicateFieldException when reactivating if email already exists for active customer")
  void shouldThrowOnDuplicateEmailWhenReactivating() {
    when(customerRepository.findById(customerEntity.getId())).thenReturn(Optional.of(customerEntity));
    when(customerRepository.existsByEmailAndStatusTrue(customerEntity.getEmail())).thenReturn(true);

    customerEntity.setStatus(false);
    assertThrows(DuplicateFieldException.class, () ->
      customerService.reactivateCustomer(customerEntity.getId()));
  }

}