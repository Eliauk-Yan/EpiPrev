package com.epiprev.business.health.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.epiprev.business.health.domain.dto.HealthRecordDTO;
import com.epiprev.business.health.domain.entity.HealthRecord;
import com.epiprev.business.health.domain.vo.HealthRecordVO;

import java.time.LocalDate;
import java.util.List;

/**
 * 健康服务接口
 */
public interface HealthService extends IService<HealthRecord> {

    /**
     * 获取指定日期的健康记录详情
     */
    HealthRecordVO getRecordByDate(LocalDate date);

    /**
     * 获取今日记录
     */
    HealthRecordVO getTodayRecord();

    /**
     * 获取最近7天的睡眠数据
     */
    List<HealthRecordVO> getRecentSleepStats();

    /**
     * 添加记录
     */
    void addRecord(HealthRecordDTO recordDTO);

    /**
     * ai 数据分析
     */
    String dataAnalysis();

    /**
     * AI 建议
     */
    String healthSuggestion();

    /**
     * 分析BMI
     */
    String analyzeBmi();

    /**
     * 分析步数
     */
    String analyzeSteps();

    /**
     * 分析心率
     */
    String analyzeHeartRate();

    /**
     * 分析血压
     */
    String analyzeBloodPressure();

    /**
     * 分析饮水
     */
    String analyzeWater();

    /**
     * 分析睡眠
     */
    String analyzeSleep();

    /**
     * 获取每日小贴士
     */
    String getDailyTip(String date);
}
