package com.certus.mentoria.config;

import java.util.HashMap;
import java.util.Map;


import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

    private final OAuth2AuthorizationRequestResolver defaultResolver;

    public CustomAuthorizationRequestResolver(ClientRegistrationRepository repo, String authorizationRequestBaseUri) {
        this.defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(repo, authorizationRequestBaseUri);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(jakarta.servlet.http.HttpServletRequest request) {
        OAuth2AuthorizationRequest authRequest = defaultResolver.resolve(request);
        return customizeAuthorizationRequest(authRequest);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(jakarta.servlet.http.HttpServletRequest request, String clientRegistrationId) {
        OAuth2AuthorizationRequest authRequest = defaultResolver.resolve(request, clientRegistrationId);
        return customizeAuthorizationRequest(authRequest);
    }

    private OAuth2AuthorizationRequest customizeAuthorizationRequest(OAuth2AuthorizationRequest req) {
        if (req == null) return null;

        Map<String, Object> additionalParameters = new HashMap<>(req.getAdditionalParameters());

        // Force Google to show account chooser and consent screen
        additionalParameters.put("prompt", "select_account consent");

        return OAuth2AuthorizationRequest.from(req)
                .additionalParameters(additionalParameters)
                .build();
    }
}
