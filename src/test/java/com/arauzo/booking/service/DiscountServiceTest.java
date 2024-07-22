package com.arauzo.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class DiscountServiceTest {

    @InjectMocks
    private DiscountService discountService;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testValidateDiscount_Success() {
        String userId = "user1";
        String houseId = "house1";
        String discountCode = "DISCOUNT10";

        Map<String, String> request = Map.of(
                "userId", userId,
                "houseId", houseId,
                "discountCode", discountCode
        );

        ResponseEntity<Map> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.postForEntity(any(String.class), eq(request), eq(Map.class))).thenReturn(responseEntity);

        CompletableFuture<Boolean> result = discountService.validateDiscount(userId, houseId, discountCode);
        assertEquals(true, result.join());
    }

    @Test
    public void testValidateDiscount_Failure() {
        String userId = "user1";
        String houseId = "house1";
        String discountCode = "INVALID";

        Map<String, String> request = Map.of(
                "userId", userId,
                "houseId", houseId,
                "discountCode", discountCode
        );

        ResponseEntity<Map> responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        when(restTemplate.postForEntity(any(String.class), eq(request), eq(Map.class))).thenReturn(responseEntity);

        CompletableFuture<Boolean> result = discountService.validateDiscount(userId, houseId, discountCode);
        assertEquals(false, result.join());
    }

}