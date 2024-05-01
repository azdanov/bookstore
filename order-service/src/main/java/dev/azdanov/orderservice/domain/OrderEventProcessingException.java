package dev.azdanov.orderservice.domain;

public class OrderEventProcessingException extends RuntimeException {

    public OrderEventProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
