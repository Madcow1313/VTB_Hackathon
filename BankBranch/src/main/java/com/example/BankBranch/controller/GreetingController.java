package com.example.BankBranch.controller;

import com.example.BankBranch.dto.PlacesResponse;
import com.example.BankBranch.dto.RoutResponse;
import com.example.BankBranch.dto.SalePointDto;
import com.example.BankBranch.model.Point;
import com.example.BankBranch.service.GraphHopperService;
import com.example.BankBranch.storage.Storage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class GreetingController {

    private static final double USER_Latitude = 55.755864;
    private static final double USER_Longitude = 37.617698;

    @Value("${graphhopper.api.key}")
    private String graphhopperApiKey;

    @Autowired
    private GraphHopperService graphHopperService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Storage storage;

    @GetMapping("/")
    public String greeting(@RequestParam(name = "data", required = false, defaultValue = "") String data,
                           Model model) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(data);
            JsonNode latlngNode = jsonNode.get("latlng");

            if (latlngNode != null && latlngNode.has("lat") && latlngNode.has("lng")) {
                double lat = latlngNode.get("lat").asDouble();
                double lng = latlngNode.get("lng").asDouble();

                System.out.println("lat: " + lat);
                System.out.println("lng: " + lng);

                Point userPoint = new Point(USER_Latitude, USER_Longitude);

                List<SalePointDto> salePointDtoList = graphHopperService.findNearestBranches(USER_Latitude, USER_Longitude, storage.getSalePointDtoList());
                salePointDtoList.forEach(System.out::println);

                Optional<RoutResponse> optionalRoutResponse = graphHopperService.findOptimalRoute(salePointDtoList, userPoint);

                if (optionalRoutResponse.isPresent()) {
                    model.addAttribute("endLat", optionalRoutResponse.get().getEndLat());
                    model.addAttribute("endLon", optionalRoutResponse.get().getEndLon());

                    System.out.println("######## endLat " + optionalRoutResponse.get().getEndLat());
                    System.out.println("######## endLon " + optionalRoutResponse.get().getEndLon());
                }

            } else {
                System.out.println("Invalid JSON structure: latlng or its components are missing.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "greeting";
    }


//    @GetMapping("/")
//    public String handleJsonRequest(@RequestBody YourRequestClass request) {
//        double lat = request.getLat();
//        double lng = request.getLng();
//
//        // Теперь у вас есть значения lat и lng, которые вы можете использовать в вашей логике
//        System.out.println("lat: " + lat);
//        System.out.println("lng: " + lng);
//
//        // Ваша логика обработки запроса...
//
//        return "greeting";
//    }


    /*
//        System.out.println(graphHopperService.findRoute(55.781863, 49.124884,55.795343, 49.106515));
//        System.out.println(graphHopperService.findPlace("Казань, Школа 21"));
//        double startLat = 55.840263;
//        double startLon = 49.081644;
//        double endLat = 55.750481;
//        double endLon = 49.209544;
//
//        String routUrl = String.format("https://graphhopper.com/api/1/route?point=%s,%s&point=%s,%s&profile=car&locale=de&calc_points=false&key=%s",
//                startLat,
//                startLon,
//                endLat,
//                endLon,
//                graphhopperApiKey);
//
//        String place = "Казань, школа 21";
//
//        String placeUrl = String.format("https://graphhopper.com/api/1/geocode?q=%s&locale=de&key=%s",
//                place,
//                graphhopperApiKey);
//
//        PlacesResponse placesResponse = graphHopperService.getPlaceResponse(place);
//
//        System.out.println(placesResponse);
//
//        RoutResponse routResponse = restTemplate.getForObject(routUrl, RoutResponse.class);
//
//        System.out.println();
//
//        System.out.println(routResponse);*/

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


}