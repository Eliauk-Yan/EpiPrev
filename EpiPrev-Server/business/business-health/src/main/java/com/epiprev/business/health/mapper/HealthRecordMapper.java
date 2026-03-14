package com.epiprev.business.health.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.epiprev.business.health.domain.entity.HealthRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 健康记录 Mapper
 */
@Mapper
public interface HealthRecordMapper extends BaseMapper<HealthRecord> {

}
