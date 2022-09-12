package com.littlepay.trip.service.impl;

import com.littlepay.trip.dto.Tap;
import com.littlepay.trip.dto.Trip;
import com.littlepay.trip.enumeration.TapType;
import com.littlepay.trip.enumeration.TripType;
import com.littlepay.trip.repository.TapRepository;
import com.littlepay.trip.repository.TripRepository;
import com.littlepay.trip.service.TripService;
import com.littlepay.trip.util.Mapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TripServiceImpl implements TripService {

    private TapRepository tapRepository;
    private TripRepository tripRepository;

    @Override
    public void generateTrips() {
        List<Tap> tapsList = tapRepository.getTapsList();
        List<Trip> tripList = createTripsFromTaps(tapsList);
        tripRepository.registerTrips(tripList);
    }

    @Override
    public TripType getTripType(Trip trip) {
        if (trip.getFromStopId() == trip.getToStopId()) {
            return TripType.CANCELLED;
        } else if (trip.getToStopId() == null) {
            return TripType.INCOMPLETE;
        } else {
            return TripType.COMPLETED;
        }
    }

    @Override
    public double findChargeAmount(Trip trip) {
        double[][] tripsCharges = tripRepository.getTripChargeList();
        double chargeAmount = 0;
        switch (trip.getTripType()) {
            case COMPLETED -> chargeAmount = tripsCharges[trip.getFromStopId().getValue()][trip.getToStopId().getValue()];
            case INCOMPLETE -> {
                double[] relatedCharges = tripsCharges[trip.getFromStopId().getValue()];
                chargeAmount = Arrays.stream(relatedCharges).max().getAsDouble();
            }
        }
        return chargeAmount;
    }


    private List<Trip> createTripsFromTaps(List<Tap> tapsList) {
        List<Trip> allTrips = new ArrayList<>();

        Map<String, List<Tap>> mapOfTapsByPan = Mapper.mapTapListToMapOfPanAndTaps(tapsList);
        for (String pan : mapOfTapsByPan.keySet()) {
            allTrips.addAll(createTripListOfASpecificPan(mapOfTapsByPan.get(pan)));
        }
        return allTrips.stream().sorted(Comparator.comparing(Trip::getStarted)).collect(Collectors.toList());
    }

    private List<Trip> createTripListOfASpecificPan(List<Tap> taps) {
        List<Trip> tripList = new ArrayList<>();
        Trip trip = null;
        Tap previousTap = null;

        for (Tap tap : taps) {
            if (tap.getTapType() == TapType.ON && previousTap != null &&
                    isIncompleteTrip(previousTap, tap)) {
                fillEndOfTripForIncompleteTrips(trip);
                tripList.add(trip);
                previousTap = null;
            }
            if (tap.getTapType() == TapType.ON && previousTap == null) {
                previousTap = tap;
                trip = new Trip();
                fillStartOfTrip(tap, trip);
            } else if (tap.getTapType() == TapType.OFF && trip != null) {
                fillEndOfTrip(tap, trip);
                tripList.add(trip);
                previousTap = null;
            }
        }
        if (previousTap != null) {
            fillEndOfTripForIncompleteTrips(trip);
            tripList.add(trip);
        }
        return tripList;
    }

    private boolean isIncompleteTrip(Tap previousTap, Tap currentTap) {
        return (!Objects.equals(previousTap.getBusId(), currentTap.getBusId()) ||
                !Objects.equals(previousTap.getCompanyId(), currentTap.getCompanyId()));
    }

    private void fillEndOfTripForIncompleteTrips(Trip trip) {
        trip.setTripType(getTripType(trip));
        trip.setChargeAmount(findChargeAmount(trip));
    }

    private void fillEndOfTrip(Tap tap, Trip trip) {
        trip.setFinished(tap.getTapDateTime());
        trip.setDurationSecs(Duration.between(trip.getStarted(), tap.getTapDateTime()).toSeconds());
        trip.setToStopId(tap.getStopId());
        trip.setTripType(getTripType(trip));
        trip.setChargeAmount(findChargeAmount(trip));
    }

    private void fillStartOfTrip(Tap tap, Trip trip) {
        trip.setPan(tap.getPan());
        trip.setStarted(tap.getTapDateTime());
        trip.setFromStopId(tap.getStopId());
        trip.setCompanyId(tap.getCompanyId());
        trip.setBusId(tap.getBusId());
    }


}
