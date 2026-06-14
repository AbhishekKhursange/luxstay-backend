package com.hotel.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.user.dto.UserResponse;
import com.hotel.user.entity.User;
import com.hotel.user.service.UserService;

@RestController
@RequestMapping("/user")
public class PublicUserController {

	@Autowired
	private UserService service;
	
	@GetMapping("/profile")
	public User profile(
	    @RequestHeader("X-User-Name") String username
	) {
	    return service.findByUsername(username);
	}
	
	

	@GetMapping("/{username}")
	public UserResponse getUser(@PathVariable String username) {
	    User user = service.findByUsername(username);
	    return new UserResponse(user.getUsername(), user.getRole(), user.getEmail(), user.getPhone());
	}
	
	// Add this endpoint — ADMIN only
	@GetMapping
	public List<User> getAllUsers(
	    @RequestHeader("X-User-Role") String role
	) {
	    if (!"ADMIN".equals(role)) {
	        throw new RuntimeException("Unauthorized");
	    }
	    return service.findAll();
	}
}
