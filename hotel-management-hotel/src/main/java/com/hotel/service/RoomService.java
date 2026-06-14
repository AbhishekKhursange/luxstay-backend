package com.hotel.service;

import com.hotel.dto.RoomRequest;
import com.hotel.entity.Hotel;
import com.hotel.entity.Room;
import com.hotel.exception.ResourceNotFoundException;
import com.hotel.repository.HotelRepository;
import com.hotel.repository.RoomRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomRepository repo;
    
    @Autowired
    private HotelRepository hotelRepo;

 // CREATE ROOM
    public Room create(RoomRequest request) {
    	
    	// ✅ VALIDATE HOTEL EXISTS

        Hotel hotel = hotelRepo.findById(request.getHotelId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Hotel not found with id: "
                                        + request.getHotelId()
                        )
                );

        Room room = new Room();

        room.setHotelId(hotel.getId());
        room.setRoomType(request.getRoomType());
        room.setDescription(request.getDescription());
        room.setPricePerNight(request.getPricePerNight());

        room.setTotalRooms(request.getTotalRooms());
        room.setAvailableRooms(request.getTotalRooms());
        room.setImageUrl(request.getImageUrl());
        
     // Bed & Occupancy
        room.setBedType(request.getBedType());
        room.setMaxOccupancy(request.getMaxOccupancy());
        room.setNumberOfBeds(request.getNumberOfBeds());
        room.setRoomSizeSqm(request.getRoomSizeSqm());

        // Amenities
        room.setAmenities(request.getAmenities());

        // Policies
        room.setCancellationPolicy(request.getCancellationPolicy());
        room.setBreakfastIncluded(request.isBreakfastIncluded());
        room.setPetsAllowed(request.isPetsAllowed());
        room.setSmokingAllowed(request.isSmokingAllowed());

        // Extra images
        room.setExtraImageUrls(request.getExtraImageUrls());

        return repo.save(room);
    }

    // GET ROOMS BY HOTEL
    public List<Room> getRoomsByHotel(Long hotelId) {
        return repo.findByHotelId(hotelId);
    }

    // GET ROOM
    public Room getRoom(Long roomId) {
        return repo.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
    }

    // REDUCE AVAILABILITY
    public void reduceAvailability(Long roomId) {

        Room room = getRoom(roomId);

        if (room.getAvailableRooms() <= 0) {
            throw new RuntimeException("Room not available");
        }

        room.setAvailableRooms(room.getAvailableRooms() - 1);

        repo.save(room);
    }
    
    public void deleteRoom(Long roomId) {
        if (!repo.existsById(roomId)) {
            throw new RuntimeException("Room not found");
        }
        repo.deleteById(roomId);
    }
}