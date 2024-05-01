package dev.azdanov.orderservice.domain;

import dev.azdanov.orderservice.domain.models.CreateOrderRequest;
import dev.azdanov.orderservice.domain.models.CreateOrderResponse;
import dev.azdanov.orderservice.domain.models.OrderStatus;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private static final List<String> DELIVERY_ALLOWED_COUNTRIES = List.of("ESTONIA", "GERMANY");

    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;
    private final OrderEventService orderEventService;

    public OrderService(
            OrderRepository orderRepository, OrderValidator orderValidator, OrderEventService orderEventService) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
        this.orderEventService = orderEventService;
    }

    public CreateOrderResponse createOrder(String userName, CreateOrderRequest request) {
        orderValidator.validate(request);

        OrderEntity createOrder = OrderMapper.convertToEntity(request);
        createOrder.setUserName(userName);
        OrderEntity createdOrder = orderRepository.save(createOrder);

        publishEvent(createdOrder, OrderStatus.NEW);

        log.info("Created a new order {}", createdOrder.getOrderNumber());
        return new CreateOrderResponse(createdOrder.getOrderNumber());
    }

    public void processNewOrders() {
        List<OrderEntity> orders = orderRepository.findByStatus(OrderStatus.NEW);
        log.info("Found {} orders to process", orders.size());
        orders.forEach(this::processOrder);
    }

    private void processOrder(OrderEntity order) {
        try {
            if (canBeDelivered(order)) {
                updateOrderStatus(order, OrderStatus.DELIVERED);
            } else {
                String reason =
                        "Delivery to \"" + order.getDeliveryAddress().country() + "\" is temporarily unavailable.";
                updateOrderStatus(order, OrderStatus.CANCELLED, reason);
            }
        } catch (Exception e) {
            log.error("Failed to process order {}", order.getOrderNumber(), e);
            updateOrderStatus(order, OrderStatus.ERROR, e.getMessage());
        }
    }

    private void updateOrderStatus(OrderEntity order, OrderStatus status) {
        orderRepository.updateOrderStatus(order.getOrderNumber(), status);
        publishEvent(order, status);
    }

    private void updateOrderStatus(OrderEntity order, OrderStatus status, String reason) {
        orderRepository.updateOrderStatus(order.getOrderNumber(), status);
        publishEvent(order, status, reason);
    }

    private void publishEvent(OrderEntity order, OrderStatus status) {
        switch (status) {
            case NEW -> orderEventService.save(OrderEventMapper.buildOrderCreatedEvent(order));
            case DELIVERED -> orderEventService.save(OrderEventMapper.buildOrderDeliveredEvent(order));
            default -> log.warn("No event published for status {}", status);
        }
    }

    private void publishEvent(OrderEntity order, OrderStatus status, String reason) {
        switch (status) {
            case CANCELLED -> orderEventService.save(OrderEventMapper.buildOrderCancelledEvent(order, reason));
            case ERROR -> orderEventService.save(OrderEventMapper.buildOrderErrorEvent(order, reason));
            default -> log.warn("No event published for status {} with reason", status);
        }
    }

    private static boolean canBeDelivered(OrderEntity order) {
        return DELIVERY_ALLOWED_COUNTRIES.contains(
                order.getDeliveryAddress().country().toUpperCase());
    }
}
