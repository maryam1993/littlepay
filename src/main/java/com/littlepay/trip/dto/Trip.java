package com.littlepay.trip.dto;

import com.littlepay.trip.enumeration.Stops;
import com.littlepay.trip.enumeration.TripType;
import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Trip {
    @CsvBindByPosition(position = 0)
    private LocalDateTime started;

    @CsvBindByPosition(position = 1)
    private LocalDateTime finished;

    @CsvBindByPosition(position = 2)
    private Long durationSecs;

    @CsvBindByPosition(position = 3)
    private Stops fromStopId;

    @CsvBindByPosition(position = 4)
    private Stops toStopId;

    @CsvBindByPosition(position = 5)
    private Double chargeAmount;

    @CsvBindByPosition(position = 6)
    private String companyId;

    @CsvBindByPosition(position = 7)
    private String BusId;

    @CsvBindByPosition(position = 8)
    private String pan;

    @CsvBindByPosition(position = 9)
    private TripType tripType;

}
