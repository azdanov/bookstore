package dev.azdanov.notificationservice.domain;

public enum NotificationTemplate {
    ORDER_CREATED(
            "Order Created Notification",
            """
        ===================================================
        Order Created Notification
        ----------------------------------------------------
        Dear %s,
        Your order with orderNumber: %s has been created successfully.

        Kind regards,
        Bookstore Team
        ===================================================
        """),
    ORDER_DELIVERED(
            "Order Delivered Notification",
            """
        ===================================================
        Order Delivered Notification
        ----------------------------------------------------
        Dear %s,
        Your order with orderNumber: %s has been delivered successfully.

        Kind regards,
        Bookstore Team
        ===================================================
        """),
    ORDER_CANCELLED(
            "Order Cancelled Notification",
            """
        ===================================================
        Order Cancelled Notification
        ----------------------------------------------------
        Dear %s,
        Your order with orderNumber: %s has been cancelled.
        Reason: %s

        Kind regards,
        Bookstore Team
        ===================================================
        """),
    ORDER_ERROR(
            "Order Processing Failure Notification",
            """
        ===================================================
        Order Processing Failure Notification
        ----------------------------------------------------
        Hi Team,
        Please inform the customer: %s, that the order with
        orderNumber: %s has failed.
        Reason: %s


        Kind regards,
        Bookstore Team
        ===================================================
        """);

    private final String title;
    private final String messageBody;

    NotificationTemplate(String title, String messageBody) {
        this.title = title;
        this.messageBody = messageBody;
    }

    public String getTitle() {
        return title;
    }

    public String getMessageBody() {
        return messageBody;
    }
}
