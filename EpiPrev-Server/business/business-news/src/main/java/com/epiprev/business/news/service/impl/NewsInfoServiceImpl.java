package com.epiprev.business.news.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.epiprev.business.news.domain.entity.NewsInfo;
import com.epiprev.business.news.mapper.NewsInfoMapper;
import com.epiprev.business.news.service.NewsInfoService;
import org.springframework.stereotype.Service;

@Service
public class NewsInfoServiceImpl extends ServiceImpl<NewsInfoMapper, NewsInfo> implements NewsInfoService {
}

