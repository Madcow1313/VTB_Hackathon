package com.example.BankBranch.storage;


import com.example.BankBranch.dto.SalePointDto;
import com.example.BankBranch.model.SalePoint;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Component
@Log4j2
public class Storage {
    private List<SalePoint> salePointList;

    @PostConstruct
    public void initialize() {
        salePointList = new ArrayList<>();

        String filePath = "BankBranch/src/main/resources/offices.json";

        try {
            String jsonString = readFileAsString(filePath);
            ObjectMapper objectMapper = new ObjectMapper();
            SalePoint[] salePoints = objectMapper.readValue(jsonString, SalePoint[].class);
            salePointList.addAll(Arrays.asList(salePoints));

            log.info("Storage salePointList.size() = {}", salePointList.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String readFileAsString(String filePath) throws IOException {

        Path path = Paths.get(filePath).toAbsolutePath().normalize();

        return Files.readString(path, StandardCharsets.UTF_8);
    }

    private SalePointDto convertToDto(SalePoint salePoint) {
        return SalePointDto.builder()
                .latitude(salePoint.getLatitude())
                .longitude(salePoint.getLongitude())
                .build();
    }

    public List<SalePointDto> getSalePointDtoList() {
        return salePointList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
