package com.hotel.payment.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotel.payment.dto.BookingResponse;
import com.hotel.payment.dto.NotificationRequest;
import com.hotel.payment.dto.PaymentRequest;
import com.hotel.payment.dto.PaymentResponse;
import com.hotel.payment.dto.UserResponse;
import com.hotel.payment.entity.Payment;
import com.hotel.payment.exception.BusinessException;
import com.hotel.payment.feign.BookingClient;
import com.hotel.payment.feign.NotificationClient;
import com.hotel.payment.feign.UserClient;
import com.hotel.payment.repository.PaymentRepository;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository repo;

    @Autowired
    private NotificationClient notificationClient;
    
    @Autowired
    private BookingClient bookingClient;
    
    @Autowired
    private UserClient userClient;
    
    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    @CircuitBreaker(name = "bookingService", fallbackMethod = "fallback")
    @Transactional
    public PaymentResponse pay(String username, PaymentRequest request) {

    	log.info("Processing payment for booking: {}", request.getBookingId());
    	// ✅ 0. Check already paid
    	if (repo.existsByBookingId(request.getBookingId())) {
    	    throw new BusinessException("Payment already done for this booking");
    	}
    	
    	// ✅ 1. Validate booking exists
        BookingResponse booking;

        try {
            booking = bookingClient.getBooking(request.getBookingId());
        } catch (FeignException.NotFound e) {
            throw new BusinessException("Booking not found");
        } catch (Exception e) {
            throw new RuntimeException("Error while validating booking");
        }
        
     // ✅ Prevent double payment (SERVICE LEVEL - IMPORTANT)
        if ("PAID".equals(booking.getStatus())) {
            throw new BusinessException("Booking already paid");
        }

        // ✅ 2. Validate booking belongs to user
        if (!booking.getUsername().equals(username)) {
            throw new BusinessException("Unauthorized payment attempt");
        }

        // ✅ 3. Validate amount
        if (Math.abs(request.getAmount() - booking.getTotalPrice()) > 0.01) {
            throw new BusinessException("Invalid payment amount");
        }
    	
        Payment payment = new Payment();
        payment.setBookingId(request.getBookingId());
        payment.setUsername(username);
        payment.setAmount(request.getAmount());

        // Simulate success
        payment.setStatus("SUCCESS");

        try {
            repo.save(payment);
        } catch (Exception e) {
            // DB constraint / race condition fallback
            throw new BusinessException("Payment already done for this booking");
        }

        try {
            bookingClient.markAsPaid(request.getBookingId());
        } catch (Exception e) {
            log.error("Booking update failed, manual recovery needed");
        }

        try {
            UserResponse user = userClient.getUser(username);

            NotificationRequest notif = new NotificationRequest();
            notif.setToEmail(user.getEmail());
            notif.setUsername(username);
            notif.setBookingId(request.getBookingId());
            notif.setAmount(request.getAmount());
            notif.setRoomType(booking.getRoomType());
            notif.setHotelName(booking.getHotelName());

            notificationClient.sendConfirmation(notif);
            log.info("Confirmation email sent to {}", user.getEmail());
        } catch (Exception e) {
            // Don't fail payment if email fails
            log.warn("Email notification failed: {}", e.getMessage());
        }
        
        log.info("Payment successful for booking: {}", request.getBookingId());

        return new PaymentResponse("Payment successful", "SUCCESS");
    }
    
    public PaymentResponse fallback(String username, PaymentRequest request, Throwable ex) {

        // 🔥 DO NOT override business exceptions
        if (ex instanceof BusinessException) {
            throw (BusinessException) ex;
        }

        log.error("Booking service is down! Fallback triggered for booking: {}",
                request.getBookingId());

        return new PaymentResponse(
                "Booking service down. Try again later",
                "FAILED"
        );
    }
}
