package com.juhmaran.customerapi.mapper;

import com.juhmaran.customerapi.entities.Customer;
import com.juhmaran.customerapi.model.CustomerRequestDTO;
import com.juhmaran.customerapi.model.CustomerResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * customer-api-spring
 *
 * @author Juliane Maran
 * @since 02/04/2026
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerMapper {

  Customer toEntity(CustomerRequestDTO dto);

  CustomerResponseDTO toDTO(Customer entity);

  List<CustomerResponseDTO> toDTOs(List<Customer> entities);

  void updateEntityFromDTO(CustomerRequestDTO dto, @MappingTarget Customer entity);

}
