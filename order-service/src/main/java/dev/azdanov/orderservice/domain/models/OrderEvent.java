package dev.azdanov.orderservice.domain.models;

import java.time.LocalDateTime;

public interface OrderEvent {

    String eventId();

    OrderEventType getEventType();

    String orderNumber();

    LocalDateTime createdAt();
}
