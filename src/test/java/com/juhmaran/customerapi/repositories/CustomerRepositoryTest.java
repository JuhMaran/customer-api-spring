package com.juhmaran.customerapi.repositories;

import com.juhmaran.customerapi.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class CustomerRepositoryTest {

  @Autowired
  CustomerRepository customerRepository;

  Customer activeCustomer;
  Customer inactiveCustomer;

  @BeforeEach
  void setUp() {
    customerRepository.deleteAll(); // garante banco limpo antes de cada teste

    activeCustomer = Customer.builder()
      .fullName("Active Customer")
      .email("active@test.com")
      .status(true)
      .build();

    inactiveCustomer = Customer.builder()
      .fullName("Inactive Customer")
      .email("inactive@test.com")
      .status(false)
      .build();

    customerRepository.save(activeCustomer);
    customerRepository.save(inactiveCustomer);

  }

  @Test
  @DisplayName("Deve salvar um cliente no banco de dados")
  void shouldSaveCustomer() {
    Customer customer = Customer.builder()
      .fullName("Ju Test")
      .email("ju@test.com")
      .status(true)
      .build();

    Customer saved = customerRepository.save(customer);

    assertThat(saved.getId()).isNotNull();
    assertThat(saved.getEmail()).isEqualTo("ju@test.com");
    assertThat(saved.getStatus()).isTrue();
  }

  @Test
  @DisplayName("Deve verificar se o email existe")
  void shouldCheckIfEmailExists() {
    boolean exists = customerRepository.existsByEmail("active@test.com");
    boolean noExists = customerRepository.existsByEmail("naoexiste@test.com");

    assertThat(exists).isTrue();
    assertThat(noExists).isFalse();
  }

  @Test
  @DisplayName("Deve listar apenas clientes ativos")
  void shouldListActiveCustomers() {
    Page<Customer> activeCustomers = customerRepository.findAllByStatusTrue(PageRequest.of(0, 10));

    assertThat(activeCustomers.getContent())
      .hasSize(1)
      .extracting("email")
      .containsExactly("active@test.com");
  }

  @Test
  @DisplayName("Deve verificar se email existe e está ativo")
  void shouldCheckIfEmailExistsAndIsActive() {
    boolean existsActive = customerRepository.existsByEmailAndStatusTrue("active@test.com");
    boolean existsInactive = customerRepository.existsByEmailAndStatusTrue("inactive@test.com");

    assertThat(existsActive).isTrue();
    assertThat(existsInactive).isFalse();
  }

}