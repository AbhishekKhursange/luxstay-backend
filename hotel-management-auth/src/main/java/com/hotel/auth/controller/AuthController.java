package com.hotel.auth.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.auth.dto.AuthRequest;
import com.hotel.auth.dto.AuthResponse;
import com.hotel.auth.dto.RegisterRequest;
import com.hotel.auth.service.AuthService;
import com.hotel.auth.service.TokenService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
    private AuthService service;
	
	@Autowired
    private TokenService tokenService;
    
    @PostMapping("/register")
    public String register(@Valid @RequestBody RegisterRequest request) {
        return service.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody AuthRequest request) {
        return service.login(request);
    }
    
    @PostMapping("/refresh")
    public AuthResponse refresh(@RequestBody Map<String, String> body) {
        return service.refresh(body.get("refreshToken"));
    }
    
    @PostMapping("/logout")
    public String logout(@RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);

        tokenService.blacklistToken(token);

        return "Logged out successfully";
    }

}
