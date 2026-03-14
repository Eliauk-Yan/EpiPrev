package com.epiprev.business.health.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.epiprev.business.health.domain.dto.HealthRecordDTO;
import com.epiprev.business.health.domain.entity.HealthRecord;
import com.epiprev.business.health.exception.HealthException;
import com.epiprev.business.health.domain.vo.HealthRecordVO;
import com.epiprev.business.health.mapper.HealthRecordMapper;
import com.epiprev.business.health.service.HealthService;
import com.epiprev.common.ai.service.AIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.epiprev.business.health.exception.HealthErrorCode.TODAY_NOT_RECORD;

/**
 * 健康服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HealthServiceImpl extends ServiceImpl<HealthRecordMapper, HealthRecord> implements HealthService {

    private final AIService aiService;

    @Override
    public HealthRecordVO getRecordByDate(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();
        HealthRecord record = getOne(new LambdaQueryWrapper<HealthRecord>()
                .eq(HealthRecord::getUserId, StpUtil.getLoginIdAsLong())
                .ge(HealthRecord::getCreateTime, start)
                .lt(HealthRecord::getCreateTime, end)
                .orderByDesc(HealthRecord::getCreateTime)
                .last("LIMIT 1"));
        HealthRecordVO vo = new HealthRecordVO();
        BeanUtil.copyProperties(record, vo);
        return vo;
    }

    @Override
    public HealthRecordVO getTodayRecord() {
        return getRecordByDate(LocalDate.now());
    }

    @Override
    public List<HealthRecordVO> getRecentSleepStats() {
        List<HealthRecord> list = list(new LambdaQueryWrapper<HealthRecord>()
                .eq(HealthRecord::getUserId, StpUtil.getLoginIdAsLong())
                .isNotNull(HealthRecord::getSleepHours)
                .orderByDesc(HealthRecord::getCreateTime)
                .last("LIMIT 7"));
        list.sort(Comparator.comparing(HealthRecord::getCreateTime));
        return list.stream().map((item) -> {
            HealthRecordVO vo = new HealthRecordVO();
            BeanUtil.copyProperties(item, vo);
            return vo;
        }).toList();
    }

    @Override
    public void addRecord(HealthRecordDTO recordDTO) {
        Long userId = StpUtil.getLoginIdAsLong();
        
        // 查找今日是否已有记录
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().plusDays(1).atStartOfDay();
        HealthRecord existingRecord = getOne(new LambdaQueryWrapper<HealthRecord>()
                .eq(HealthRecord::getUserId, userId)
                .ge(HealthRecord::getCreateTime, start)
                .lt(HealthRecord::getCreateTime, end)
                .orderByDesc(HealthRecord::getCreateTime)
                .last("LIMIT 1"));

        if (existingRecord != null) {
            // 如果已有记录，则更新（保留原有的 createTime 和 id）
            BeanUtil.copyProperties(recordDTO, existingRecord, "id", "userId", "createTime");
            updateById(existingRecord);
        } else {
            // 否则新增记录
            HealthRecord record = new HealthRecord();
            BeanUtil.copyProperties(recordDTO, record);
            record.setUserId(userId);
            save(record);
        }
    }

    private String aiCall(String prompt) {
        try {
            return aiService.call(prompt);
        } catch (Exception e) {
            log.error("AI service call failed", e);
            return "AI分析暂时不可用，请稍后再试。";
        }
    }

    @Override
    public String dataAnalysis() {
        HealthRecordVO healthRecord = getTodayRecord();
        if (healthRecord == null) {
            throw new HealthException(TODAY_NOT_RECORD);
        }
        String prompt = "这是我今日的健康数据: " + healthRecord + ", 帮我进行一下分析。";
        return aiCall(prompt);
    }

    @Override
    public String healthSuggestion() {
        HealthRecordVO healthRecord = getTodayRecord();
        if (healthRecord == null) {
            throw new HealthException(TODAY_NOT_RECORD);
        }
        String prompt = "这是我今日的健康数据: " + healthRecord + ", 帮我进行一下健康建议。";
        return aiCall(prompt);
    }

    @Override
    @Cached(name = "health:tip:", key = "#date", expire = 86400, timeUnit = TimeUnit.SECONDS, cacheType = CacheType.BOTH)
    public String getDailyTip(String date) {
        String prompt = "请生成一条简短的今日健康小贴士（约30-50字），内容涉及饮食、运动或心理健康，语气亲切。";
        return aiCall(prompt);
    }

    @Override
    public String analyzeBmi() {
        HealthRecordVO record = getTodayRecord();
        if (record == null) {
            throw new HealthException(TODAY_NOT_RECORD);
        }
        String prompt = "这是用户的今日健康数据: " + record + "。请重点详细分析用户的BMI指数和体重情况（身高" + record.getHeight() + "cm，体重"
                + record.getWeight() + "kg），计算BMI值，判断是否在正常范围（18.5-24），并给出针对性的体重管理建议。";
        return aiCall(prompt);
    }

    @Override
    public String analyzeSteps() {
        HealthRecordVO record = getTodayRecord();
        if (record == null) {
            throw new HealthException(TODAY_NOT_RECORD);
        }
        String prompt = "这是用户的今日健康数据: " + record + "。请重点分析用户的今日步数（" + record.getSteps()
                + "步），评价其运动量是否充足（以日行一万步为参考），并给出相应的运动建议。";
        return aiCall(prompt);
    }

    @Override
    public String analyzeHeartRate() {
        HealthRecordVO record = getTodayRecord();
        if (record == null) {
            throw new HealthException(TODAY_NOT_RECORD);
        }
        String prompt = "这是用户的今日健康数据: " + record + "。请重点分析用户的静息心率（" + record.getHeartRate()
                + "次/分），判断是否属于正常范围（60-100次/分），并给出心脏健康相关的建议。";
        return aiCall(prompt);
    }

    @Override
    public String analyzeBloodPressure() {
        HealthRecordVO record = getTodayRecord();
        if (record == null) {
            throw new HealthException(TODAY_NOT_RECORD);
        }
        String prompt = "这是用户的今日健康数据: " + record + "。请重点分析用户的血压数据（收缩压" + record.getSystolic() + "mmHg，舒张压"
                + record.getDiastolic() + "mmHg），判断血压水平是否正常（参考值<120/80），并给出维持血压健康的建议。";
        return aiCall(prompt);
    }

    @Override
    public String analyzeWater() {
        HealthRecordVO record = getTodayRecord();
        if (record == null) {
            throw new HealthException(TODAY_NOT_RECORD);
        }
        String prompt = "这是用户的今日健康数据: " + record + "。请重点分析用户的今日饮水量（" + record.getWaterIntake()
                + "ml），判断是否达到推荐标准（一般建议2000ml左右），并说明饮水对健康的重要性。";
        return aiCall(prompt);
    }

    @Override
    public String analyzeSleep() {
        HealthRecordVO today = getTodayRecord();
        List<HealthRecordVO> recent = getRecentSleepStats();

        StringBuilder dataDesc = new StringBuilder();
        if (today != null) {
            dataDesc.append("今日睡眠时长: ").append(today.getSleepHours()).append("小时。");
        }
        dataDesc.append("最近7天睡眠趋势: ");
        for (HealthRecordVO vo : recent) {
            dataDesc.append(vo.getCreateTime().toLocalDate()).append("睡眠").append(vo.getSleepHours()).append("小时; ");
        }

        String prompt = "这是用户的近期睡眠数据: " + dataDesc.toString() + "。请重点分析用户的睡眠习惯和趋势，判断睡眠时长是否充足（建议7-8小时），并给出改善睡眠质量的建议。";
        return aiCall(prompt);
    }
}
