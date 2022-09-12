package com.littlepay.trip.repository;

import com.littlepay.trip.dto.Trip;

import java.util.List;

public interface TripRepository {

    double[][] getTripChargeList();

    void registerTrips(List<Trip> trips);

}
