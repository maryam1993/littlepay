package com.littlepay.trip.resources;

import com.littlepay.trip.enumeration.Stops;

public class TripCharge {

    private static final double[][] tripsCharges = new double[5][5];

    public static double[][] getTripsCharges() {
        tripsCharges[Stops.Stop1.getValue()][Stops.Stop1.getValue()] = 0;
        tripsCharges[Stops.Stop1.getValue()][Stops.Stop2.getValue()] = 3.25;
        tripsCharges[Stops.Stop1.getValue()][Stops.Stop3.getValue()] = 7.30;

        tripsCharges[Stops.Stop2.getValue()][Stops.Stop1.getValue()] = 3.25;
        tripsCharges[Stops.Stop2.getValue()][Stops.Stop2.getValue()] = 0;
        tripsCharges[Stops.Stop2.getValue()][Stops.Stop3.getValue()] = 5.50;


        tripsCharges[Stops.Stop3.getValue()][Stops.Stop1.getValue()] = 7.30;
        tripsCharges[Stops.Stop3.getValue()][Stops.Stop2.getValue()] = 5.50;
        tripsCharges[Stops.Stop3.getValue()][Stops.Stop3.getValue()] = 0;

        return tripsCharges;
    }
}
