package dev.azdanov.notificationservice.domain.models;

public interface OrderEvent {

    String eventId();

    String orderNumber();

    Customer customer();

    default String getReason() {
        return "";
    }

    default String getRecipientEmail() {
        return customer().email();
    }

    default String getRecipientName() {
        return customer().name();
    }
}
