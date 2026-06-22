package com.restaurant.authusuarios.userservice.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthServiceClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl;

    public AuthServiceClient(@Value("${app.auth.base-url}") String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void deactivateAccountByEmail(String email) {
        restTemplate.exchange(
                baseUrl + "/internal/auth/deactivate/{email}",
                HttpMethod.POST,
                HttpEntity.EMPTY,
                Void.class,
                email
        );
    }
}
