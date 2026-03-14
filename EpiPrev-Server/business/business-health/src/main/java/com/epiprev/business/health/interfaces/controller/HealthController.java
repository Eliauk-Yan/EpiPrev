package com.epiprev.business.health.interfaces.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.epiprev.business.health.domain.dto.HealthRecordDTO;
import com.epiprev.business.health.domain.entity.HealthRecord;
import com.epiprev.business.health.domain.vo.HealthRecordVO;
import com.epiprev.business.health.service.HealthService;
import com.epiprev.common.web.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 健康控制器
 */
@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
public class HealthController {

    private final HealthService healthService;

    @GetMapping("/detail")
    public Result<HealthRecordVO> detail(@RequestParam("date") LocalDate date) {
        return Result.success(healthService.getRecordByDate(date));
    }

    @GetMapping("/today")
    public Result<HealthRecordVO> today() {
        return Result.success(healthService.getTodayRecord());
    }

    @GetMapping("/sleep/recent")
    public Result<List<HealthRecordVO>> getRecentSleepStats() {
        return Result.success(healthService.getRecentSleepStats());
    }

    @PostMapping("/add")
    public Result<Boolean> add(@RequestBody HealthRecordDTO recordDTO) {
        healthService.addRecord(recordDTO);
        return Result.success(true);
    }

    /**
     * AI 数据分析接口
     */
    @GetMapping("/ai/analysis")
    public Result<String> dataAnalysis() {
        return Result.success(healthService.dataAnalysis());
    }

    @GetMapping("/ai/analysis/bmi")
    public Result<String> analyzeBmi() {
        return Result.success(healthService.analyzeBmi());
    }

    @GetMapping("/ai/analysis/steps")
    public Result<String> analyzeSteps() {
        return Result.success(healthService.analyzeSteps());
    }

    @GetMapping("/ai/analysis/heart-rate")
    public Result<String> analyzeHeartRate() {
        return Result.success(healthService.analyzeHeartRate());
    }

    @GetMapping("/ai/analysis/bp")
    public Result<String> analyzeBloodPressure() {
        return Result.success(healthService.analyzeBloodPressure());
    }

    @GetMapping("/ai/analysis/water")
    public Result<String> analyzeWater() {
        return Result.success(healthService.analyzeWater());
    }

    @GetMapping("/ai/analysis/sleep")
    public Result<String> analyzeSleep() {
        return Result.success(healthService.analyzeSleep());
    }

    /**
     * AI 建议接口
     */
    @GetMapping("/ai/suggestion")
    public Result<String> healthSuggestion() {
        return Result.success(healthService.healthSuggestion());
    }

    /**
     * 获取每日小贴士
     */
    @GetMapping("/ai/tip")
    public Result<String> getDailyTip() {
        return Result.success(healthService.getDailyTip(LocalDate.now().toString()));
    }

    // ================== 后台管理接口 ==================

    @GetMapping("/page")
    public Result<Page<HealthRecord>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long userId) {
        Page<HealthRecord> pageParam = new Page<>(current, size);
        LambdaQueryWrapper<HealthRecord> wrapper = new LambdaQueryWrapper<>();
        if (userId != null) {
            wrapper.eq(HealthRecord::getUserId, userId);
        }
        wrapper.orderByDesc(HealthRecord::getCreateTime);
        return Result.success(healthService.page(pageParam, wrapper));
    }

    @PostMapping
    public Result<Void> save(@RequestBody HealthRecord record) {
        healthService.save(record);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody HealthRecord record) {
        healthService.updateById(record);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        healthService.removeById(id);
        return Result.success();
    }
}
