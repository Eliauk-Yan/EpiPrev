package com.epiprev.server.health.entity;

import com.epiprev.server.common.entity.BaseEntity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("health_record")
public class HealthRecord extends BaseEntity {

    private Long userId;

    private Double weight;

    private Double height;

    private Double sleepHours;

    private String mood;

    /**
     * 收缩压 (mmHg)
     */
    private Integer systolic;

    /**
     * 舒张压 (mmHg)
     */
    private Integer diastolic;

    /**
     * 心率 (次/分)
     */
    private Integer heartRate;

    /**
     * 体温 (℃)
     */
    private Double temperature;

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

    private LocalDate recordDate;

}
