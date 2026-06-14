package com.hotel.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel.payment.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
	boolean existsByBookingId(Long bookingId);
}