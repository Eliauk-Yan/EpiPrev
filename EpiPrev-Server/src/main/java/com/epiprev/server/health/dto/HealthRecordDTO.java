package com.epiprev.server.health.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class HealthRecordDTO {
    private Double weight;
    private Double sleepHours;
    private String mood;
    private LocalDate recordDate;
}

