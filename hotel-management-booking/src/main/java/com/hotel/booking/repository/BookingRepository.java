package com.hotel.booking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel.booking.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUsername(String username);

    boolean existsByUsernameAndRoomIdAndStatus(
            String username,
            Long roomId,
            String status
    );
}