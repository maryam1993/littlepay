package com.littlepay.trip.service;

import com.littlepay.trip.enumeration.TripType;
import com.littlepay.trip.dto.Trip;

public interface TripService {

    void generateTrips();

    TripType getTripType(Trip trip);

    double findChargeAmount(Trip trip);


}
