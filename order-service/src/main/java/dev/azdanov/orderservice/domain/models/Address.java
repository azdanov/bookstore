package dev.azdanov.orderservice.domain.models;

import jakarta.validation.constraints.NotBlank;

public record Address(
        @NotBlank(message = "Address line 1 is required") String addressLine1,
        String addressLine2,
        @NotBlank(message = "Address city is required") String city,
        @NotBlank(message = "Address state is required") String state,
        @NotBlank(message = "Address zip code is required") String zipCode,
        @NotBlank(message = "Address country is required") String country) {}
