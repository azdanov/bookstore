package dev.azdanov.orderservice.clients.catalog;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ProductServiceClient {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceClient.class);

    private static final String CATALOG_SERVICE = "catalog-service";

    private final RestClient restClient;

    ProductServiceClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @CircuitBreaker(name = CATALOG_SERVICE)
    @Retry(name = CATALOG_SERVICE, fallbackMethod = "findProductByCodeFallback")
    public Optional<Product> findProductByCode(String code) {
        log.info("Fetching product for code: {}", code);
        return Optional.ofNullable(fetchProductByCode(code));
    }

    private Product fetchProductByCode(String code) {
        return restClient.get().uri("/api/v1/products/{code}", code).retrieve().body(Product.class);
    }

    Optional<Product> findProductByCodeFallback(String code, Throwable t) {
        log.warn("Fallback triggered for getProductByCode. Code: {}, Error: {}", code, t.getMessage());
        return Optional.empty();
    }
}
