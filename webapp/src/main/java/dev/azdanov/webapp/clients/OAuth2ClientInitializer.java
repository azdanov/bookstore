package dev.azdanov.webapp.clients;

import java.util.Optional;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestInitializer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.AbstractOAuth2Token;

class OAuth2ClientInitializer implements ClientHttpRequestInitializer {

    private final OAuth2AuthorizedClientService authorizedClientService;

    OAuth2ClientInitializer(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @Override
    public void initialize(ClientHttpRequest request) {
        getOAuth2AuthenticationToken()
                .flatMap(this::getOAuth2AuthorizedClient)
                .map(OAuth2AuthorizedClient::getAccessToken)
                .map(AbstractOAuth2Token::getTokenValue)
                .ifPresent(request.getHeaders()::setBearerAuth);
    }

    private Optional<OAuth2AuthenticationToken> getOAuth2AuthenticationToken() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(OAuth2AuthenticationToken.class::isInstance)
                .map(OAuth2AuthenticationToken.class::cast);
    }

    private Optional<OAuth2AuthorizedClient> getOAuth2AuthorizedClient(OAuth2AuthenticationToken oauthToken) {
        return Optional.ofNullable(authorizedClientService.loadAuthorizedClient(
                oauthToken.getAuthorizedClientRegistrationId(), oauthToken.getName()));
    }
}
