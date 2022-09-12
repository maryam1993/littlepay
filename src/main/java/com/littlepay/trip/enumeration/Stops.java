package com.littlepay.trip.enumeration;

public enum Stops {
    Stop1(0), Stop2(1), Stop3(2);

    private final int value;

    Stops(int value) {
        this.value = value;
    }

    public int getValue(){
        return value;
    }
}
