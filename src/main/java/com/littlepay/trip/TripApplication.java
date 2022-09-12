package com.littlepay.trip;

import com.littlepay.trip.service.TripService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class TripApplication {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(TripApplication.class, args);

		TripService service = applicationContext.getBean(TripService.class);
		service.generateTrips();
	}

}
