package com.example.BankBranch.dto;


import lombok.Data;

@Data
public class InputData {
    private LatLng latlng;
    private String service;
    private String user_type;
    private String vehicle;

    public double getLat() {
        return latlng.getLat();
    }

    public double getLng() {
        return latlng.getLng();
    }
}

@Data
class LatLng {
    private double lat;
    private double lng;
}