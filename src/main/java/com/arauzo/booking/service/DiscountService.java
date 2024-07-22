package com.arauzo.booking.service;

import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class DiscountService {
    private final RestTemplate restTemplate;

    public DiscountService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Retry(name = "discountService", fallbackMethod = "fallbackValidateDiscount")
    @TimeLimiter(name = "discountService")
    public CompletableFuture<Boolean> validateDiscount(String userId, String houseId, String discountCode) {
        String url = "https://sbv2bumkomidlxwffpgbh4k6jm0ydskh.lambda-url.us-east-1.on.aws/";
        Map<String, String> request = Map.of(
                "userId", userId,
                "houseId", houseId,
                "discountCode", discountCode
        );
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
        return CompletableFuture.completedFuture(response.getStatusCode() == HttpStatus.OK);
    }

    private CompletableFuture<Boolean> fallbackValidateDiscount(String userId, String houseId, String discountCode, Throwable throwable) {
        return CompletableFuture.completedFuture(false);
    }
}
