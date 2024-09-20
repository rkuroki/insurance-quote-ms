package com.insurance.insurancequote.dto.mapper;

import com.insurance.insurancequote.dto.CustomerDTO;
import com.insurance.insurancequote.entity.Customer;
import org.mapstruct.factory.Mappers;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class CustomerMapperTest {

    private final CustomerMapper mapper = Mappers.getMapper(CustomerMapper.class);

    @Test
    public void testToEntity() {
        CustomerDTO dto = new CustomerDTO();
        dto.setDocumentNumber("36205578900");
        dto.setName("John Doe");
        dto.setType("NATURAL");
        dto.setGender("MALE");
        dto.setDateOfBirth(LocalDate.of(1999, 1, 31));
        dto.setEmail("johnwick@gmail.com");
        dto.setPhoneNumber("11950503030");

        Customer entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getDocumentNumber(), entity.getDocumentNumber());
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getType(), entity.getType());
        assertEquals(dto.getGender(), entity.getGender());
        assertEquals(dto.getDateOfBirth(), entity.getDateOfBirth());
        assertEquals(dto.getEmail(), entity.getEmail());
        assertEquals(dto.getPhoneNumber(), entity.getPhoneNumber());
    }

    @Test
    public void testToDTO() {
        Customer entity = new Customer();
        entity.setDocumentNumber("59409504950");
        entity.setName("John Doe");
        entity.setType("ARTIFICIAL");
        entity.setGender("FEMALE");
        entity.setDateOfBirth(LocalDate.of(1809, 12, 30));
        entity.setEmail("mister@test.com");
        entity.setPhoneNumber("11950543432");

        CustomerDTO dto = mapper.toDTO(entity);

        assertNotNull(dto);
        assertEquals(entity.getDocumentNumber(), dto.getDocumentNumber());
        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getType(), dto.getType());
        assertEquals(entity.getGender(), dto.getGender());
        assertEquals(entity.getDateOfBirth(), dto.getDateOfBirth());
        assertEquals(entity.getEmail(), dto.getEmail());
        assertEquals(entity.getPhoneNumber(), dto.getPhoneNumber());
    }

}
