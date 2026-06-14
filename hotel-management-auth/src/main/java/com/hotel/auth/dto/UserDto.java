package com.hotel.auth.dto;

import lombok.Data;

@Data
public class UserDto {
    private String username;
    private String password;
    private String role;
}