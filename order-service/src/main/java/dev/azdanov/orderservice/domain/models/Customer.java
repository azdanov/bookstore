package dev.azdanov.orderservice.domain.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record Customer(
        @NotBlank(message = "Customer name is required") String name,
        @NotBlank(message = "Customer email is required") @Email(message = "Customer email must be well-formed")
                String email,
        @NotBlank(message = "Customer phone number is required") String phone) {}
