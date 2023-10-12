package com.example.BankBranch.controller;

import com.example.BankBranch.dto.PlacesResponse;
import com.example.BankBranch.dto.RoutResponse;
import com.example.BankBranch.service.GraphHopperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Controller
public class GreetingController {

    @Value("${graphhopper.api.key}")
    private String graphhopperApiKey;

    @Autowired
    private GraphHopperService graphHopperService;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/")
    public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) throws IOException {
        model.addAttribute("name", name);

//        System.out.println(graphHopperService.findRoute(55.781863, 49.124884,55.795343, 49.106515));
//        System.out.println(graphHopperService.findPlace("Казань, Школа 21"));
        double startLat = 55.840263;
        double startLon = 49.081644;
        double endLat = 55.750481;
        double endLon = 49.209544;

        String routUrl = String.format("https://graphhopper.com/api/1/route?point=%s,%s&point=%s,%s&profile=car&locale=de&calc_points=false&key=%s",
                startLat,
                startLon,
                endLat,
                endLon,
                graphhopperApiKey);

        String place = "Казань, школа 21";

        String placeUrl = String.format("https://graphhopper.com/api/1/geocode?q=%s&locale=de&key=%s",
                place,
                graphhopperApiKey);

        PlacesResponse placesResponse = restTemplate.getForObject(placeUrl, PlacesResponse.class);

        System.out.println(placesResponse);

        RoutResponse routResponse = restTemplate.getForObject(routUrl, RoutResponse.class);

        System.out.println();

        System.out.println(routResponse);

        return "greeting";
    }

    @GetMapping("/yandex")
    public String yandex(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "yandex";
    }

}