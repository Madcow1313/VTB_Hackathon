package com.example.BankBranch.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class SalePointDto {

    private double latitude;

    private double longitude;

    private double distanceToClient;

}
