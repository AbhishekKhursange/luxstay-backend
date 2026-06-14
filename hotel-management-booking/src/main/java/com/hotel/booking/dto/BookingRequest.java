package com.hotel.booking.dto;

import lombok.Data;

@Data
public class BookingRequest {

    private Long roomId;

    private int days;
}