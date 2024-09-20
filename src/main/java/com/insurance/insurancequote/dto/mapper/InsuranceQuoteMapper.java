package com.insurance.insurancequote.dto.mapper;

import com.insurance.insurancequote.dto.InsuranceQuoteDTO;
import com.insurance.insurancequote.entity.InsuranceQuote;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = CustomerMapper.class)
public interface InsuranceQuoteMapper {

    InsuranceQuoteMapper INSTANCE = Mappers.getMapper(InsuranceQuoteMapper.class);

    InsuranceQuote toEntity(InsuranceQuoteDTO dto);

    InsuranceQuoteDTO toDTO(InsuranceQuote entity);

}
