package com.littlepay.trip.util;

import com.littlepay.trip.constant.DateConstant;
import com.littlepay.trip.dto.Tap;
import com.littlepay.trip.enumeration.Stops;
import com.littlepay.trip.enumeration.TapType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Mapper {

    public static List<Tap> mapToTapList(List<String[]> tapData) {

        return tapData.stream().map(data -> {
            Tap tap = new Tap();
            tap.setId(data[0]);
            tap.setTapDateTime(LocalDateTime.parse(data[1],
                    DateTimeFormatter.ofPattern(DateConstant.DATE_TIME_FORMAT)));
            tap.setTapType(TapType.valueOf(data[2]));
            tap.setStopId(Stops.valueOf(data[3]));
            tap.setCompanyId(data[4]);
            tap.setBusId(data[5]);
            tap.setPan(data[6]);
            return tap;
        }).sorted(Comparator.comparing(Tap::getTapDateTime)).collect(Collectors.toList());
    }

    public static Map<String, List<Tap>> mapTapListToMapOfPanAndTaps(List<Tap> taps) {
        return taps.stream().collect(Collectors.groupingBy(Tap::getPan));
    }
}
