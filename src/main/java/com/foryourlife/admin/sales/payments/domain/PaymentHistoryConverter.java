package com.foryourlife.admin.sales.payments.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class PaymentHistoryConverter implements AttributeConverter<PaymentHistory,String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(PaymentHistory attribute) {
        try{
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PaymentHistory convertToEntityAttribute(String dbData) {
        try{
            return objectMapper.readValue(dbData, PaymentHistory.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
