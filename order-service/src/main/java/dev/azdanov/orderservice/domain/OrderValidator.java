package dev.azdanov.orderservice.domain;

import dev.azdanov.orderservice.clients.catalog.Product;
import dev.azdanov.orderservice.clients.catalog.ProductServiceClient;
import dev.azdanov.orderservice.domain.models.CreateOrderRequest;
import dev.azdanov.orderservice.domain.models.OrderItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
class OrderValidator {

    private static final Logger log = LoggerFactory.getLogger(OrderValidator.class);

    private final ProductServiceClient client;

    OrderValidator(ProductServiceClient client) {
        this.client = client;
    }

    void validate(CreateOrderRequest request) {
        request.items().forEach(this::validateOrderItem);
    }

    private void validateOrderItem(OrderItem item) {
        Product product = getProductByCode(item.code());
        validateProductPrice(item, product);
    }

    private Product getProductByCode(String code) {
        return client.findProductByCode(code)
                .orElseThrow(() -> new InvalidOrderException("Invalid product code: " + code));
    }

    private void validateProductPrice(OrderItem item, Product product) {
        if (item.price().compareTo(product.price()) != 0) {
            log.error(
                    "Product price not matching. Actual price: {}, received price: {}", product.price(), item.price());
            throw new InvalidOrderException("Product price not matching");
        }
    }
}
