package com.littlepay.trip;

import com.littlepay.trip.dto.Tap;
import com.littlepay.trip.dto.Trip;
import com.littlepay.trip.enumeration.Stops;
import com.littlepay.trip.enumeration.TapType;
import com.littlepay.trip.enumeration.TripType;
import com.littlepay.trip.repository.impl.TripRepositoryImpl;
import com.littlepay.trip.resources.TripCharge;
import com.littlepay.trip.service.impl.TripServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TripApplicationTests {

    @InjectMocks
    private TripServiceImpl tripService;

    @Mock
    private TripRepositoryImpl tripRepository;

    private List<Tap> taps;

    @BeforeEach
    void setUp() {
        taps = new ArrayList<>();
        taps.add(getTapOn());
    }

    @Test
    void createTripsFromTaps_oneTapOnAndTapOffWithDifferentStop_isCompletedTrip() {
        taps.add(getTapOff(Stops.Stop3));
        when(tripRepository.getTripChargeList()).thenReturn(TripCharge.getTripsCharges());

        List<Trip> result = tripService.createTripsFromTaps(taps);

        assertAll(
                () -> assertEquals(1, result.size(), "Number of created trip failed."),
                () -> assertEquals(TripType.COMPLETED, result.get(0).getTripType(), "Trip type failed."),
                () -> assertEquals(7.30, result.get(0).getChargeAmount(), "Charge amount failed."),
                () -> assertEquals(300, result.get(0).getDurationSecs(), "Duration failed.")
        );
    }

    @Test
    void createTripsFromTaps_oneTapOnAndTapOffWithSameStop_isCanceledTrip() {
        Tap tapOff = getTapOff(Stops.Stop1);
        taps.add(tapOff);

        when(tripRepository.getTripChargeList()).thenReturn(TripCharge.getTripsCharges());

        List<Trip> result = tripService.createTripsFromTaps(taps);

        assertAll(
                () -> assertEquals(1, result.size(), "Number of created trip failed."),
                () -> assertEquals(TripType.CANCELLED, result.get(0).getTripType(), "Trip type failed."),
                () -> assertEquals(0, result.get(0).getChargeAmount(), "Charge amount failed."),
                () -> assertEquals(300, result.get(0).getDurationSecs(), "Duration failed.")
        );
    }

    @Test
    void createTripsFromTaps_oneTapOn_isIncompleteTrip() {

        when(tripRepository.getTripChargeList()).thenReturn(TripCharge.getTripsCharges());

        List<Trip> result = tripService.createTripsFromTaps(taps);

        assertAll(
                () -> assertEquals(1, result.size(), "Number of created trip failed."),
                () -> assertEquals(TripType.INCOMPLETE, result.get(0).getTripType(), "Trip type failed."),
                () -> assertEquals(7.30, result.get(0).getChargeAmount(), "Charge amount failed."),
                () -> assertNull(result.get(0).getDurationSecs(), "Duration failed.")
        );
    }

    @Test
    void createTripsFromTaps_twoTapOns_twoIncompleteTrips() {
        Tap anotherTapOn = getTapOn();
        anotherTapOn.setCompanyId("Company33");
        taps.add(anotherTapOn);

        when(tripRepository.getTripChargeList()).thenReturn(TripCharge.getTripsCharges());

        List<Trip> result = tripService.createTripsFromTaps(taps);

        assertAll(
                () -> {
                    assertEquals(2, result.size(), "Number of created trip failed.");
                    assertAll(
                            () -> assertEquals(TripType.INCOMPLETE, result.get(0).getTripType(), "Trip type failed."),
                            () -> assertEquals(7.30, result.get(0).getChargeAmount(), "Charge amount failed."),
                            () -> assertNull(result.get(0).getDurationSecs(), "Duration failed.")
                    );

                    assertAll(
                            () -> assertEquals(TripType.INCOMPLETE, result.get(1).getTripType(), "Trip type failed."),
                            () -> assertEquals(7.30, result.get(1).getChargeAmount(), "Charge amount failed."),
                            () -> assertNull(result.get(1).getDurationSecs(), "Duration failed.")
                    );
                });
    }

    @Test
    void createTripsFromTaps_twoTapOnsWithDifferentPans_twoIncompleteTrips() {
        Tap anotherTapOn = getTapOn();
        anotherTapOn.setPan("5019717010103742");
        taps.add(anotherTapOn);

        when(tripRepository.getTripChargeList()).thenReturn(TripCharge.getTripsCharges());

        List<Trip> result = tripService.createTripsFromTaps(taps);

        assertAll(
                () -> {
                    assertEquals(2, result.size(), "Number of created trip failed.");
                    assertAll(
                            () -> assertEquals(TripType.INCOMPLETE, result.get(0).getTripType(), "Trip type failed."),
                            () -> assertEquals(7.30, result.get(0).getChargeAmount(), "Charge amount failed."),
                            () -> assertEquals("5500005555555559", result.get(0).getPan(), "PAN failed."),
                            () -> assertNull(result.get(0).getDurationSecs(), "Duration failed.")
                    );

                    assertAll(
                            () -> assertEquals(TripType.INCOMPLETE, result.get(1).getTripType(), "Trip type failed."),
                            () -> assertEquals(7.30, result.get(1).getChargeAmount(), "Charge amount failed."),
                            () -> assertEquals("5019717010103742", result.get(1).getPan(), "PAN failed."),
                            () -> assertNull(result.get(1).getDurationSecs(), "Duration failed.")
                    );
                });
    }

    @Test
    void createTripsFromTaps_twoTapOnsAndTwoTapOffsWithSamePanAndDifferentTime_twoCompletedTrips() {
        Tap tapOn2 = getTapOn();
        tapOn2.setStopId(Stops.Stop3);
        tapOn2.setTapDateTime(LocalDateTime.of(2021, 5, 14, 20, 30, 0));
        taps.add(tapOn2);

        Tap tapOff1 = getTapOff(Stops.Stop2);
        taps.add(tapOff1);

        Tap tapOff2 = getTapOff(Stops.Stop1);
        tapOff2.setTapDateTime(LocalDateTime.of(2021, 5, 14, 20, 37, 0));
        taps.add(tapOff2);

        when(tripRepository.getTripChargeList()).thenReturn(TripCharge.getTripsCharges());

        List<Trip> result = tripService.createTripsFromTaps(taps);

        assertAll(
                () -> {
                    assertEquals(2, result.size(), "Number of created trip failed.");
                    assertAll(
                            () -> assertEquals(TripType.COMPLETED, result.get(0).getTripType(), "Trip type failed."),
                            () -> assertEquals(3.25, result.get(0).getChargeAmount(), "Charge amount failed."),
                            () -> assertEquals(300, result.get(0).getDurationSecs(), "Duration failed.")
                    );
                    assertAll(
                            () -> assertEquals(TripType.COMPLETED, result.get(1).getTripType(), "Trip type failed."),
                            () -> assertEquals(7.30, result.get(1).getChargeAmount(), "Charge amount failed."),
                            () -> assertEquals(420, result.get(1).getDurationSecs(), "Duration failed.")
                    );
                });
    }

    @Test
    void createTripsFromTaps_twoTapOnsAndOneTapOffWithSamePan_OneCompletedAndOneIncompleteTrips() {
        Tap tapOff1 = getTapOff(Stops.Stop2);
        taps.add(tapOff1);

        Tap tapOn2 = getTapOn();
        tapOn2.setStopId(Stops.Stop2);
        tapOn2.setTapDateTime(LocalDateTime.of(2021, 5, 14, 20, 47, 0));
        taps.add(tapOn2);

        when(tripRepository.getTripChargeList()).thenReturn(TripCharge.getTripsCharges());

        List<Trip> result = tripService.createTripsFromTaps(taps);

        assertAll(
                () -> {
                    assertEquals(2, result.size(), "Number of created trip failed.");
                    assertAll(
                            () -> assertEquals(TripType.COMPLETED, result.get(0).getTripType(), "Trip type failed."),
                            () -> assertEquals(3.25, result.get(0).getChargeAmount(), "Charge amount failed."),
                            () -> assertEquals(300, result.get(0).getDurationSecs(), "Duration failed.")
                    );
                    assertAll(
                            () -> assertEquals(TripType.INCOMPLETE, result.get(1).getTripType(), "Trip type failed."),
                            () -> assertEquals(5.5, result.get(1).getChargeAmount(), "Charge amount failed."),
                            () -> assertNull(result.get(1).getDurationSecs(), "Duration failed.")
                    );
                });
    }

    private Tap getTapOn() {
        Tap tapOn = new Tap();

        tapOn.setTapType(TapType.ON);
        tapOn.setBusId("Bus4");
        tapOn.setPan("5500005555555559");
        tapOn.setTapDateTime(LocalDateTime.of(2021, 5, 14, 15, 17, 0));
        tapOn.setStopId(Stops.Stop1);

        return tapOn;
    }

    private Tap getTapOff(Stops stopId) {
        Tap tapOff = new Tap();

        tapOff.setTapType(TapType.OFF);
        tapOff.setBusId("Bus4");
        tapOff.setPan("5500005555555559");
        tapOff.setTapDateTime(LocalDateTime.of(2021, 5, 14, 15, 22, 0));
        tapOff.setStopId(stopId);

        return tapOff;
    }
}


