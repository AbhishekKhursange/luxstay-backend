package com.hotel.booking.feign;

import com.hotel.booking.dto.HotelResponse;
import com.hotel.booking.dto.RoomResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "HOTEL-MANAGEMENT-HOTEL")
public interface RoomClient {

    @GetMapping("/room/{id}")
    RoomResponse getRoom(@PathVariable Long id);

    @PutMapping("/room/{id}/reduce")
    String reduceAvailability(@PathVariable Long id);
    
    @GetMapping("/hotel/{id}")
    HotelResponse getHotel(
        @PathVariable Long id);
}