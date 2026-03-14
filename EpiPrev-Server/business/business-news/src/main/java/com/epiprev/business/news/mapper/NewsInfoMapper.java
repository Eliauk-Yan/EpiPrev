package com.epiprev.business.news.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.epiprev.business.news.domain.entity.NewsInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NewsInfoMapper extends BaseMapper<NewsInfo> {
}

