package com.example.BankBranch.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
import java.util.Set;

//todo тут должна быть ентити
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SalePoint {

    private Long id;

    private String salePointName;

    private String city;

    private String address;

    private String salePointCode;

    private String status;

    private List<OpenHours> openHours;

    private String rko;

    private String network;

    private List<OpenHours> openHoursIndividual;

    private String officeType;

    private String salePointFormat;

    private String suoAvailability;

    private String hasRamp;

    private double latitude;

    private double longitude;

    private String metroStation;

    private int distance;

    private boolean kep;

    private boolean myBranch;

    private Set<String> servicesIndividual;
    private Set<String> servicesLegalEntity;
    private Long workload;
}