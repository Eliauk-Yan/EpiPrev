package com.epiprev.server.health.controller;

import com.epiprev.server.common.result.Result;
import com.epiprev.server.health.domain.dto.HealthRecordDTO;
import com.epiprev.server.health.domain.vo.HealthRecordVO;
import com.epiprev.server.health.service.HealthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

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
     * 
     * @return 分析结果
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
     * 
     * @return 健康建议
     */
    @GetMapping("/ai/suggestion")
    public Result<String> healthSuggestion() {
        return Result.success(healthService.healthSuggestion());
    }

    /**
     * 获取每日小贴士
     * 
     * @return tip
     */
    @GetMapping("/ai/tip")
    public Result<String> getDailyTip() {
        return Result.success(healthService.getDailyTip(java.time.LocalDate.now().toString()));
    }

}
