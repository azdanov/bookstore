package dev.azdanov.orderservice.web.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.azdanov.orderservice.AbstractIntegrationTest;
import dev.azdanov.orderservice.WithMockOAuth2User;
import org.junit.jupiter.api.Test;

class GetOrdersTests extends AbstractIntegrationTest {

    @Test
    @WithMockOAuth2User(username = "user")
    void shouldGetOrdersSuccessfully() throws Exception {
        mockMvc.perform(get("/api/v1/orders")).andExpect(status().isOk());
    }
}
