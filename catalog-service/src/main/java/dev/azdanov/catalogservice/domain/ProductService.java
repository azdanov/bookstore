package dev.azdanov.catalogservice.domain;

import dev.azdanov.catalogservice.ApplicationProperties;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final ApplicationProperties properties;

    public ProductService(ProductRepository productRepository, ApplicationProperties properties) {
        this.productRepository = productRepository;
        this.properties = properties;
    }

    public PagedResult<Product> getProducts(int page) {
        Pageable pageable = getPageable(page);
        Page<Product> productsPage = productRepository.findAll(pageable).map(ProductMapper::toProduct);
        return PagedResult.from(productsPage);
    }

    public Optional<Product> getProductByCode(String code) {
        return productRepository.findByCode(code).map(ProductMapper::toProduct);
    }

    private Pageable getPageable(int page) {
        Sort sort = Sort.by("name").ascending();
        int validPage = Math.max(0, page - 1);
        return PageRequest.of(validPage, properties.pageSize(), sort);
    }
}
