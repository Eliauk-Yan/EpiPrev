package com.epiprev.server.health.service.impl;

import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.epiprev.server.health.convert.HealthConvert;
import com.epiprev.server.health.domain.dto.HealthRecordDTO;
import com.epiprev.server.health.entity.HealthRecord;
import com.epiprev.server.health.exception.HealthException;
import com.epiprev.server.health.domain.vo.HealthRecordVO;
import com.epiprev.server.health.mapper.HealthRecordMapper;
import com.epiprev.server.health.service.HealthService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.Comparator;
import java.util.List;

import static com.epiprev.server.health.controller.HealthErrorCode.TODAY_NOT_RECORD;

@Service
@RequiredArgsConstructor
public class HealthServiceImpl extends ServiceImpl<HealthRecordMapper, HealthRecord> implements HealthService {

    private final ChatModel chatModel;

    private final HealthConvert healthConvert;

    @Override
    public HealthRecordVO getRecordByDate(LocalDate date) {
        // 1. 查询记录
        HealthRecord record = getOne(new LambdaQueryWrapper<HealthRecord>()
                .eq(HealthRecord::getUserId, StpUtil.getLoginIdAsLong()).eq(HealthRecord::getRecordDate, date));
        // 2. 转换为VO返回
        return healthConvert.toVO(record);
    }

    @Override
    public HealthRecordVO getTodayRecord() {
        // 1. 调用通用方法获取今日记录
        return getRecordByDate(LocalDate.now());
    }

    @Override
    public List<HealthRecordVO> getRecentSleepStats() {
        // 1. 查询数据
        List<HealthRecord> list = list(
                new LambdaQueryWrapper<HealthRecord>()
                        .eq(HealthRecord::getUserId, StpUtil.getLoginIdAsLong())
                        .isNotNull(HealthRecord::getSleepHours)
                        .orderByDesc(HealthRecord::getRecordDate)
                        .last("LIMIT 7"));
        // 2. 按日期升序排序以用于趋势图
        list.sort(Comparator.comparing(HealthRecord::getRecordDate));
        // 3. 转换为VO列表返回
        return healthConvert.toVOs(list);
    }

    @Override
    public void addRecord(HealthRecordDTO recordDTO) {
        Long userId = StpUtil.getLoginIdAsLong();
        HealthRecord record = new HealthRecord();
        BeanUtil.copyProperties(recordDTO, record);
        record.setUserId(userId);
        // 1. 如果未指定日期，默认为今天
        if (record.getRecordDate() == null) {
            record.setRecordDate(LocalDate.now());
        }
        record.setCreateTime(LocalDateTime.now());
        // 2. 保存记录
        save(record);
    }

    @Override
    public String dataAnalysis() {
        // 1. 获取今日健康记录
        HealthRecordVO healthRecord = getTodayRecord();
        if (healthRecord == null) {
            throw new HealthException(TODAY_NOT_RECORD);
        }
        // 2. 构造提示词
        String prompt = "这是我今日的健康数据: " + healthRecord + ", 帮我进行一下分析。";
        // 3. 调用ChatModel进行分析
        return chatModel.call(prompt);
    }

    @Override
    public String healthSuggestion() {
        // 1. 获取今日健康记录
        HealthRecordVO healthRecord = getTodayRecord();
        if (healthRecord == null) {
            throw new HealthException(TODAY_NOT_RECORD);
        }
        // 2. 构造提示词
        String prompt = "这是我今日的健康数据: " + healthRecord + ", 帮我进行一下健康建议。";
        // 3. 调用ChatModel进行分析
        return chatModel.call(prompt);
    }

    @Override
    @Cached(name = "health:tip:", key = "#date", expire = 86400, timeUnit = TimeUnit.SECONDS, cacheType = CacheType.BOTH)
    public String getDailyTip(String date) {
        try {
            // 1. 构造提示词
            String prompt = "请生成一条简短的今日健康小贴士（约30-50字），内容涉及饮食、运动或心理健康，语气亲切。";
            // 2. 调用大模型获取回复
            return chatModel.call(prompt);
        } catch (Exception e) {
            return "今天也要保持好心情，多喝水哦！";
        }
    }

    @Override
    public String analyzeBmi() {
        HealthRecordVO record = getTodayRecord();
        if (record == null) {
            throw new HealthException(TODAY_NOT_RECORD);
        }
        String prompt = "这是用户的今日健康数据: " + record + "。请重点详细分析用户的BMI指数和体重情况（身高" + record.getHeight() + "cm，体重"
                + record.getWeight() + "kg），计算BMI值，判断是否在正常范围（18.5-24），并给出针对性的体重管理建议。";
        return chatModel.call(prompt);
    }

    @Override
    public String analyzeSteps() {
        HealthRecordVO record = getTodayRecord();
        if (record == null) {
            throw new HealthException(TODAY_NOT_RECORD);
        }
        String prompt = "这是用户的今日健康数据: " + record + "。请重点分析用户的今日步数（" + record.getSteps()
                + "步），评价其运动量是否充足（以日行一万步为参考），并给出相应的运动建议。";
        return chatModel.call(prompt);
    }

    @Override
    public String analyzeHeartRate() {
        HealthRecordVO record = getTodayRecord();
        if (record == null) {
            throw new HealthException(TODAY_NOT_RECORD);
        }
        String prompt = "这是用户的今日健康数据: " + record + "。请重点分析用户的静息心率（" + record.getHeartRate()
                + "次/分），判断是否属于正常范围（60-100次/分），并给出心脏健康相关的建议。";
        return chatModel.call(prompt);
    }

    @Override
    public String analyzeBloodPressure() {
        HealthRecordVO record = getTodayRecord();
        if (record == null) {
            throw new HealthException(TODAY_NOT_RECORD);
        }
        String prompt = "这是用户的今日健康数据: " + record + "。请重点分析用户的血压数据（收缩压" + record.getSystolic() + "mmHg，舒张压"
                + record.getDiastolic() + "mmHg），判断血压水平是否正常（参考值<120/80），并给出维持血压健康的建议。";
        return chatModel.call(prompt);
    }

    @Override
    public String analyzeWater() {
        HealthRecordVO record = getTodayRecord();
        if (record == null) {
            throw new HealthException(TODAY_NOT_RECORD);
        }
        String prompt = "这是用户的今日健康数据: " + record + "。请重点分析用户的今日饮水量（" + record.getWaterIntake()
                + "ml），判断是否达到推荐标准（一般建议2000ml左右），并说明饮水对健康的重要性。";
        return chatModel.call(prompt);
    }

    @Override
    public String analyzeSleep() {
        // 获取今日记录和近期记录，综合分析
        HealthRecordVO today = getTodayRecord();
        List<HealthRecordVO> recent = getRecentSleepStats();

        StringBuilder dataDesc = new StringBuilder();
        if (today != null) {
            dataDesc.append("今日睡眠时长: ").append(today.getSleepHours()).append("小时。");
        }
        dataDesc.append("最近7天睡眠趋势: ");
        for (HealthRecordVO vo : recent) {
            dataDesc.append(vo.getRecordDate()).append("睡眠").append(vo.getSleepHours()).append("小时; ");
        }

        String prompt = "这是用户的近期睡眠数据: " + dataDesc.toString() + "。请重点分析用户的睡眠习惯和趋势，判断睡眠时长是否充足（建议7-8小时），并给出改善睡眠质量的建议。";
        return chatModel.call(prompt);
    }
}
