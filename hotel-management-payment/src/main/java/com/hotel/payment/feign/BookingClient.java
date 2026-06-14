package com.hotel.payment.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.hotel.payment.dto.BookingResponse;

@FeignClient(name = "HOTEL-MANAGEMENT-BOOKING")
public interface BookingClient {

	@GetMapping("/booking/internal/{id}")
	BookingResponse getBooking(@PathVariable Long id);
    
	@PutMapping("/booking/internal/{id}/pay")
	void markAsPaid(@PathVariable Long id);
}
