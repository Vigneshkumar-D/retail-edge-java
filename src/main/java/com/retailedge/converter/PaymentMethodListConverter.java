package com.retailedge.converter;

import com.retailedge.enums.invoice.PaymentMethod;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class PaymentMethodListConverter implements AttributeConverter<List<PaymentMethod>, String> {

    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(List<PaymentMethod> paymentMethods) {
        if (paymentMethods == null || paymentMethods.isEmpty()) {
            return null;
        }
        // Join enum names into a single string
        return paymentMethods.stream()
                .map(PaymentMethod::name)
                .collect(Collectors.joining(DELIMITER));
    }

    @Override
    public List<PaymentMethod> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        // Split string back into a list of enums
        return Arrays.stream(dbData.split(DELIMITER))
                .map(PaymentMethod::valueOf)
                .collect(Collectors.toList());
    }
}
