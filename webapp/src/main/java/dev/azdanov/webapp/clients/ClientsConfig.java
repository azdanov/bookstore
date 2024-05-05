package dev.azdanov.webapp.clients;

import dev.azdanov.webapp.ApplicationProperties;
import dev.azdanov.webapp.clients.catalog.CatalogServiceClient;
import dev.azdanov.webapp.clients.orders.OrderServiceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
class ClientsConfig {

    private final ApplicationProperties properties;

    ClientsConfig(ApplicationProperties properties) {
        this.properties = properties;
    }

    @Bean
    CatalogServiceClient catalogServiceClient(RestClient.Builder restClientBuilder) {
        RestClient restClient =
                restClientBuilder.baseUrl(properties.apiGatewayUrl()).build();
        RestClientAdapter exchangeAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory =
                HttpServiceProxyFactory.builderFor(exchangeAdapter).build();
        return factory.createClient(CatalogServiceClient.class);
    }

    @Bean
    OrderServiceClient orderServiceClient(RestClient.Builder restClientBuilder) {
        RestClient restClient =
                restClientBuilder.baseUrl(properties.apiGatewayUrl()).build();
        RestClientAdapter exchangeAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory =
                HttpServiceProxyFactory.builderFor(exchangeAdapter).build();
        return factory.createClient(OrderServiceClient.class);
    }
}
