package com.example.BankBranch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Path {
    private double distance;
    private double weight;
    private int time;
    private int transfers;
    @JsonAlias("snapped_waypoints")
    private String snappedWaypoints;

}

/*
{"hints":
    {"visited_nodes.sum":82,"visited_nodes.average":82.0},
    "info":{"copyrights":["GraphHopper","OpenStreetMap contributors"],"took":1},
    "paths":[{"distance":2130.125,"weight":291.231291,"time":259282,"transfers":0,"snapped_waypoints":"o|msIctyjHssArmB"}]}

 */