package dev.azdanov.orderservice.web.controllers;

import static dev.azdanov.orderservice.testdata.TestDataFactory.createOrderRequestWithInvalidCustomer;
import static dev.azdanov.orderservice.testdata.TestDataFactory.createValidOrderRequest;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

import dev.azdanov.orderservice.AbstractIntegrationTest;
import io.restassured.http.ContentType;
import java.math.BigDecimal;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class OrderControllerIntegrationTest extends AbstractIntegrationTest {

    @Nested
    class CreateOrderTest {
        @Test
        void shouldCreateOrderSuccessfully() {
            mockGetProductByCode("P100", "Product 1", new BigDecimal("25.50"));

            var payload = createValidOrderRequest();
            given().contentType(ContentType.JSON)
                    .body(payload)
                    .when()
                    .post("/api/v1/orders")
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("orderNumber", notNullValue());
        }

        @Test
        void shouldReturnBadRequestWhenMandatoryDataIsMissing() {
            var payload = createOrderRequestWithInvalidCustomer();
            given().contentType(ContentType.JSON)
                    .body(payload)
                    .when()
                    .post("/api/v1/orders")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }
}
