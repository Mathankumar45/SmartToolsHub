package com.SmartToolsHub.payments;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.Map;

@Component
public class Pay2AllClient {

    private final RestTemplate rest;
    private final String apiKey;
    private final String secretKey;

    private static final String BASE_URL = "https://api.pay2all.in/v1";

    public Pay2AllClient(
            @Value("${pay2all.api-key}") String apiKey,
            @Value("${pay2all.secret-key}") String secretKey) {

        this.rest = new RestTemplate();
        this.apiKey = apiKey;
        this.secretKey = secretKey;
    }

    public Map<String, Object> rechargeMobile(
            String mobile, int providerId, int amount, String transactionId) {

        String url = BASE_URL + "/recharge";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Correct Pay2All Authentication format
        headers.set("Authorization", "Bearer " + apiKey + ":" + secretKey);

        Map<String, Object> payload = Map.of(
                "number", mobile,
                "provider_id", providerId,
                "amount", amount,
                "client_id", transactionId
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        ResponseEntity<Map> resp = rest.postForEntity(url, request, Map.class);

        return resp.getBody();
    }
}
