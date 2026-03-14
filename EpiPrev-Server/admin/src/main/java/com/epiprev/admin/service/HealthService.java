package com.epiprev.admin.service;

import com.epiprev.common.web.result.MultiResult;
import com.epiprev.common.web.result.Result;

import java.util.Map;

/**
 * 健康打卡管理服务接口
 */
public interface HealthService {

    /**
     * 分页查询健康打卡记录
     */
    MultiResult<Map<String, Object>> pageHealthRecord(Integer page, Integer size, String nickName);

    /**
     * 根据Id查询健康记录
     */
    Result<Map<String, Object>> getHealthRecordById(Long id);

    /**
     * 删除健康记录
     */
    Result<Boolean> deleteHealthRecord(Long id);
}
