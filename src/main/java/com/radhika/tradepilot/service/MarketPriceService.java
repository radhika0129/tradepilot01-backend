package com.radhika.tradepilot.service;

import com.radhika.tradepilot.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import java.util.Map;

@Service
public class MarketPriceService {

    @Value("${indian.api.key}")
    private String apiKey;

    @Value("${indian.api.url}")
    private String apiUrl;


    private final RestTemplate restTemplate = new RestTemplate();

    public Double getCurrentPrice(String stockSymbol) {

        String url = apiUrl + "/stock?name=" + stockSymbol;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-Key", apiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        entity,
                        Map.class
                );

        Map body = response.getBody();

        if (body == null || !body.containsKey("currentPrice")) {
            throw new CustomException("Live market price not available");
        }

        Map currentPrice = (Map) body.get("currentPrice");

        if (currentPrice == null || !currentPrice.containsKey("NSE")) {
            throw new CustomException("NSE market price not available");
        }

        String nsePrice = currentPrice.get("NSE").toString();

        return Double.parseDouble(nsePrice);
    }
}