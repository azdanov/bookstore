package dev.azdanov.catalogservice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dev.azdanov.catalogservice.AbstractJpaTest;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "/scripts/INIT_PRODUCTS.sql")
class ProductRepositoryJpaTest extends AbstractJpaTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void findByCode_whenProductExists_shouldReturnProduct() {
        ProductEntity product = productRepository.findByCode("P100").orElseThrow();
        assertThat(product.getCode()).isEqualTo("P100");
        assertThat(product.getName()).isEqualTo("The Hunger Games");
        assertThat(product.getDescription()).isEqualTo("Winning will make you famous. Losing means certain death...");
        assertThat(product.getPrice()).isEqualTo(new BigDecimal("34.0"));
    }

    @Test
    void findByCode_whenProductNotExists_shouldReturnEmpty() {
        assertThat(productRepository.findByCode("invalid_product_code")).isEmpty();
    }
}
