package com.example.BankBranch.dto;

import com.example.BankBranch.model.Path;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoutResponse {
    private List<Path> paths;
    private double userLat;
    private double userLon;
    private double endLat;
    private double endLon;
}

/*
{   "hints": {"visited_nodes.sum":82,"visited_nodes.average":82.0},
    "info":{"copyrights":["GraphHopper","OpenStreetMap contributors"],"took":1},
    "paths":[{"distance":2130.125,"weight":291.231291,"time":259282,"transfers":0,"snapped_waypoints":"o|msIctyjHssArmB"}]}

 */