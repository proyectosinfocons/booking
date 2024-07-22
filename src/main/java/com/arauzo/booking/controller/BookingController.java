package com.arauzo.booking.controller;

import com.arauzo.booking.entity.Booking;
import com.arauzo.booking.model.ResponseMessage;
import com.arauzo.booking.repository.BookingRepository;
import com.arauzo.booking.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/book")
public class BookingController {
    private final BookingRepository bookRepository;
    private final DiscountService discountService;

    @Autowired
    public BookingController(BookingRepository bookRepository, DiscountService discountService) {
        this.bookRepository = bookRepository;
        this.discountService = discountService;
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<ResponseMessage>> book(@RequestBody Booking booking) {
        return discountService.validateDiscount(booking.getUserId(), booking.getHouseId(), booking.getDiscountCode())
                .thenCompose(isValid -> {
                    if (isValid) {
                        bookRepository.save(booking);
                        ResponseMessage response = new ResponseMessage(200, "Book Accepted");
                        return CompletableFuture.completedFuture(ResponseEntity.ok(response));
                    } else {
                        ResponseMessage response = new ResponseMessage(400, "Invalid Discount");
                        return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response));
                    }
                });
    }
}
