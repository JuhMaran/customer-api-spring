package com.juhmaran.customerapi.mapper;

import com.juhmaran.customerapi.entities.Customer;
import com.juhmaran.customerapi.model.CustomerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * customer-api-spring
 *
 * @author Juliane Maran
 * @since 02/04/2026
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerMapper {

  CustomerDTO customerToCustomerDTO(Customer customer);

  @Mapping(target = "version", ignore = true)
  Customer customerDTOToCustomer(CustomerDTO customerDTO);

}
