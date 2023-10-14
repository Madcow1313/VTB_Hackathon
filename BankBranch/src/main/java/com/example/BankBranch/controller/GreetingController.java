package com.example.BankBranch.controller;

import com.example.BankBranch.dto.InputData;
import com.example.BankBranch.dto.RoutResponse;
import com.example.BankBranch.service.GraphHopperService;
import com.example.BankBranch.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
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

                System.out.println("UserLat: " + request.getLat());
                System.out.println("UserLng: " + request.getLng());

                Optional<RoutResponse> optionalRoutResponse = graphHopperService.findOptimalRoute(request);

                if (optionalRoutResponse.isPresent()) {
                    model.addAttribute("endLat", optionalRoutResponse.get().getEndLat());
                    model.addAttribute("endLon", optionalRoutResponse.get().getEndLon());
                    model.addAttribute("message", "true");

                    System.out.println("######## endLat " + optionalRoutResponse.get().getEndLat());
                    System.out.println("######## endLon " + optionalRoutResponse.get().getEndLon());
                }
                model.addAttribute("message", "false");
                model.addAttribute("allSalePoint", storage.getSalePointList());

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