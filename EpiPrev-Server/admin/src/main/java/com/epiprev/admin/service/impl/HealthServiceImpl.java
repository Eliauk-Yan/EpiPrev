package com.epiprev.admin.service.impl;

import com.epiprev.admin.service.HealthService;
import com.epiprev.common.web.result.MultiResult;
import com.epiprev.common.web.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

/**
 * 健康打卡管理服务实现类
 */
@Slf4j
@Service
public class HealthServiceImpl implements HealthService {

    @Override
    public MultiResult<Map<String, Object>> pageHealthRecord(Integer page, Integer size, String nickName) {
        log.info("Admin分页查询健康打卡记录: page={}, size={}, nickName={}", page, size, nickName);
        return MultiResult.multiSuccess(Collections.emptyList(), 0, page, size);
    }

    @Override
    public Result<Map<String, Object>> getHealthRecordById(Long id) {
        return Result.success(null);
    }

    @Override
    public Result<Boolean> deleteHealthRecord(Long id) {
        log.info("Admin删除健康打卡记录: id={}", id);
        return Result.success(true);
    }
}
