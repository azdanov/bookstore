package dev.azdanov.orderservice.domain;

import dev.azdanov.orderservice.domain.models.CreateOrderRequest;
import dev.azdanov.orderservice.domain.models.OrderDetails;
import dev.azdanov.orderservice.domain.models.OrderItem;
import dev.azdanov.orderservice.domain.models.OrderStatus;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

class OrderMapper {

    private OrderMapper() {}

    static OrderEntity convertToEntity(CreateOrderRequest request) {
        OrderEntity order = new OrderEntity();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setStatus(OrderStatus.NEW);
        order.setCustomer(request.customer());
        order.setDeliveryAddress(request.deliveryAddress());

        Set<OrderItemEntity> orderItems = request.items().stream()
                .map(item -> {
                    OrderItemEntity orderItem = new OrderItemEntity();
                    orderItem.setCode(item.code());
                    orderItem.setName(item.name());
                    orderItem.setPrice(item.price());
                    orderItem.setQuantity(item.quantity());
                    orderItem.setOrder(order);
                    return orderItem;
                })
                .collect(Collectors.toSet());

        order.setItems(orderItems);

        return order;
    }

    public static OrderDetails convertToOverview(OrderEntity orderEntity) {
        return new OrderDetails(
                orderEntity.getOrderNumber(),
                orderEntity.getUserName(),
                orderEntity.getItems().stream()
                        .map(item -> new OrderItem(item.getCode(), item.getName(), item.getPrice(), item.getQuantity()))
                        .collect(Collectors.toSet()),
                orderEntity.getCustomer(),
                orderEntity.getDeliveryAddress(),
                orderEntity.getStatus(),
                orderEntity.getComments(),
                orderEntity.getCreatedAt());
    }
}
