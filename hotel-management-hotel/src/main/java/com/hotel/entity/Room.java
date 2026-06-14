package com.hotel.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "rooms")
@Data
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long hotelId;

    private String roomType;

    @Column(columnDefinition = "TEXT")
    private String description;
    
    private double pricePerNight;

    private int totalRooms;

    private int availableRooms;
    
    @Column(length = 500)
    private String imageUrl;
    
 // ── Bed & Occupancy ─────────────────────────
    private String bedType;        // King, Queen, Twin, Single, Double
    private int maxOccupancy;      // max guests
    private int numberOfBeds;
    private double roomSizeSqm;    // room size in sqm

    // ── Amenities (stored as comma-separated) ───
    @Column(length = 500)
    private String amenities;      // "WiFi,AC,TV,Mini Bar,Safe"

    // ── Policies ────────────────────────────────
    @Column(length = 500)
    private String cancellationPolicy;  // Free, Non-refundable, 24hr
    private boolean breakfastIncluded;
    private boolean petsAllowed;
    private boolean smokingAllowed;

    // ── Extra images ────────────────────────────
    // stored as comma-separated URLs
    @Column(length = 2000)
    private String extraImageUrls;  // "url1,url2,url3"
}