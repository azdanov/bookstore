package dev.azdanov.orderservice.domain.models;

import dev.azdanov.orderservice.domain.OrderEvent;

public enum OrderEventType {
    ORDER_CREATED(OrderCreatedEvent.class),
    ORDER_DELIVERED(OrderDeliveredEvent.class),
    ORDER_CANCELLED(OrderCancelledEvent.class),
    ORDER_PROCESSING_FAILED(OrderErrorEvent.class);

    private final Class<? extends OrderEvent> eventClass;

    OrderEventType(Class<? extends OrderEvent> eventClass) {
        this.eventClass = eventClass;
    }

    public Class<? extends OrderEvent> getEventClass() {
        return eventClass;
    }
}
