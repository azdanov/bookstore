package dev.azdanov.orderservice.domain.models;

/**
 * Representation of the various statuses that an order can have throughout its lifecycle.
 */
public enum OrderStatus {
    /**
     * Status indicating that the order has been newly created and is awaiting processing.
     */
    NEW,

    /**
     * Status indicating that the order is currently being processed.
     * Note: This status is not used at the moment but reserved for future use where more detailed tracking is required.
     */
    IN_PROCESS,

    /**
     * Status indicating that the order has been successfully delivered to the customer.
     */
    DELIVERED,

    /**
     * Status indicating that the order has been canceled.
     * This can occur for various reasons, such as issues with payment,
     * customer request, or inability to fulfill the order.
     */
    CANCELLED,

    /**
     * Status indicating that an error occurred during the processing of the order.
     * This status is used to flag orders that require attention due to issues that prevented normal processing.
     */
    ERROR
}
