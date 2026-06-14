package com.hotel.payment.dto;

import lombok.Data;

@Data
public class UserResponse {
    private String username;
    private String role;
    private String email;
    private String phone;
}
