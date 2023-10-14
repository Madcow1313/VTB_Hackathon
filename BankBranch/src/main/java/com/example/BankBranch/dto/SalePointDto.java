package com.example.BankBranch.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class SalePointDto {
    private double latitude;
    private double longitude;
    private Set<String> servicesIndividual;
    private Set<String> servicesLegalEntity;
    private Long workload;
    private double distanceToClient;
}
