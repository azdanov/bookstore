package dev.azdanov.webapp.clients.catalog;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface CatalogServiceClient {

    @GetExchange("/catalog/api/v1/products")
    PagedResult<Product> getProducts(@RequestParam int page);
}
