package com.hotel.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class HotelManagementBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelManagementBookingApplication.class, args);
	}

}
