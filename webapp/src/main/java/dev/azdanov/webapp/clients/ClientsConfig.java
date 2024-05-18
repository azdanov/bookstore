package dev.azdanov.webapp.clients;

import dev.azdanov.webapp.ApplicationProperties;
import dev.azdanov.webapp.clients.catalog.CatalogServiceClient;
import dev.azdanov.webapp.clients.orders.OrderServiceClient;
import java.time.Duration;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
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
    RestClientCustomizer restClientCustomizer(OAuth2AuthorizedClientService authorizedClientService) {
        return restClientBuilder -> restClientBuilder
                .baseUrl(properties.apiGatewayUrl())
                .requestInitializer(new OAuth2ClientInitializer(authorizedClientService))
                .requestFactory(ClientHttpRequestFactories.get(ClientHttpRequestFactorySettings.DEFAULTS
                        .withConnectTimeout(Duration.ofSeconds(5))
                        .withReadTimeout(Duration.ofSeconds(5))));
    }

    @Bean
    CatalogServiceClient catalogServiceClient(RestClient.Builder restClientBuilder) {
        return createServiceClient(restClientBuilder.build(), CatalogServiceClient.class);
    }

    @Bean
    OrderServiceClient orderServiceClient(RestClient.Builder restClientBuilder) {
        return createServiceClient(restClientBuilder.build(), OrderServiceClient.class);
    }

    private <T> T createServiceClient(RestClient restClient, Class<T> clientClass) {
        return HttpServiceProxyFactory.builder()
                .exchangeAdapter(RestClientAdapter.create(restClient))
                .build()
                .createClient(clientClass);
    }
}
