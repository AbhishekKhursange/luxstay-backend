package com.hotel.payment.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.hotel.payment.dto.NotificationRequest;

@FeignClient(name = "HOTEL-MANAGEMENT-NOTIFICATION")
public interface NotificationClient {

    @PostMapping("/notify")
    String sendNotification(@RequestBody String message);
    
    @PostMapping("/notify/booking-confirmed")
    String sendConfirmation(@RequestBody NotificationRequest request);
}
