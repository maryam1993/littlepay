package com.littlepay.trip.constant;

public class TripCharge {

    private static final double[][] tripsCharges = new double[5][5];

    public static double[][] getTripsCharges() {
        tripsCharges[0][0] = 0;
        tripsCharges[0][1] = 3.25;
        tripsCharges[0][2] = 7.30;

        tripsCharges[1][0] = 3.25;
        tripsCharges[1][1] = 0;
        tripsCharges[1][2] = 5.50;


        tripsCharges[2][0] = 7.30;
        tripsCharges[2][1] = 5.50;
        tripsCharges[2][2] = 0;

        return tripsCharges;
    }
}
