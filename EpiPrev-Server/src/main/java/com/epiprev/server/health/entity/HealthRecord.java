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

    private Double sleepHours;

    private String mood;

    private LocalDate recordDate;

}


