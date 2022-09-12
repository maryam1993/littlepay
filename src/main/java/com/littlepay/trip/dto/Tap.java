package com.littlepay.trip.dto;

import com.littlepay.trip.enumeration.Stops;
import com.littlepay.trip.enumeration.TapType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Tap {

    private String Id;
    private LocalDateTime tapDateTime;
    private TapType tapType;
    private Stops stopId;
    private String companyId;
    private String busId;
    private String pan;

}
