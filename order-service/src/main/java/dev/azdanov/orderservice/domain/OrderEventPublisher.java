package dev.azdanov.orderservice.domain;

import dev.azdanov.orderservice.ApplicationProperties;
import dev.azdanov.orderservice.domain.models.OrderEvent;
import dev.azdanov.orderservice.domain.models.OrderEventType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
class OrderEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final ApplicationProperties properties;

    OrderEventPublisher(RabbitTemplate rabbitTemplate, ApplicationProperties properties) {
        this.rabbitTemplate = rabbitTemplate;
        this.properties = properties;
    }

    public void publish(OrderEvent event) {
        String routingKey = getRoutingKeyForEvent(event.getEventType());

        rabbitTemplate.convertAndSend(properties.orderEventsExchange(), routingKey, event);
    }

    private String getRoutingKeyForEvent(OrderEventType eventType) {
        return switch (eventType) {
            case ORDER_CREATED -> properties.newOrdersQueue();
            case ORDER_DELIVERED -> properties.deliveredOrdersQueue();
            case ORDER_CANCELLED -> properties.cancelledOrdersQueue();
            case ORDER_PROCESSING_FAILED -> properties.errorOrdersQueue();
        };
    }
}
