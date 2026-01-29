package com.epiprev.server.health.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class HealthRecordVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;
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
    private LocalDateTime createTime;
}
