package dev.azdanov.catalogservice.web.controllers;

import dev.azdanov.catalogservice.domain.PagedResult;
import dev.azdanov.catalogservice.domain.Product;
import dev.azdanov.catalogservice.domain.ProductNotFoundException;
import dev.azdanov.catalogservice.domain.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    PagedResult<Product> getProducts(@RequestParam(name = "page", defaultValue = "1") int page) {
        log.info("Getting products for page {}", page);
        return productService.getProducts(page);
    }

    @GetMapping("/{code}")
    Product getProductByCode(@PathVariable String code) {
        log.info("Getting product by code {}", code);
        return productService.getProductByCode(code).orElseThrow(() -> ProductNotFoundException.forCode(code));
    }
}
