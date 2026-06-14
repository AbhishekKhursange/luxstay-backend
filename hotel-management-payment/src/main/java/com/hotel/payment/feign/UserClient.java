package com.hotel.payment.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.hotel.payment.dto.UserResponse;

@FeignClient(name = "HOTEL-MANAGEMENT-USER")
public interface UserClient {

    @GetMapping("/user/{username}")
    UserResponse getUser(@PathVariable String username);
}