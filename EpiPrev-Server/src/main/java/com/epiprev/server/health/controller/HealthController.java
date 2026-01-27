package com.epiprev.server.health.controller;
import com.epiprev.server.common.result.Result;

import cn.dev33.satoken.stp.StpUtil;
import com.epiprev.server.health.dto.HealthRecordDTO;
import com.epiprev.server.health.service.HealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/health")
public class HealthController {

    @Autowired
    private HealthService healthService;

    @GetMapping("/list")
    public Result list() {
        return Result.success(healthService.getRecentRecords(StpUtil.getLoginIdAsLong()));
    }

    @PostMapping("/add")
    public Result add(@RequestBody HealthRecordDTO recordDTO) {
        healthService.addRecord(recordDTO);
        return Result.success("记录成功");
    }
}



