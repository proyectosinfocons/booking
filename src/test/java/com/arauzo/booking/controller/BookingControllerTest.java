package com.arauzo.booking.controller;

import com.arauzo.booking.entity.Booking;
import com.arauzo.booking.model.ResponseMessage;
import com.arauzo.booking.repository.BookingRepository;
import com.arauzo.booking.service.DiscountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class BookingControllerTest {

    @InjectMocks
    private BookingController bookingController;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private DiscountService discountService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testBook_Success() throws Exception {
        Booking booking = new Booking();
        booking.setUserId("user1");
        booking.setHouseId("house1");
        booking.setDiscountCode("DISCOUNT10");

        when(discountService.validateDiscount(any(), any(), any())).thenReturn(CompletableFuture.completedFuture(true));

        CompletableFuture<ResponseEntity<ResponseMessage>> responseEntity = bookingController.book(booking);
        ResponseEntity<ResponseMessage> response = responseEntity.get();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(200, response.getBody().getCode());
        assertEquals("Book Accepted", response.getBody().getMessage());
    }

    @Test
    public void testBook_InvalidDiscount() throws Exception {
        Booking booking = new Booking();
        booking.setUserId("user1");
        booking.setHouseId("house1");
        booking.setDiscountCode("INVALID");

        when(discountService.validateDiscount(any(), any(), any())).thenReturn(CompletableFuture.completedFuture(false));

        CompletableFuture<ResponseEntity<ResponseMessage>> responseEntity = bookingController.book(booking);
        ResponseEntity<ResponseMessage> response = responseEntity.get();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().getCode());
        assertEquals("Invalid Discount", response.getBody().getMessage());
    }
}