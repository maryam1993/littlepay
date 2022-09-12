package com.littlepay.trip.repository.impl;

import com.littlepay.trip.dto.Tap;
import com.littlepay.trip.repository.TapRepository;
import com.littlepay.trip.util.Mapper;
import com.littlepay.trip.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TapRepositoryImpl implements TapRepository {

    @Value("${taps.file.name}")
    private String fileName;

    @Override
    public List<Tap> getTapsList() {
        return Mapper.mapToTapList(FileUtil.readFile(fileName));
    }


}
