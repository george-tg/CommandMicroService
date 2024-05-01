package com.kth.journalsystem.service;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class KeycloakTokenExchangeService {
    private static final String TOKEN_EXCHANGE_URL = "http://localhost:8181/realms/Journal/protocol/openid-connect/token";
    private static final String CLIENT_ID = "journal";
    private static final String CLIENT_SECRET = "yWIpK52fU3cAryzVDt3t6qFXDd53g3ad";
    private final RestTemplate restTemplate = new RestTemplateBuilder().build();

    public Map getLimitedScopeToken(String token) throws RestClientException {
        String url = TOKEN_EXCHANGE_URL;
        String clientId = CLIENT_ID;
        String clientSecret = CLIENT_SECRET;
        String scope = "patient"; // Replace with your specific scope

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "urn:ietf:params:oauth:grant-type:token-exchange");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("subject_token", token);
        body.add("subject_token_type", "urn:ietf:params:oauth:token-type:access_token");
        body.add("scope", scope);
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

        return response.getBody();
    }
}

