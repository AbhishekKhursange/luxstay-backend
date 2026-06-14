package com.hotel.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRequest {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;
    
    private String fullName;
    
    private String role;
    
    @NotBlank(message = "Email required")
    private String email;
    
    private String phone;
}