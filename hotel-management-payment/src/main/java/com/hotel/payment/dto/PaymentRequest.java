package com.hotel.payment.dto;

import lombok.Data;

@Data
public class PaymentRequest {
    private Long bookingId;
    private double amount;
}
