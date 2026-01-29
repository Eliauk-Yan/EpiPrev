package com.epiprev.server.health.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.epiprev.server.health.domain.dto.HealthRecordDTO;
import com.epiprev.server.health.domain.vo.HealthRecordVO;
import com.epiprev.server.health.entity.HealthRecord;
import java.time.LocalDate;
import java.util.List;

public interface HealthService extends IService<HealthRecord> {

    /**
     * 获取指定日期的健康记录详情
     *
     * @param date 日期
     * @return 记录详情，若无则返回null
     */
    HealthRecordVO getRecordByDate(LocalDate date);

    /**
     * 获取今日记录
     *
     * @return 今日记录详情
     */
    HealthRecordVO getTodayRecord();

    /**
     * 获取最近7天的睡眠数据
     *
     * @return 睡眠数据列表
     */
    List<HealthRecordVO> getRecentSleepStats();

    void addRecord(HealthRecordDTO recordDTO);

    /**
     * ai 数据分析
     * 
     * @return 分析结果
     */
    String dataAnalysis();

    /**
     * AI 建议
     * 
     * @return 健康建议
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
     * 获取每日小贴士（AI生成的，一天一条，有缓存）
     * 
     * @return tip
     */
    String getDailyTip(String date);
}
