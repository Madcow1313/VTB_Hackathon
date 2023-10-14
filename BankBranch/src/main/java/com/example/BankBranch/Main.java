package com.example.BankBranch;

import com.example.BankBranch.model.SalePoint;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {


//        String filePath = "BankBranch/src/main/resources/offices_test.json";
        String filePath = "BankBranch/src/main/resources/offices.json";

        try {


            String jsonString = readFileAsString(filePath);

            ObjectMapper objectMapper = new ObjectMapper();
            SalePoint[] salePoints = objectMapper.readValue(jsonString, SalePoint[].class);

            System.out.println("salePoints.length " + salePoints.length);

            Arrays.stream(salePoints)
                    .forEach(salePoint -> {
                        String address = salePoint.getAddress();
                        int indexOfCity = address.indexOf("г. ");
                        if (indexOfCity != -1) {
                            int indexOfComma = address.indexOf(',', indexOfCity);
                            if (indexOfComma != -1) {
                                String city = address.substring(indexOfCity + 3, indexOfComma).trim();
                                salePoint.setCity(city);
                            }
                        }
                    });

            System.out.println(salePoints[0]);

//            List<String> cities = Arrays.stream(salePoints)
//                    .map(SalePoint::getCity)
//                    .filter(city -> city != null && !city.isEmpty())
//                    .distinct()
//                    .collect(Collectors.toList());
//
//            System.out.println("cities = " + cities.size());
//
//            cities.forEach(System.out::println);

            Map<String, Long> cityCountMap = Arrays.stream(salePoints)
                    .filter(salePoint -> salePoint.getCity() != null && !salePoint.getCity().isEmpty())
                    .collect(Collectors.groupingBy(SalePoint::getCity, Collectors.counting()));

            cityCountMap.forEach((city, count) -> {
                System.out.println(city + ": " + count);
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String readFileAsString(String filePath) throws IOException {

        Path path = Paths.get(filePath).toAbsolutePath().normalize();

        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
