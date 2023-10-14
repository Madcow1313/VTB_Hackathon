package com.example.BankBranch.controller;

import com.example.BankBranch.dto.InputData;
import com.example.BankBranch.dto.RoutResponse;
import com.example.BankBranch.dto.SalePointDto;
import com.example.BankBranch.model.Point;
import com.example.BankBranch.service.GraphHopperService;
import com.example.BankBranch.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class GreetingController {

    @Value("${graphhopper.api.key}")
    private String graphhopperApiKey;

    @Autowired
    private GraphHopperService graphHopperService;

    @Autowired
    private Storage storage;

    @GetMapping("/")
    public String greeting() {
        return "greeting";
    }

    @PostMapping("/")
    public String handleJsonRequest(@RequestBody InputData request,
                                    Model model) {
        try {

            if (request != null) {
                double lat = request.getLat();
                double lng = request.getLng();

                System.out.println("lat: " + lat);
                System.out.println("lng: " + lng);

                Point userPoint = new Point(lat, lng);

                List<SalePointDto> salePointDtoList = graphHopperService.findNearestBranches(lat, lng, storage.getSalePointDtoList());
                salePointDtoList.forEach(System.out::println);

                Optional<RoutResponse> optionalRoutResponse = graphHopperService.findOptimalRoute(salePointDtoList, userPoint);

                if (optionalRoutResponse.isPresent()) {
                    model.addAttribute("endLat", optionalRoutResponse.get().getEndLat());
                    model.addAttribute("endLon", optionalRoutResponse.get().getEndLon());
                    model.addAttribute("message", "true");

                    System.out.println("######## endLat " + optionalRoutResponse.get().getEndLat());
                    System.out.println("######## endLon " + optionalRoutResponse.get().getEndLon());
                }
                model.addAttribute("message", "false");
                model.addAttribute("allSalePoint", storage.getSalePointList() );

            } else {
                System.out.println("Invalid JSON structure: latlng or its components are missing.");
            }

            System.out.println(request);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return "greeting";
    }
}