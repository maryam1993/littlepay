package com.littlepay.trip.util;

import com.littlepay.trip.exception.FileException;
import com.opencsv.*;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

@Slf4j
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
            log.error("Error while reading the file {}", fileName, e.getCause());
            throw new FileException(e.getMessage(), e.getCause());
        }
    }


    public static <T> void writeFile(String header, String fileName, List<T> data) {
        try (Writer writer = new FileWriter(fileName)) {
            writer.append(header).append("\n");

            StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .build();
            beanToCsv.write(data);

            writer.flush();
        } catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException ex) {
            throw new FileException(ex.getMessage(), ex.getCause());
        }
    }
}
