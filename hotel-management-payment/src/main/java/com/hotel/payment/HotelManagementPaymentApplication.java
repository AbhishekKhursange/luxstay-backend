package com.hotel.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class HotelManagementPaymentApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelManagementPaymentApplication.class, args);
	}

}
