package dev.azdanov.notificationservice.domain.models;

public record Address(
        String addressLine1, String addressLine2, String city, String state, String zipCode, String country) {}
