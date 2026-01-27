package com.epiprev.server.health.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.epiprev.server.health.dto.HealthRecordDTO;
import com.epiprev.server.health.entity.HealthRecord;
import com.epiprev.server.health.vo.HealthRecordVO;

import java.util.List;

public interface HealthService extends IService<HealthRecord> {
    List<HealthRecordVO> getRecentRecords(Long userId);

    void addRecord(HealthRecordDTO recordDTO);
}


