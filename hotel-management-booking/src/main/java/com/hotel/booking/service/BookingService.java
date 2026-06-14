package com.hotel.booking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotel.booking.dto.BookingRequest;
import com.hotel.booking.dto.BookingResponse;
import com.hotel.booking.dto.HotelResponse;
import com.hotel.booking.dto.RoomResponse;
import com.hotel.booking.entity.Booking;
import com.hotel.booking.feign.RoomClient;
import com.hotel.booking.repository.BookingRepository;

@Service
public class BookingService {

    @Autowired
    private BookingRepository repo;

    @Autowired
    private RoomClient roomClient;
    
    // ✅ CREATE BOOKING
    public BookingResponse book(String username, BookingRequest request) {

        // 1. Validate days
        if (request.getDays() <= 0) {
            throw new RuntimeException("Days must be greater than 0");
        }
        
     // 2. Duplicate booking check
        boolean alreadyExists =
                repo.existsByUsernameAndRoomIdAndStatus(
                        username,
                        request.getRoomId(),
                        "CREATED"
                );

        if (alreadyExists) {
            throw new RuntimeException("You already have an active booking for this hotel");
        }

     // 3. Get room details
        RoomResponse room = roomClient.getRoom(request.getRoomId());

        if (room == null) {
            throw new RuntimeException("Room not found");
        }
        
        String hotelName = "";
        try {
            HotelResponse hotel = roomClient.getHotel(room.getHotelId());
            hotelName = hotel.getName();
        } catch (Exception e) {
            hotelName = "";  // fallback if hotel fetch fails
        }

     // 4. Calculate total
        double total = room.getPricePerNight() * request.getDays();
        
     // 5. Reduce room availability
        roomClient.reduceAvailability(room.getId());
        
     // 6. Save booking
        Booking booking = new Booking();

        booking.setUsername(username);
        booking.setRoomId(room.getId());
        booking.setDays(request.getDays());
        booking.setTotalPrice(total);
        booking.setStatus("CREATED");
        booking.setRoomType(room.getRoomType());
        booking.setHotelName(hotelName);

        Booking saved = repo.save(booking);

     // 7. Return response
        return mapToResponse(saved, room);
    }

    // ✅ MARK AS PAID (FINAL CONTROL)
    public void markAsPaid(String username, Long bookingId) {

        Booking booking = repo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // 🔐 Ownership check
        if (!booking.getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized");
        }

        if ("PAID".equals(booking.getStatus())) {
            throw new RuntimeException("Booking already paid");
        }

        booking.setStatus("PAID");
        repo.save(booking);
    }
    
    public void markAsPaidInternal(Long bookingId) {

        Booking booking = repo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if ("PAID".equals(booking.getStatus())) {
            throw new RuntimeException("Booking already paid");
        }

        booking.setStatus("PAID");
        repo.save(booking);
    }

    // ✅ USER BOOKINGS
    public List<BookingResponse> userBookings(String username) {
        return repo.findByUsername(username).stream()
            .map(b -> {
                BookingResponse r = new BookingResponse();
                r.setBookingId(b.getId());
                r.setUsername(b.getUsername());
                r.setRoomId(b.getRoomId());
                r.setDays(b.getDays());
                r.setTotalPrice(b.getTotalPrice());
                r.setStatus(b.getStatus());
                r.setRoomType(b.getRoomType());
                r.setHotelName(b.getHotelName());
                return r;
            })
            .collect(java.util.stream.Collectors.toList());
    }

    // ✅ SECURE API (USED BY FRONTEND)
    public BookingResponse getBookingById(String username, Long id) {

        Booking booking = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // 🔐 SECURITY CHECK
        if (!booking.getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access");
        }

        RoomResponse room =
                roomClient.getRoom(booking.getRoomId());

        return mapToResponse(booking, room);
    }

    // ✅ INTERNAL API (USED BY PAYMENT SERVICE)
    public BookingResponse getBookingByIdInternal(Long id) {

        Booking booking = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        RoomResponse room =
                roomClient.getRoom(booking.getRoomId());

        return mapToResponse(booking, room);
    }

    // ✅ COMMON MAPPER (CLEAN CODE)
    private BookingResponse mapToResponse(Booking booking, RoomResponse room) {

        BookingResponse response = new BookingResponse();
        response.setBookingId(booking.getId());
        response.setUsername(booking.getUsername());
        response.setRoomId(booking.getRoomId());
        response.setDays(booking.getDays());
        response.setTotalPrice(booking.getTotalPrice());
        response.setStatus(booking.getStatus());
        response.setRoomType(booking.getRoomType());
        response.setHotelName(booking.getHotelName());

        if (room != null) {
            response.setRoomType(room.getRoomType());
        }

        return response;
    }
}