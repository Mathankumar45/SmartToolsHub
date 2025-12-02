package com.SmartToolsHub.payments;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.Map;

@Component
public class Pay2AllClient {

    private final RestTemplate rest;
    private final String baseUrl;
    private final String apiKey;

    public Pay2AllClient(@Value("${pay2all.base-url}") String baseUrl,
                         @Value("${pay2all.api.key}") String apiKey) {
        this.rest = new RestTemplate();
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
    }

    /**
     * Example request map: depends on Pay2All docs.
     * Here we send mobile number, operator, circle and amount.
     */
    public Map<String, Object> rechargeMobile(String mobile, String operator, String circle, int amount, String transactionId) {
        String url = baseUrl + "/recharge"; // replace with actual path
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("API-Key", apiKey); // or use Authorization Bearer as required by provider

        Map<String,Object> payload = Map.of(
            "mobile", mobile,
            "operator", operator,
            "circle", circle,
            "amount", amount,
            "client_txn_id", transactionId // idempotency
        );

        HttpEntity<Map<String,Object>> req = new HttpEntity<>(payload, headers);
        ResponseEntity<Map> resp = rest.postForEntity(url, req, Map.class);
        return resp.getBody();
    }
}
