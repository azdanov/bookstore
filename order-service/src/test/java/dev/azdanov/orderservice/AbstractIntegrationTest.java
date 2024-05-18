package dev.azdanov.orderservice;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.RestAssured;
import java.math.BigDecimal;
import java.util.Objects;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.keycloak.OAuth2Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.wiremock.integrations.testcontainers.WireMockContainer;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import({PostgresConfig.class, RabbitMqConfig.class, KeycloakConfig.class})
@AutoConfigureMockMvc
public abstract class AbstractIntegrationTest {

    static final String CLIENT_ID = "bookstore-webapp";
    static final String CLIENT_SECRET = "P1sibsIrELBhmvK18BOzw1bUl96DcP2z";
    static final String USERNAME = "azdanov";
    static final String PASSWORD = "azdanov";

    @Autowired
    OAuth2ResourceServerProperties oAuth2ResourceServerProperties;

    @LocalServerPort
    int port;

    static WireMockContainer wiremockServer = new WireMockContainer("wiremock/wiremock:3.5.2-alpine");

    @Autowired
    protected MockMvc mockMvc;

    @BeforeAll
    static void beforeAll() {
        wiremockServer.start();
        configureFor(wiremockServer.getHost(), wiremockServer.getPort());
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("orders.catalog-service-url", wiremockServer::getBaseUrl);
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    protected static void mockGetProductByCode(String code, String name, BigDecimal price) {
        String payload =
                """
                    {
                        "code": "%s",
                        "name": "%s",
                        "price": %f
                    }
                """
                        .formatted(code, name, price.doubleValue());

        stubFor(WireMock.get(urlMatching("/api/v1/products/" + code))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(200)
                        .withBody(payload)));
    }

    private final RestClient restClient = RestClient.builder().build();

    protected String getToken() {
        MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        request.add(OAuth2Constants.GRANT_TYPE, OAuth2Constants.PASSWORD);
        request.add(OAuth2Constants.CLIENT_ID, CLIENT_ID);
        request.add(OAuth2Constants.CLIENT_SECRET, CLIENT_SECRET);
        request.add(OAuth2Constants.USERNAME, USERNAME);
        request.add(OAuth2Constants.PASSWORD, PASSWORD);

        KeyCloakToken token = restClient
                .post()
                .uri(oAuth2ResourceServerProperties.getJwt().getIssuerUri() + "/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(request)
                .retrieve()
                .body(KeyCloakToken.class);

        return Objects.requireNonNull(token).accessToken();
    }

    record KeyCloakToken(@JsonProperty("access_token") String accessToken) {}
}
