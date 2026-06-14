package com.hotel.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class HotelRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String location;
    
    private String imageUrl;
    
    private String galleryImages;
}