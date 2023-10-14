package com.example.BankBranch.service;

import com.example.BankBranch.dto.PlacesResponse;
import com.example.BankBranch.dto.RoutResponse;
import com.example.BankBranch.dto.SalePointDto;
import com.example.BankBranch.model.Point;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@Service
public class GraphHopperService {

    private static final int NUM_NEAREST_BRANCHES = 3;
//    private static final double USER_Latitude = 55.755864;
//    private static final double USER_Longitude = 37.617698;

    @Value("${graphhopper.api.key}")
    private String graphhopperApiKey;

    @Autowired
    private OkHttpClient client;

    @Autowired
    private RestTemplate restTemplate;

    public RoutResponse findRoute(double userLat, double userLon, double endLat, double endLon) throws IOException {

        Request request = new Request.Builder()
                .url("https://graphhopper.com/api/1/route?point=" + userLat + "," + userLon + "&point=" + endLat + "," + endLon + "&profile=car&locale=de&calc_points=false&key=" + graphhopperApiKey)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                ObjectMapper objectMapper = new ObjectMapper();
                String responseBody = response.body().string();
                RoutResponse routResponse = objectMapper.readValue(responseBody, RoutResponse.class);
                routResponse.setUserLat(userLat);
                routResponse.setUserLon(userLon);
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


    public List<SalePointDto> findNearestBranches(double userLatitude, double userLongitude, List<SalePointDto> branches) {
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

    public Optional<RoutResponse> findOptimalRoute(List<SalePointDto> salePointDtoList, Point userPoint) throws IOException {

        List<RoutResponse> routResponses = new ArrayList<>();

        for (SalePointDto spd : salePointDtoList) {
            routResponses.add(findRoute(userPoint.getLat(), userPoint.getLng(), spd.getLatitude(), spd.getLongitude()));
        }

        return routResponses.stream().min(Comparator.comparingInt(response -> response.getPaths().get(0).getTime()));
    }
}
