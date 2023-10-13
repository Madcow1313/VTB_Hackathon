package com.example.BankBranch.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;


@Data
//@JsonIgnoreProperties(ignoreUnknown = true)
public class SalePoint {
    @JsonProperty("salePointName")
    private String salePointName;

    @JsonProperty("address")
    private String address;

    @JsonProperty("salePointCode")
    private String salePointCode;

    @JsonProperty("status")
    private String status;

    @JsonProperty("openHours")
    private List<OpenHours> openHours;

    @JsonProperty("rko")
    private String rko;

    @JsonProperty("network")
    private String network;

    @JsonProperty("openHoursIndividual")
    private List<OpenHours> openHoursIndividual;

    @JsonProperty("officeType")
    private String officeType;

    @JsonProperty("salePointFormat")
    private String salePointFormat;

    @JsonProperty("suoAvailability")
    private String suoAvailability;

    @JsonProperty("hasRamp")
    private String hasRamp;

    @JsonProperty("latitude")
    private double latitude;

    @JsonProperty("longitude")
    private double longitude;

    @JsonProperty("metroStation")
    private String metroStation;

    @JsonProperty("distance")
    private int distance;

    @JsonProperty("kep")
    private boolean kep;

    @JsonProperty("myBranch")
    private boolean myBranch;

    // Добавьте геттеры и сеттеры
}