package com.hotel.payment.dto;

import lombok.Data;

@Data
public class NotificationRequest {
    private String toEmail;
    private String username;
    private Long bookingId;
    private double amount;
    private String roomType;
    private String hotelName;
}
