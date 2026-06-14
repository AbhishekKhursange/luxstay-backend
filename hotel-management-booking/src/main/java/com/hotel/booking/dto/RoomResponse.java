package com.hotel.booking.dto;

import lombok.Data;

@Data
public class RoomResponse {

    private Long id;

    private Long hotelId;

    private String roomType;

    private double pricePerNight;

    private int availableRooms;
    
    private String hotelName;
}