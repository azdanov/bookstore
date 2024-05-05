package dev.azdanov.webapp.web.controllers;

import dev.azdanov.webapp.clients.orders.CreateOrderRequest;
import dev.azdanov.webapp.clients.orders.OrderBrief;
import dev.azdanov.webapp.clients.orders.OrderConfirmationResponse;
import dev.azdanov.webapp.clients.orders.OrderDetails;
import dev.azdanov.webapp.clients.orders.OrderServiceClient;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
class OrderController {

    private final OrderServiceClient orderServiceClient;

    OrderController(OrderServiceClient orderServiceClient) {
        this.orderServiceClient = orderServiceClient;
    }

    @GetMapping("/cart")
    String cart() {
        return "cart";
    }

    @PostMapping("/api/orders")
    @ResponseBody
    OrderConfirmationResponse createOrder(@Valid @RequestBody CreateOrderRequest orderRequest) {
        return orderServiceClient.createOrder(orderRequest);
    }

    @GetMapping("/orders/{orderNumber}")
    String showOrderDetails(@PathVariable String orderNumber, Model model) {
        model.addAttribute("orderNumber", orderNumber);
        return "order_details";
    }

    @GetMapping("/api/orders/{orderNumber}")
    @ResponseBody
    OrderDetails getOrder(@PathVariable String orderNumber) {
        return orderServiceClient.getOrder(orderNumber);
    }

    @GetMapping("/orders")
    String showOrders() {
        return "orders";
    }

    @GetMapping("/api/orders")
    @ResponseBody
    List<OrderBrief> getOrders() {
        return orderServiceClient.getOrders();
    }
}
