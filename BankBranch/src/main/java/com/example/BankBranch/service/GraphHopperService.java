package com.example.BankBranch.service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GraphHopperService {

    @Value("${graphhopper.api.key}")
    private String graphhopperApiKey;

    @Autowired
    private OkHttpClient client;

    public String findRoute(double startLat, double startLon, double endLat, double endLon) throws IOException {

        Request request = new Request.Builder()
                .url("https://graphhopper.com/api/1/route?point=" + startLat + "," + startLon + "&point=" + endLat + "," + endLon + "&profile=car&locale=de&calc_points=false&key=" + graphhopperApiKey)
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

    public String findPlace (String place) throws IOException {

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


}
