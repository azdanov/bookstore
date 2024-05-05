package dev.azdanov.webapp.clients.orders;

public record OrderConfirmationResponse(String orderNumber, OrderStatus status) {}
