package com.littlepay.trip.repository.impl;

import com.littlepay.trip.dto.Tap;
import com.littlepay.trip.repository.TapRepository;
import com.littlepay.trip.util.FileUtil;
import com.littlepay.trip.util.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class TapRepositoryImpl implements TapRepository {

    @Value("${taps.file.name}")
    private String fileName;

    @Override
    public List<Tap> getTapsList() {
        log.info("In the process of reading the file {}", fileName);
        return ObjectMapper.mapToTapList(FileUtil.readFile(fileName));
    }


}
