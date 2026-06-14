package com.hotel.payment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.payment.dto.PaymentRequest;
import com.hotel.payment.dto.PaymentResponse;
import com.hotel.payment.service.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService service;

    @PostMapping
    public PaymentResponse pay(
            @RequestHeader("X-User-Name") String username,
            @RequestBody PaymentRequest request
    ) {
        return service.pay(username, request);
    }
}
