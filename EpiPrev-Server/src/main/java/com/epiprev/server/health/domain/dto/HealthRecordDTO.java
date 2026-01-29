package com.epiprev.server.health.domain.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class HealthRecordDTO {

    private Double weight;

    private Double height;

    private Double sleepHours;

    private String mood;

    private Integer systolic;

    private Integer diastolic;

    private Integer heartRate;

    private Double temperature;

    private Double bloodSugar;

    private Integer waterIntake;

    private Integer steps;

    private LocalDate recordDate;

}
