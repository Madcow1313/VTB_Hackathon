package com.example.BankBranch.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GHGeocodingEntry {
    private Point point;
    private String name;
    private String country;
    private String countrycode;
    private String city;
    private String state;
    private String street;
    private String postcode;
    private long osm_id;
    private String osm_type;
    private String housenumber;
    private String osm_key;
    private String osm_value;
    private String house_number;
}
