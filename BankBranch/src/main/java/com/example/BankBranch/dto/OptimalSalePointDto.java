package com.example.BankBranch.dto;

import com.example.BankBranch.model.Point;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptimalSalePointDto {
    private Point point;


}
