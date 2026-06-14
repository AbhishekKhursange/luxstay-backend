package com.hotel.dto;

import lombok.Data;

@Data
public class RoomResponse {

    private Long id;

    private Long hotelId;

    private String roomType;
    
    private String description;

    private double pricePerNight;

    private int totalRooms;

    private int availableRooms;
    
    private String imageUrl;
    
 // Bed & Occupancy
    private String bedType;
    private int maxOccupancy;
    private int numberOfBeds;
    private double roomSizeSqm;

    // Amenities
    private String amenities;

    // Policies
    private String cancellationPolicy;
    private boolean breakfastIncluded;
    private boolean petsAllowed;
    private boolean smokingAllowed;

    // Extra images
    private String extraImageUrls;
}