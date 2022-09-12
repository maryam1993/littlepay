package com.littlepay.trip.util;

import com.littlepay.trip.exception.FileException;
import com.littlepay.trip.dto.Trip;
import com.opencsv.*;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class FileUtil {

    public static List<String[]> readFile(String fileName) {
        try {
            CSVParser parser = new CSVParserBuilder().build();
            CSVReader reader = new CSVReaderBuilder(new FileReader(fileName)).
                    withSkipLines(1).// Skipping first line as it is the header
                            withCSVParser(parser).
                    build();
            return reader.readAll();
        } catch (IOException | CsvException e) {
            throw new FileException(e.getMessage(), e.getCause());
        }
    }

    public static void writeFile(String header, String fileName, List<Trip> data) {
        try (Writer writer = new FileWriter(fileName)) {
            writer.append(header).append("\n");

            StatefulBeanToCsv<Trip> beanToCsv = new StatefulBeanToCsvBuilder<Trip>(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .build();
            beanToCsv.write(data);

            writer.flush();
        } catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException ex) {
            throw new FileException(ex.getMessage(), ex.getCause());
        }
    }
}
