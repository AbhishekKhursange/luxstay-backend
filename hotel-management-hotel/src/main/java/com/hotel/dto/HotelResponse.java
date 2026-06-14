package com.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HotelResponse {
    private Long id;
    private String name;
    private String location;
    private String imageUrl;
    private String galleryImages;
}
