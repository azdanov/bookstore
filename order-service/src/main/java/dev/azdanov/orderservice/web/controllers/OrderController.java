package dev.azdanov.orderservice.web.controllers;

import dev.azdanov.orderservice.domain.OrderNotFoundException;
import dev.azdanov.orderservice.domain.OrderService;
import dev.azdanov.orderservice.domain.SecurityService;
import dev.azdanov.orderservice.domain.models.CreateOrderRequest;
import dev.azdanov.orderservice.domain.models.CreateOrderResponse;
import dev.azdanov.orderservice.domain.models.OrderBrief;
import dev.azdanov.orderservice.domain.models.OrderDetails;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@SecurityRequirement(name = "security_auth")
class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;
    private final SecurityService securityService;

    OrderController(OrderService orderService, SecurityService securityService) {
        this.orderService = orderService;
        this.securityService = securityService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateOrderResponse createOrder(@Valid @RequestBody CreateOrderRequest request) {
        String userName = getUserName();
        log.info("Creating order: {} for user: {}", request, userName);
        return orderService.createOrder(userName, request);
    }

    @GetMapping
    List<OrderBrief> getOrders() {
        String userName = getUserName();
        log.info("Fetching orders for user: {}", userName);
        return orderService.findOrders(userName);
    }

    @GetMapping("/{orderNumber}")
    OrderDetails getOrder(@PathVariable String orderNumber) {
        String userName = getUserName();
        log.info("Fetching order: {} for user: {}", orderNumber, userName);
        return orderService
                .findUserOrder(userName, orderNumber)
                .orElseThrow(() -> new OrderNotFoundException(orderNumber));
    }

    private String getUserName() {
        return securityService.getLoginUserName().orElseThrow();
    }
}
