package dev.azdanov.catalogservice.web.controllers;

import dev.azdanov.catalogservice.domain.PagedResult;
import dev.azdanov.catalogservice.domain.Product;
import dev.azdanov.catalogservice.domain.ProductNotFoundException;
import dev.azdanov.catalogservice.domain.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
class ProductController {

    private final ProductService productService;

    ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    PagedResult<Product> getProducts(@RequestParam(name = "page", defaultValue = "1") int page) {
        return productService.getProducts(page);
    }

    @GetMapping("/{code}")
    Product getProductByCode(@PathVariable String code) {
        return productService.getProductByCode(code).orElseThrow(() -> ProductNotFoundException.forCode(code));
    }
}
