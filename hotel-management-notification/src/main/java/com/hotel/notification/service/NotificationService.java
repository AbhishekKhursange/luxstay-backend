package com.hotel.notification.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.hotel.notification.dto.NotificationRequest;

@Service
public class NotificationService {
	
	@Autowired
    private JavaMailSender mailSender;

    public String send(String message) {
        System.out.println("NOTIFICATION: " + message);
        return "Notification sent";
    }
    
    public void sendBookingConfirmation(NotificationRequest req) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(req.getToEmail());
        msg.setSubject("LuxStay — Booking #" + req.getBookingId() + " Confirmed!");
        msg.setText(
            "Dear " + req.getUsername() + ",\n\n" +
            "Your booking has been confirmed!\n\n" +
            "Booking ID : #" + req.getBookingId() + "\n" +
            "Hotel      : " + req.getHotelName() + "\n" +
            "Room Type  : " + req.getRoomType() + "\n" +
            "Amount Paid: ₹" + req.getAmount() + "\n\n" +
            "Thank you for choosing LuxStay!\n\n" +
            "Team LuxStay"
        );
        mailSender.send(msg);
    }
}
