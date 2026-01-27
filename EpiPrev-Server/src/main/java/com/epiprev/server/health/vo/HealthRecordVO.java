package com.epiprev.server.health.vo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class HealthRecordVO {
    private Long id;
    private Double weight;
    private Double sleepHours;
    private String mood;
    private LocalDate recordDate;
    private LocalDateTime createTime;
}

