package com.hotel.payment.dto;

import lombok.Data;

@Data
public class BookingResponse {

    private Long bookingId;
    private String username;
    private int days;
    private double totalPrice;
    private String status; 
    private String roomType;
    private String hotelName;
}
