package com.algaworks.algashop.authorizationserver.infrastructure.security.oidc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.authorization.oidc.web.authentication.OidcLogoutAuthenticationSuccessHandler;

@Configuration
public class OidcLogoutSuccessHandlerConfig {

    @Bean
    public OidcLogoutAuthenticationSuccessHandler oidcLogoutAuthenticationSuccessHandler(
            OidcRevokeAuthorizationsLogoutHandler oidcRevokeAuthorizationsLogoutHandler
    ) {
        OidcLogoutAuthenticationSuccessHandler oidcLogoutAuthenticationSuccessHandler = new OidcLogoutAuthenticationSuccessHandler();
        oidcLogoutAuthenticationSuccessHandler.setLogoutHandler(oidcRevokeAuthorizationsLogoutHandler);
        return  oidcLogoutAuthenticationSuccessHandler;
    }
}
