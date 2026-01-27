package com.epiprev.server.health.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.epiprev.server.health.dto.HealthRecordDTO;
import com.epiprev.server.health.entity.HealthRecord;
import com.epiprev.server.health.vo.HealthRecordVO;
import com.epiprev.server.health.mapper.HealthRecordMapper;
import com.epiprev.server.health.service.HealthService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class HealthServiceImpl extends ServiceImpl<HealthRecordMapper, HealthRecord> implements HealthService {

    @Override

    public List<HealthRecordVO> getRecentRecords(Long userId) {
        Page<HealthRecord> p = new Page<>(1, 30); // Last 30 records
        QueryWrapper<HealthRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.orderByDesc("record_date");
        List<HealthRecord> records = page(p, wrapper).getRecords();
        return BeanUtil.copyToList(records, HealthRecordVO.class);
    }

    @Override
    public void addRecord(HealthRecordDTO recordDTO) {
        Long userId = StpUtil.getLoginIdAsLong();
        HealthRecord record = new HealthRecord();
        BeanUtil.copyProperties(recordDTO, record);
        record.setUserId(userId);
        if (record.getRecordDate() == null) {
            record.setRecordDate(LocalDate.now());
        }
        record.setCreateTime(LocalDateTime.now());
        save(record);
    }
}


