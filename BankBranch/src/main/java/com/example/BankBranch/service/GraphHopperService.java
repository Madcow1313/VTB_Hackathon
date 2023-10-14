package com.example.BankBranch.service;

import com.example.BankBranch.dto.InputData;
import com.example.BankBranch.dto.PlacesResponse;
import com.example.BankBranch.dto.RoutResponse;
import com.example.BankBranch.dto.SalePointDto;
import com.example.BankBranch.storage.Storage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class GraphHopperService {

    private static final int NUM_NEAREST_BRANCHES = 20;
//    private static final double USER_Latitude = 55.755864;
//    private static final double USER_Longitude = 37.617698;

    @Value("${graphhopper.api.key}")
    private String graphhopperApiKey;

    @Autowired
    private OkHttpClient client;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Storage storage;

    public RoutResponse findRoute(InputData inputData, double endLat, double endLon) throws IOException {

        //todo это жесткий упоротый хардкод, руки за такое оторвать
        String vehicle;
        if ("YES".equalsIgnoreCase(inputData.getVehicle())) {
            vehicle = "car";
        } else {
            vehicle = "foot";
        }

        Request request = new Request.Builder()
                //todo ну можно же было нормально написать через билдер
                .url("https://graphhopper.com/api/1/route?point=" + inputData.getLat() + "," + inputData.getLng() + "&point=" + endLat + "," + endLon + "&profile=" + vehicle + "&locale=de&calc_points=false&key=" + graphhopperApiKey)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                ObjectMapper objectMapper = new ObjectMapper();
                String responseBody = response.body().string();
                RoutResponse routResponse = objectMapper.readValue(responseBody, RoutResponse.class);
                routResponse.setUserLat(inputData.getLat());
                routResponse.setUserLon(inputData.getLng());
                routResponse.setEndLat(endLat);
                routResponse.setEndLon(endLon);
                return routResponse;
            } else {
                // Обработка ошибки, если ответ не успешен
                throw new IOException("Unexpected code " + response);
            }
        }
    }

    public String findPlace(String place) throws IOException {

        Request request = new Request.Builder()
                .url("https://graphhopper.com/api/1/geocode?q=" + place + "&locale=de&key=" + graphhopperApiKey)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                // Обработка ошибки, если ответ не успешен
                throw new IOException("Unexpected code " + response);
            }
        }
    }


    public PlacesResponse getPlaceResponse(String place) {
        String placeUrl = String.format("https://graphhopper.com/api/1/geocode?q=%s&locale=de&key=%s",
                place,
                graphhopperApiKey);
        return restTemplate.getForObject(placeUrl, PlacesResponse.class);
    }


    public List<SalePointDto> findNearestBranches(InputData request, List<SalePointDto> branches) {

        /*
        * user_type=physical
        * user_type=company
        *
        * service=ATM
        * service=credit
        * service=acquiring
        * service=leasing
        * */
        return branches.stream()
                .filter(branch -> branch.getLatitude() != 0 && branch.getLongitude() != 0)
                .peek(branch -> {
                    double branchLatitude = branch.getLatitude();
                    double branchLongitude = branch.getLongitude();
                    double distance = calculateDistance(request.getLat(), request.getLng(), branchLatitude, branchLongitude);
                    branch.setDistanceToClient(distance);
                })
                .sorted(Comparator.comparingDouble(SalePointDto::getDistanceToClient))
                .limit(NUM_NEAREST_BRANCHES)
                .collect(Collectors.toList());
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Радиус Земли в километрах

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c * 1000;
    }

    public Optional<RoutResponse> findOptimalRoute(InputData request) throws IOException {

        List<SalePointDto> salePointDtoList = findNearestBranches(request, storage.getSalePointDtoList());
        salePointDtoList.forEach(System.out::println);

        List<RoutResponse> routResponses = new ArrayList<>();

        for (SalePointDto spd : salePointDtoList) {
            RoutResponse routResponse = findRoute(request, spd.getLatitude(), spd.getLongitude());
            routResponse.setTimeWithWorkLoad(spd.getWorkload() + routResponse.getPaths().get(0).getTime());
            routResponses.add(routResponse);

        }

        routResponses.forEach(System.out::println);

        Optional<RoutResponse> optionalRoutResponse = routResponses.stream().min(Comparator.comparingInt(response -> (int) response.getTimeWithWorkLoad()));

        log.info("optionalRoutResponse = {}", optionalRoutResponse);

        return optionalRoutResponse;
    }
}
