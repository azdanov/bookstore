package dev.azdanov.webapp.web.controllers;

import dev.azdanov.webapp.clients.orders.CreateOrderRequest;
import dev.azdanov.webapp.clients.orders.OrderBrief;
import dev.azdanov.webapp.clients.orders.OrderConfirmationResponse;
import dev.azdanov.webapp.clients.orders.OrderDetails;
import dev.azdanov.webapp.clients.orders.OrderServiceClient;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderServiceClient orderServiceClient;

    OrderController(OrderServiceClient orderServiceClient) {
        this.orderServiceClient = orderServiceClient;
    }

    @GetMapping("/cart")
    String cart() {
        log.info("Showing cart");
        return "cart";
    }

    @PostMapping("/api/orders")
    @ResponseBody
    OrderConfirmationResponse createOrder(@Valid @RequestBody CreateOrderRequest orderRequest) {
        log.info("Creating order: {}", orderRequest);
        return orderServiceClient.createOrder(orderRequest);
    }

    @GetMapping("/orders/{orderNumber}")
    String showOrderDetails(@PathVariable String orderNumber, Model model) {
        log.info("Showing order details for order number: {}", orderNumber);
        model.addAttribute("orderNumber", orderNumber);
        return "order_details";
    }

    @GetMapping("/api/orders/{orderNumber}")
    @ResponseBody
    OrderDetails getOrder(@PathVariable String orderNumber) {
        log.info("Getting order details for order number: {}", orderNumber);
        return orderServiceClient.getOrder(orderNumber);
    }

    @GetMapping("/orders")
    String showOrders() {
        log.info("Showing orders");
        return "orders";
    }

    @GetMapping("/api/orders")
    @ResponseBody
    List<OrderBrief> getOrders() {
        log.info("Getting orders");
        return orderServiceClient.getOrders();
    }
}
