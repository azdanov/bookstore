package dev.azdanov.webapp.clients.orders;

import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface OrderServiceClient {

    @PostExchange("/orders/api/v1/orders")
    OrderConfirmationResponse createOrder(@RequestBody CreateOrderRequest orderRequest);

    @GetExchange("/orders/api/v1/orders")
    List<OrderBrief> getOrders();

    @GetExchange("/orders/api/v1/orders/{orderNumber}")
    OrderDetails getOrder(@PathVariable String orderNumber);
}
