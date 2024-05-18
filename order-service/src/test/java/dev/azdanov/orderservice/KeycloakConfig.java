package dev.azdanov.orderservice;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;

@TestConfiguration(proxyBeanMethods = false)
class KeycloakConfig {

    @Bean
    KeycloakContainer keycloak(DynamicPropertyRegistry registry) {
        var keycloak =
                new KeycloakContainer("quay.io/keycloak/keycloak:24.0.4").withRealmImportFile("/keycloak/realm.json");
        registry.add(
                "spring.security.oauth2.resourceserver.jwt.issuer-uri",
                () -> keycloak.getAuthServerUrl() + "/realms/bookstore");
        return keycloak;
    }
}
