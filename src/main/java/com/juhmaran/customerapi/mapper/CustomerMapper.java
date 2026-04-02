package com.juhmaran.customerapi.mapper;

import com.juhmaran.customerapi.entities.Customer;
import com.juhmaran.customerapi.model.CustomerRequestDTO;
import com.juhmaran.customerapi.model.CustomerResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

/**
 * Customer Mapper
 *
 * @author Juliane Maran
 * @since 02/04/2026
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerMapper {

  @Mapping(target = "version", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "registrationDate", ignore = true)
  @Mapping(target = "lastUpdate", ignore = true)
  @Mapping(target = "id", ignore = true)
  Customer toEntity(CustomerRequestDTO dto);

  CustomerResponseDTO toDTO(Customer entity);

  @Mapping(target = "version", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "registrationDate", ignore = true)
  @Mapping(target = "lastUpdate", ignore = true)
  @Mapping(target = "id", ignore = true)
  void updateEntityFromDTO(CustomerRequestDTO dto, @MappingTarget Customer entity);

}
