package dev.azdanov.webapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
class SecurityConfig {

    private final ClientRegistrationRepository clientRegistrationRepository;

    SecurityConfig(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(auth -> auth.requestMatchers(
                                "/",
                                "/error",
                                "/js/*",
                                "/css/*",
                                "/images/*",
                                "/webjars/**",
                                "/actuator/**",
                                "/products/**",
                                "/api/products/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .cors(CorsConfigurer::disable)
                .csrf(Customizer.withDefaults())
                .oauth2Login(Customizer.withDefaults())
                .logout(logout -> logout.clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .logoutSuccessHandler(oidcLogoutSuccessHandler()))
                .build();
    }

    private LogoutSuccessHandler oidcLogoutSuccessHandler() {
        OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler =
                new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
        oidcLogoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}");
        return oidcLogoutSuccessHandler;
    }
}
