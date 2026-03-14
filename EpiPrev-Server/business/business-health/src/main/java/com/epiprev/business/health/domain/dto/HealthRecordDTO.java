package com.epiprev.business.health.domain.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 健康记录传输对象
 */
@Data
public class HealthRecordDTO {

    /**
     * 体重 (kg)
     */
    private BigDecimal weight;

    /**
     * 身高 (cm)
     */
    private BigDecimal height;

    /**
     * 体温 (℃)
     */
    private Double temperature;

    /**
     * 收缩压 (mmHg)
     */
    private Integer systolic;

    /**
     * 舒张压 (mmHg)
     */
    private Integer diastolic;

    /**
     * 昨日睡眠时长 (h)
     */
    private BigDecimal sleepHours;

    /**
     * 心率 (次/分)
     */
    private Integer heartRate;

    /**
     * 血糖 (mmol/L)
     */
    private Double bloodSugar;

    /**
     * 饮水量 (ml)
     */
    private Integer waterIntake;

    /**
     * 步数
     */
    private Integer steps;

    /**
     * 心情
     */
    private String mood;

}
