package com.epiprev.business.health.domain.vo;

import com.epiprev.common.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 健康记录视图对象
 */
@Getter
@Setter
public class HealthRecordVO extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private BigDecimal weight;

    private BigDecimal height;

    private Double temperature;

    private Integer systolic;

    private Integer diastolic;

    private BigDecimal sleepHours;

    private Integer heartRate;

    private Double bloodSugar;

    private Integer waterIntake;

    private Integer steps;

    private String mood;

    @Override
    public String toString() {
        return "健康监测数据{" +
                "ID=" + id +
                ", 用户ID=" + userId +
                ", 身高=" + height + "cm" +
                ", 体重=" + weight + "kg" +
                ", 体温=" + temperature + "℃" +
                ", 收缩压=" + systolic +
                ", 舒张压=" + diastolic +
                ", 心率=" + heartRate +
                ", 血糖=" + bloodSugar +
                ", 睡眠时长=" + sleepHours + "h" +
                ", 饮水量=" + waterIntake + "ml" +
                ", 步数=" + steps +
                ", 情绪='" + mood + '\'' +
                '}';
    }
}
