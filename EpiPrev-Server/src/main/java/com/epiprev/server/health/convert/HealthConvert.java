package com.epiprev.server.health.convert;

import com.epiprev.server.health.domain.vo.HealthRecordVO;
import com.epiprev.server.health.entity.HealthRecord;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @classname HealthConvert
 * @description 健康模块转换器
 * @date 2026/01/29 23:10
 */
@Mapper(componentModel = "spring")
public interface HealthConvert {

    HealthRecordVO toVO(HealthRecord record);

    List<HealthRecordVO> toVOs(List<HealthRecord> record);

}
