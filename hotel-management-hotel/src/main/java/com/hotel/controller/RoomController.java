package com.hotel.controller;

import com.hotel.dto.RoomRequest;
import com.hotel.entity.Room;
import com.hotel.service.RoomService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private RoomService service;

    @PostMapping
    public Room create(@RequestBody RoomRequest request) {
        return service.create(request);
    }

    @GetMapping("/hotel/{hotelId}")
    public List<Room> getRooms(@PathVariable Long hotelId) {
        return service.getRoomsByHotel(hotelId);
    }

    @GetMapping("/{roomId}")
    public Room getRoom(@PathVariable Long roomId) {
        return service.getRoom(roomId);
    }

    @PutMapping("/{roomId}/reduce")
    public String reduceAvailability(@PathVariable Long roomId) {

        service.reduceAvailability(roomId);

        return "Room updated";
    }
    
    @DeleteMapping("/{roomId}")
    public String deleteRoom(@PathVariable Long roomId) {
        service.deleteRoom(roomId);
        return "Room deleted";
    }
}