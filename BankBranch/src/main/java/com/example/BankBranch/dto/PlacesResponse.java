package com.example.BankBranch.dto;

import com.example.BankBranch.model.GHGeocodingEntry;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlacesResponse {
    List<GHGeocodingEntry> hits;
}

/*
{"hits":[
        {"point":{"lat":55.8109569,"lng":49.1883779},
        "name":"Школа-интернат для детей с ограниченными возможностями",
        "country":"Russland",
        "countrycode":"RU",
        "city":"Kasan",
        "state":"Tatarstan",
        "street":"улица Александра Попова",
        "postcode":"420029",
        "osm_id":7711010885,
        "osm_type":"N",
        "housenumber":"21"
        ,"osm_key":"amenity",
        "osm_value":"school","house_number":"21"}
        ,{"point":{"lat":55.78182554999999,"lng":49.12469026037388},
        "extent":[49.1242186,55.7815838,49.1254779,55.7820544],
        "name":"Школа 21",
        "country":"Russland",
        "countrycode":"RU",
        "city":"Kasan",
        "state":"Tatarstan",
        "street":"Спартаковская улица",
        "postcode":"420107",
        "osm_id":89534088,
        "osm_type":"W",
        "housenumber":"2 к2",
        "osm_key":"amenity",
        "osm_value":"training",
        "house_number":"2 к2"}],
        "locale":"de"}
*/
