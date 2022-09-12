package com.littlepay.trip;

import com.littlepay.trip.enumeration.TripType;
import com.littlepay.trip.service.TripService;
import com.littlepay.trip.dto.Trip;
import com.littlepay.trip.util.FileUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class TripApplicationTests {

    @Autowired
    TripService tripService;


    @Test
    void contextLoads() {
        List<Trip> trips = new ArrayList<>();
        Trip trip = new Trip();
        trip.setTripType(TripType.COMPLETED);
//        trip.setChargeAmount(32);
        trip.setPan("29385245435");
        trips.add(trip);
//        FileUtil.writeFile("trips.csv", trips);
    }
}


