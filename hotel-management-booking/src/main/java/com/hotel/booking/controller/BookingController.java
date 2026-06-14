package com.hotel.booking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.booking.dto.BookingRequest;
import com.hotel.booking.dto.BookingResponse;
import com.hotel.booking.service.BookingService;

@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingService service;

    @PostMapping
    public BookingResponse book(
            @RequestHeader("X-User-Name") String username,
            @RequestBody BookingRequest request
    ) {
        return service.book(username, request);
    }

    @GetMapping
    public List<BookingResponse> myBookings(@RequestHeader("X-User-Name") String username) {
        return service.userBookings(username);
    }
    
    @GetMapping("/{id}")
    public BookingResponse getBooking(
            @RequestHeader("X-User-Name") String username,
            @PathVariable Long id
    ) {
        return service.getBookingById(username, id);
    }
    
    @GetMapping("/internal/{id}")
    public BookingResponse getBookingInternal(@PathVariable Long id) {
        return service.getBookingByIdInternal(id);
    }
    
    @PutMapping("/{id}/pay")
    public String markAsPaid(
            @RequestHeader("X-User-Name") String username,
            @PathVariable Long id
    ) {
        service.markAsPaid(username, id);
        return "Booking marked as PAID";
    }
    
    @PutMapping("/internal/{id}/pay")
    public void markAsPaidInternal(@PathVariable Long id) {
        service.markAsPaidInternal(id);
    }
}
