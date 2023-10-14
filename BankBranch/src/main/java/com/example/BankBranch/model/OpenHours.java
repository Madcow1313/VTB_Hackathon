package com.example.BankBranch.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class OpenHours {
    @JsonProperty("days")
    private String days;

    @JsonProperty("hours")
    private String hours;

    // Добавьте геттеры и сеттеры
}