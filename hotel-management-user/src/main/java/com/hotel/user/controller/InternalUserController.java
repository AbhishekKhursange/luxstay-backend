package com.hotel.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.user.dto.UserRequest;
import com.hotel.user.dto.UserResponse;
import com.hotel.user.entity.User;
import com.hotel.user.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/internal/user")
public class InternalUserController {
	
	@Autowired
    private UserService service;
	
	@PostMapping("/register")
	public ResponseEntity<UserResponse> register(
	        @Valid @RequestBody UserRequest request
	) {
	    User user = service.save(request);

	    UserResponse response = new UserResponse(
	            user.getUsername(),
	            user.getRole(),
	            user.getEmail(), 
	            user.getPhone()
	    );

	    return ResponseEntity.ok(response);
	}
	
	// Add this new endpoint inside InternalUserController:

	@PostMapping("/register/oauth")
	public ResponseEntity<UserResponse> registerOAuth(
	        @RequestBody UserRequest request
	) {
	    // Check if user already exists — if yes, just return existing user
	    try {
	        User existing = service.findByUsername(request.getUsername());
	        UserResponse response = new UserResponse(
	            existing.getUsername(),
	            existing.getRole(),
	            existing.getEmail(),
	            existing.getPhone()
	        );
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        // User doesn't exist — create new one
	        User user = service.save(request);
	        UserResponse response = new UserResponse(
	            user.getUsername(),
	            user.getRole(),
	            user.getEmail(),
	            user.getPhone()
	        );
	        return ResponseEntity.ok(response);
	    }
	}

	@GetMapping("/{username}")
	public ResponseEntity<User> getUser(@PathVariable String username) {
	    return ResponseEntity.ok(service.findByUsername(username));
	}

}
