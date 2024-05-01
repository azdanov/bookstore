package dev.azdanov.orderservice.domain;

import dev.azdanov.orderservice.domain.models.OrderEventType;
import java.time.LocalDateTime;

public interface OrderEvent {

    String eventId();

    OrderEventType getEventType();

    String orderNumber();

    LocalDateTime createdAt();
}
