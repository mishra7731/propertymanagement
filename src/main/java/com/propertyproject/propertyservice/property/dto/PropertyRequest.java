package com.propertyproject.propertyservice.property.dto;

import com.propertyproject.propertyservice.property.PropertyType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PropertyRequest(
    @NotBlank @Size(max = 200) String name,
        @NotBlank @Size(max = 200) String addressLine1,
        @NotBlank @Size(max = 120) String city,
        @NotBlank @Size(min = 2, max = 2) String state,
        @NotBlank @Size(max = 10) String postalCode,
        @NotNull PropertyType propertyType,
        @NotNull @Positive Integer squareFeet,
        @Min(1800) @Max(2100) Short yearBuilt,
        LocalDate acquiredOn,
        @PositiveOrZero BigDecimal acquisitionPrice
) {}
