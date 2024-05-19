package dev.azdanov.notificationservice.events;

import dev.azdanov.notificationservice.domain.NotificationService;
import dev.azdanov.notificationservice.domain.NotificationTemplate;
import dev.azdanov.notificationservice.domain.OrderEventEntity;
import dev.azdanov.notificationservice.domain.OrderEventRepository;
import dev.azdanov.notificationservice.domain.models.OrderCancelledEvent;
import dev.azdanov.notificationservice.domain.models.OrderCreatedEvent;
import dev.azdanov.notificationservice.domain.models.OrderDeliveredEvent;
import dev.azdanov.notificationservice.domain.models.OrderErrorEvent;
import dev.azdanov.notificationservice.domain.models.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class OrderEventHandler {

    private static final Logger log = LoggerFactory.getLogger(OrderEventHandler.class);

    private final NotificationService notificationService;
    private final OrderEventRepository orderEventRepository;

    public OrderEventHandler(NotificationService notificationService, OrderEventRepository orderEventRepository) {
        this.notificationService = notificationService;
        this.orderEventRepository = orderEventRepository;
    }

    @RabbitListener(queues = "${notification.new-orders-queue}")
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        handleEvent(event, NotificationTemplate.ORDER_CREATED);
    }

    @RabbitListener(queues = "${notification.delivered-orders-queue}")
    public void handleOrderDeliveredEvent(OrderDeliveredEvent event) {
        handleEvent(event, NotificationTemplate.ORDER_DELIVERED);
    }

    @RabbitListener(queues = "${notification.cancelled-orders-queue}")
    public void handleOrderCancelledEvent(OrderCancelledEvent event) {
        handleEvent(event, NotificationTemplate.ORDER_CANCELLED);
    }

    @RabbitListener(queues = "${notification.error-orders-queue}")
    public void handleOrderErrorEvent(OrderErrorEvent event) {
        handleEvent(event, NotificationTemplate.ORDER_ERROR);
    }

    private void handleEvent(OrderEvent event, NotificationTemplate template) {
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("Received duplicate order event {}", event);
            return;
        }
        log.info("Received an order event {}", event);
        notificationService.sendNotification(event, template);
        var orderEvent = new OrderEventEntity(event.eventId());
        orderEventRepository.save(orderEvent);
    }
}
