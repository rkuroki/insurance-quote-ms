package com.insurance.insurancequote.dto.mapper;

import com.insurance.insurancequote.dto.CustomerDTO;
import com.insurance.insurancequote.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    @Mapping(target = "id", ignore = true)
    Customer toEntity(CustomerDTO dto);

    CustomerDTO toDTO(Customer entity);
}
