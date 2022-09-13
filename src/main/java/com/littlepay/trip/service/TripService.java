package com.littlepay.trip.service;

import com.littlepay.trip.dto.Tap;
import com.littlepay.trip.enumeration.TripType;
import com.littlepay.trip.dto.Trip;

import java.util.List;

public interface TripService {

    void generateTrips();

    TripType getTripType(Trip trip);

    double findChargeAmount(Trip trip);

    List<Trip> createTripsFromTaps (List<Tap> tapsList);

}
