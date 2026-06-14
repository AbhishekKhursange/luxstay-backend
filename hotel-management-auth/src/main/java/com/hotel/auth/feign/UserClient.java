package com.hotel.auth.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.hotel.auth.dto.RegisterRequest;
import com.hotel.auth.dto.UserDto;

@FeignClient(name = "HOTEL-MANAGEMENT-USER", path = "/internal/user")
public interface UserClient {

    @PostMapping("/register")
    UserDto register(@RequestBody RegisterRequest request);

    @GetMapping("/{username}")
    UserDto getUser(@PathVariable String username);
    
    @PostMapping("/register/oauth")          // ← ADD this
    UserDto registerOAuthUser(@RequestBody RegisterRequest request);
}