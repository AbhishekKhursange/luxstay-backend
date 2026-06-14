package com.hotel.booking.dto;

import lombok.Data;

@Data
public class BookingResponse {

    private Long bookingId;

    private String username;

    private int days;

    private double totalPrice;

    private String status;

    private Long roomId;

    private String roomType;
    
    private String hotelName;
}