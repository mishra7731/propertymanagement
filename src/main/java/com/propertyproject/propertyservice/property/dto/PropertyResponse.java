package com.propertyproject.propertyservice.property.dto;

import com.propertyproject.propertyservice.property.PropertyType;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record PropertyResponse(
        Long id,
        String name,
        String addressLine1,
        String city,
        String state,
        String postalCode,
        PropertyType propertyType,
        Integer squareFeet,
        Short yearBuilt,
        LocalDate acquiredOn,
        BigDecimal acquisitionPrice,
        Instant createdAt,
        Instant updatedAt
) {}