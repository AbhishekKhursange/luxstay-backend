package com.hotel.notification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.notification.dto.NotificationRequest;
import com.hotel.notification.service.NotificationService;

@RestController
@RequestMapping("/notify")
public class NotificationController {

    @Autowired
    private NotificationService service;

    @PostMapping
    public String notify(@RequestBody String message) {
        return service.send(message);
    }
    
    @PostMapping("/booking-confirmed")
    public String sendConfirmation(@RequestBody NotificationRequest request) {
        service.sendBookingConfirmation(request);
        return "Email sent";
    }
}
