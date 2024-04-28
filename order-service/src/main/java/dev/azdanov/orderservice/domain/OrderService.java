package dev.azdanov.orderservice.domain;

import dev.azdanov.orderservice.domain.models.CreateOrderRequest;
import dev.azdanov.orderservice.domain.models.CreateOrderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(OrderRepository orderRepository, OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public CreateOrderResponse createOrder(String userName, CreateOrderRequest request) {
        orderValidator.validate(request);

        OrderEntity newOrder = OrderMapper.convertToEntity(request);
        newOrder.setUserName(userName);
        OrderEntity savedOrder = orderRepository.save(newOrder);

        log.info("Created Order with orderNumber={}", savedOrder.getOrderNumber());

        return new CreateOrderResponse(savedOrder.getOrderNumber());
    }
}
