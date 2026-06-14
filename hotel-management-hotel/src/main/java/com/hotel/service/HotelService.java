package com.hotel.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotel.dto.HotelRequest;
import com.hotel.entity.Hotel;
import com.hotel.exception.ResourceNotFoundException;
import com.hotel.repository.HotelRepository;

@Service
public class HotelService {

    @Autowired
    private HotelRepository repo;

 // CREATE HOTEL
    public Hotel create(HotelRequest request) {
        Hotel hotel = new Hotel();
        hotel.setName(request.getName());
        hotel.setLocation(request.getLocation());
        hotel.setImageUrl(request.getImageUrl());
        hotel.setGalleryImages(request.getGalleryImages());
        return repo.save(hotel);
    }

 // GET ALL HOTELS
    public List<Hotel> getAll() {
        return repo.findAll();
    }

 // GET HOTEL BY ID
    public Hotel getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
