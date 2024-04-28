package dev.azdanov.orderservice.domain.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record OrderItem(
        @NotBlank(message = "Order item code is required") String code,
        @NotBlank(message = "Order item name is required") String name,
        @NotNull(message = "Order item price is required") BigDecimal price,
        @NotNull(message = "Order item quantity is required") @Min(value = 1, message = "Order item quantity must be greater than 0")
                Integer quantity) {}
