package com.hotel.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {

	@NotBlank(message = "Username required")
    private String username;
	
	@NotBlank(message = "Password required")
    private String password;
	
	private String fullName; 
    
    private String role = "USER";
    
    @NotBlank(message = "Email required")
    @Email(message = "Invalid email format")
    private String email;
    
    private String phone;
}