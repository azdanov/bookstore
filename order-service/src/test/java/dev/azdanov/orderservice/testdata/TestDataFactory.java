package dev.azdanov.orderservice.testdata;

import static org.instancio.Select.field;

import dev.azdanov.orderservice.domain.models.Address;
import dev.azdanov.orderservice.domain.models.CreateOrderRequest;
import dev.azdanov.orderservice.domain.models.Customer;
import dev.azdanov.orderservice.domain.models.OrderItem;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.apache.logging.log4j.util.Strings;
import org.instancio.Instancio;

public class TestDataFactory {

    static final List<String> VALID_COUNTRIES = List.of("Estonia", "Germany");
    static final Set<OrderItem> VALID_ORDER_ITEMS =
            Set.of(new OrderItem("P100", "Product 1", new BigDecimal("25.50"), 1));

    public static CreateOrderRequest createValidOrderRequest() {
        return Instancio.of(CreateOrderRequest.class)
                .generate(field(Customer::email), gen -> gen.text().pattern("#a#a#a#a#a#a@example.com"))
                .set(field(CreateOrderRequest::items), VALID_ORDER_ITEMS)
                .generate(field(Address::country), gen -> gen.oneOf(VALID_COUNTRIES))
                .create();
    }

    public static CreateOrderRequest createOrderRequestWithInvalidCustomer() {
        return Instancio.of(CreateOrderRequest.class)
                .generate(field(Customer::email), gen -> gen.text().pattern("#c#c#c#c#d#d@example.com"))
                .set(field(Customer::phone), Strings.EMPTY)
                .generate(field(Address::country), gen -> gen.oneOf(VALID_COUNTRIES))
                .set(field(CreateOrderRequest::items), VALID_ORDER_ITEMS)
                .create();
    }

    public static CreateOrderRequest createOrderRequestWithInvalidDeliveryAddress() {
        return Instancio.of(CreateOrderRequest.class)
                .generate(field(Customer::email), gen -> gen.text().pattern("#c#c#c#c#d#d@example.com"))
                .set(field(Address::country), Strings.EMPTY)
                .set(field(CreateOrderRequest::items), VALID_ORDER_ITEMS)
                .create();
    }

    public static CreateOrderRequest createOrderRequestWithNoItems() {
        return Instancio.of(CreateOrderRequest.class)
                .generate(field(Customer::email), gen -> gen.text().pattern("#c#c#c#c#d#d@example.com"))
                .generate(field(Address::country), gen -> gen.oneOf(VALID_COUNTRIES))
                .set(field(CreateOrderRequest::items), Set.of())
                .create();
    }
}
