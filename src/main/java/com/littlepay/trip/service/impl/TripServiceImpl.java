package com.littlepay.trip.service.impl;

import com.littlepay.trip.dto.Tap;
import com.littlepay.trip.dto.Trip;
import com.littlepay.trip.enumeration.TapType;
import com.littlepay.trip.enumeration.TripType;
import com.littlepay.trip.repository.TapRepository;
import com.littlepay.trip.repository.TripRepository;
import com.littlepay.trip.service.TripService;
import com.littlepay.trip.util.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class TripServiceImpl implements TripService {

    private TapRepository tapRepository;
    private TripRepository tripRepository;

    @Override
    public void generateTrips() {
        log.info("starting the generating trips process...");
        List<Tap> tapsList = tapRepository.getTapsList();

        log.info("starting the process of creating trips from given taps...");
        List<Trip> tripList = createTripsFromTaps(tapsList);

        tripRepository.registerTrips(tripList);

        log.info("process ended successfully!");
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

    @Override
    public List<Trip> createTripsFromTaps(List<Tap> tapsList) {
        List<Trip> allTrips = new ArrayList<>();
// creates a map of each specific pan and the related taps
        Map<String, List<Tap>> mapOfTapsByPan = ObjectMapper.mapTapListToMapOfPanAndTaps(tapsList);
        for (String pan : mapOfTapsByPan.keySet()) {
// creates related trips from the taps
            allTrips.addAll(createTripListOfASpecificPan(mapOfTapsByPan.get(pan)));
        }
        log.info("All the trips have been created.");
        return allTrips.stream().sorted(Comparator.comparing(Trip::getStarted)).collect(Collectors.toList());
    }

    private List<Trip> createTripListOfASpecificPan(List<Tap> taps) {
        List<Trip> tripList = new ArrayList<>();
        Trip trip = null;
        Tap previousTap = null;

        for (Tap tap : taps) {
            // if there is no tap off, trip will be completed here
            if (tap.getTapType() == TapType.ON && previousTap != null &&
                    isIncompleteTrip(previousTap, tap)) {
                fillEndOfTripForIncompleteTrips(trip);
                tripList.add(trip);
                previousTap = null;
            }
            // create a new trip for a new tap on
            if (tap.getTapType() == TapType.ON && previousTap == null) {
                previousTap = tap;
                trip = new Trip();
                fillStartOfTrip(tap, trip);
                // complete the trip object which tap on has created
            } else if (tap.getTapType() == TapType.OFF && trip != null) {
                fillEndOfTrip(tap, trip);
                tripList.add(trip);
                previousTap = null;
            }
        }
        // if there is no tap off for the last tap on, here the trip will be completed.
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
