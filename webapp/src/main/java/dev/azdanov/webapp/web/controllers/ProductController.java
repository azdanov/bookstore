package dev.azdanov.webapp.web.controllers;

import dev.azdanov.webapp.clients.catalog.CatalogServiceClient;
import dev.azdanov.webapp.clients.catalog.PagedResult;
import dev.azdanov.webapp.clients.catalog.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final CatalogServiceClient catalogService;

    ProductController(CatalogServiceClient catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping
    String index() {
        log.info("Redirecting to /products");
        return "redirect:/products";
    }

    @GetMapping("/products")
    String showProductsPage(@RequestParam(name = "page", defaultValue = "1") int page, Model model) {
        log.info("Showing products page: {}", page);
        model.addAttribute("page", page);
        return "products";
    }

    @GetMapping("/api/products")
    @ResponseBody
    PagedResult<Product> products(@RequestParam(name = "page", defaultValue = "1") int page) {
        log.info("Getting products for page: {}", page);
        return catalogService.getProducts(page);
    }
}
