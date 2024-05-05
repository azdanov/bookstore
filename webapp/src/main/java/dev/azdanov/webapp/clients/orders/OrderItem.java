package dev.azdanov.webapp.clients.orders;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

public record OrderItem(
        @NotBlank(message = "Order code is required") String code,
        @NotBlank(message = "Order name is required") String name,
        @NotNull(message = "Order price is required") BigDecimal price,
        @NotNull(message = "Order quantity is required") @Min(value = 1, message = "Order quantity must be greater than 0")
                Integer quantity)
        implements Serializable {}
