package dev.azdanov.orderservice.domain;

import dev.azdanov.orderservice.domain.models.OrderBrief;
import dev.azdanov.orderservice.domain.models.OrderStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByStatus(OrderStatus status);

    Optional<OrderEntity> findByOrderNumber(String orderNumber);

    default void updateOrderStatus(String orderNumber, OrderStatus status) {
        OrderEntity order = findByOrderNumber(orderNumber).orElseThrow();
        order.setStatus(status);
        save(order);
    }

    List<OrderBrief> findByUserName(String userName);

    @EntityGraph(attributePaths = {"items"})
    Optional<OrderEntity> findByUserNameAndOrderNumber(String userName, String orderNumber);
}
