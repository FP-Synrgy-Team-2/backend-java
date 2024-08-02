package com.example.jangkau.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/refresh-token")
public class OAuthController {

    @Value("${BASEURL}")
    private String authUrl;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

//    @CookieValue
    @GetMapping()
    public ResponseEntity<Map> getNewAccessToken(@RequestParam("refresh_token") String refreshToken) {
        String url = authUrl + "/oauth/token?grant_type=refresh_token&refresh_token=" + refreshToken +
                "&client_id=my-client-web&client_secret=password";

        ResponseEntity<Map> response = restTemplateBuilder.build().exchange(url, HttpMethod.POST, null, new ParameterizedTypeReference<Map>() {});

        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(response.getBody());
        } else {
            throw new ResponseStatusException(response.getStatusCode(), "Failed to refresh token");
        }
    }

}


