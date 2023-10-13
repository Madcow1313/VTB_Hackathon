package com.example.BankBranch.controller;

import com.example.BankBranch.dto.PlacesResponse;
import com.example.BankBranch.dto.RoutResponse;
import com.example.BankBranch.model.Point;
import com.example.BankBranch.service.GraphHopperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

        PlacesResponse placesResponse = graphHopperService.getPlaceResponse(place);

        System.out.println(placesResponse);

        RoutResponse routResponse = restTemplate.getForObject(routUrl, RoutResponse.class);

        System.out.println();

        System.out.println(routResponse);

        return "greeting";
    }

    @PostMapping("/findPlace")
    public String findPlace(@RequestParam(required = false, defaultValue = "") String findPlace,
                            Model model) {

        PlacesResponse placesResponse = null;
        System.out.println(findPlace);
        if (!findPlace.isEmpty()) {
            placesResponse = graphHopperService.getPlaceResponse(findPlace);
            model.addAttribute("point", placesResponse.getHits().get(0));
            model.addAttribute("pointLat", placesResponse.getHits().get(0).getPoint().getLat());
            model.addAttribute("pointLng", placesResponse.getHits().get(0).getPoint().getLng());
        }

        if (placesResponse != null) {
            System.out.println("pointLat = " + placesResponse.getHits().get(0).getPoint().getLat() +
                    "\npointLng = " + placesResponse.getHits().get(0).getPoint().getLng());
        }


        return "greeting";
    }

    @PostMapping("/buildRoute")
    public String createPath(@RequestParam(required = false, defaultValue = "") String address1,
                             @RequestParam(required = false, defaultValue = "") String address2,
                             Model model) throws IOException {
        System.out.println(address1 + address2);

        Point point1 = graphHopperService.getPlaceResponse(address1).getFirstPoint();
        Point point2 = graphHopperService.getPlaceResponse(address2).getFirstPoint();

        model.addAttribute("point1", point1);
        model.addAttribute("point2", point2);

        return "buildRoute";
    }


    @GetMapping("/yandex")
    public String yandex(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "yandex";
    }

}