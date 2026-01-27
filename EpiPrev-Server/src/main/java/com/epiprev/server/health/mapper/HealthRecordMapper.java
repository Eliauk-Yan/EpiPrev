package com.epiprev.server.health.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.epiprev.server.health.entity.HealthRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HealthRecordMapper extends BaseMapper<HealthRecord> {
}


