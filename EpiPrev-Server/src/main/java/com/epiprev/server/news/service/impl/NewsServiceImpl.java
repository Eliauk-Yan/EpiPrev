package com.epiprev.server.news.service.impl;
import com.epiprev.server.common.result.Result;

import cn.hutool.core.bean.BeanUtil;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.epiprev.server.news.entity.NewsInfo;
import com.epiprev.server.news.mapper.NewsInfoMapper;
import com.epiprev.server.news.service.NewsService;
import com.epiprev.server.news.vo.NewsVO;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class NewsServiceImpl extends ServiceImpl<NewsInfoMapper, NewsInfo> implements NewsService {

    @Override
    @Cached(name = "newsList:", key = "#page + '-' + #size", expire = 3600, timeUnit = TimeUnit.SECONDS, cacheType = CacheType.BOTH)
    public Page<NewsVO> getList(int page, int size) {
        Page<NewsInfo> p = new Page<>(page, size);
        QueryWrapper<NewsInfo> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("publish_time");
        Page<NewsInfo> newsPage = page(p, wrapper);

        Page<NewsVO> result = new Page<>(newsPage.getCurrent(), newsPage.getSize(), newsPage.getTotal());
        result.setRecords(BeanUtil.copyToList(newsPage.getRecords(), NewsVO.class));
        return result;
    }

    @Override
    @Cached(name = "newsDetail:", key = "#id", expire = 3600, timeUnit = TimeUnit.SECONDS, cacheType = CacheType.BOTH)
    public NewsVO getDetail(Long id) {
        NewsInfo news = getById(id);
        if (news == null) {
            return null;
        }
        NewsVO vo = new NewsVO();
        BeanUtil.copyProperties(news, vo);
        return vo;
    }
}



