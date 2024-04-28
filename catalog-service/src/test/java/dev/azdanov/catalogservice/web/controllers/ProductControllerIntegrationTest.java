package dev.azdanov.catalogservice.web.controllers;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import dev.azdanov.catalogservice.AbstractIntegrationTest;
import dev.azdanov.catalogservice.domain.Product;
import io.restassured.http.ContentType;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "/scripts/INIT_PRODUCTS.sql")
class ProductControllerIntegrationTest extends AbstractIntegrationTest {

    @Test
    void products_whenNoParams_shouldReturnFirstPage() {
        given().contentType(ContentType.JSON)
                .when()
                .get("/api/v1/products")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("data", hasSize(10))
                .body("totalElements", is(15))
                .body("pageNumber", is(1))
                .body("totalPages", is(2))
                .body("isFirst", is(true))
                .body("isLast", is(false))
                .body("hasNext", is(true))
                .body("hasPrevious", is(false));
    }

    @Test
    void productsCode_whenCodeExists_shouldReturnProduct() {
        Product product = given().contentType(ContentType.JSON)
                .when()
                .get("/api/v1/products/{code}", "P100")
                .then()
                .statusCode(HttpStatus.OK.value())
                .assertThat()
                .extract()
                .body()
                .as(Product.class);

        assertThat(product.code()).isEqualTo("P100");
        assertThat(product.name()).isEqualTo("The Hunger Games");
        assertThat(product.description()).isEqualTo("Winning will make you famous. Losing means certain death...");
        assertThat(product.price()).isEqualTo(new BigDecimal("34.0"));
    }

    @Test
    void productsCode_whenCodeNotExists_shouldReturn404() {
        String code = "invalid_product_code";
        given().contentType(ContentType.JSON)
                .when()
                .get("/api/v1/products/{code}", code)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("status", is(HttpStatus.NOT_FOUND.value()))
                .body("title", is("Product Not Found"))
                .body("detail", is("Product with code " + code + " not found"));
    }
}
