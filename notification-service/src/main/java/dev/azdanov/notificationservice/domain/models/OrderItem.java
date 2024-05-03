package dev.azdanov.notificationservice.domain.models;

import java.math.BigDecimal;

public record OrderItem(String code, String name, BigDecimal price, Integer quantity) {}
