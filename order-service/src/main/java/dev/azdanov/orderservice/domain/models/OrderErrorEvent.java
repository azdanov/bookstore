package dev.azdanov.orderservice.domain.models;

import java.time.LocalDateTime;
import java.util.Set;

public record OrderErrorEvent(
        String eventId,
        String orderNumber,
        Set<OrderItem> items,
        Customer customer,
        Address deliveryAddress,
        String reason,
        LocalDateTime createdAt)
        implements OrderEvent {

    @Override
    public OrderEventType getEventType() {
        return OrderEventType.ORDER_PROCESSING_FAILED;
    }
}
