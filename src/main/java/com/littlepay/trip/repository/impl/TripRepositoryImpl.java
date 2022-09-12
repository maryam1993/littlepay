package com.littlepay.trip.repository.impl;

import com.littlepay.trip.constant.TripCharge;
import com.littlepay.trip.repository.TripRepository;
import com.littlepay.trip.dto.Trip;
import com.littlepay.trip.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TripRepositoryImpl implements TripRepository {

    @Value("${trips.file.name}")
    private String fileName;

    @Value("${header}")
    private String header;

    @Override
    public double[][] getTripChargeList() {
        return TripCharge.getTripsCharges();
    }

    @Override
    public void registerTrips(List<Trip> trips) {
        FileUtil.writeFile(header, fileName, trips);
    }
}
