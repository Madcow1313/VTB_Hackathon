package com.example.BankBranch;

import com.example.BankBranch.dto.SalePointDto;
import com.example.BankBranch.model.SalePoint;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    private static final int NUM_NEAREST_BRANCHES = 250;
    private static final double USER_Latitude = 55.755864;
    private static final double USER_Longitude = 37.617698;

    public static void main(String[] args) {


//        String filePath = "BankBranch/src/main/resources/offices_test.json";
        String filePath = "BankBranch/src/main/resources/offices.json";

        try {


            String jsonString = readFileAsString(filePath);

            ObjectMapper objectMapper = new ObjectMapper();
//            SalePoint[] salePoints = objectMapper.readValue(jsonString, SalePoint[].class);
//
//            System.out.println("salePoints.length " + salePoints.length);
//
//            Arrays.stream(salePoints)
//                    .forEach(salePoint -> {
//                        String address = salePoint.getAddress();
//                        int indexOfCity = address.indexOf("г. ");
//                        if (indexOfCity != -1) {
//                            int indexOfComma = address.indexOf(',', indexOfCity);
//                            if (indexOfComma != -1) {
//                                String city = address.substring(indexOfCity + 3, indexOfComma).trim();
//                                salePoint.setCity(city);
//                            }
//                        }
//                    });
//
//            System.out.println(salePoints[0]);

//            List<String> cities = Arrays.stream(salePoints)
//                    .map(SalePoint::getCity)
//                    .filter(city -> city != null && !city.isEmpty())
//                    .distinct()
//                    .collect(Collectors.toList());
//
//            System.out.println("cities = " + cities.size());
//
//            cities.forEach(System.out::println);

//            Map<String, Long> cityCountMap = Arrays.stream(salePoints)
//                    .filter(salePoint -> salePoint.getCity() != null && !salePoint.getCity().isEmpty())
//                    .collect(Collectors.groupingBy(SalePoint::getCity, Collectors.counting()));
//
//            cityCountMap.forEach((city, count) -> {
//                System.out.println(city + ": " + count);
//            });


            SalePointDto[] salePointsDto = objectMapper.readValue(jsonString, SalePointDto[].class);
            List<SalePointDto> nearestBranches = findNearestBranches(USER_Latitude, USER_Longitude, Arrays.stream(salePointsDto).toList());
            nearestBranches.forEach(System.out::println);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String readFileAsString(String filePath) throws IOException {

        Path path = Paths.get(filePath).toAbsolutePath().normalize();

        return Files.readString(path, StandardCharsets.UTF_8);
    }


    public static List<SalePointDto> findNearestBranches(double userLatitude, double userLongitude, List<SalePointDto> branches) {
        return branches.stream()
                .filter(branch -> branch.getLatitude() != 0 && branch.getLongitude() != 0)
                .peek(branch -> {
                    double branchLatitude = branch.getLatitude();
                    double branchLongitude = branch.getLongitude();
                    double distance = calculateDistance(userLatitude, userLongitude, branchLatitude, branchLongitude);
                    branch.setDistanceToClient(distance);
                })
                .sorted(Comparator.comparingDouble(SalePointDto::getDistanceToClient))
                .limit(NUM_NEAREST_BRANCHES)
                .collect(Collectors.toList());
    }

    private static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Радиус Земли в километрах

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c * 1000;
    }


}
