package com.epiprev.server.news.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.epiprev.server.news.entity.NewsInfo;
import com.epiprev.server.news.mapper.NewsInfoMapper;
import com.epiprev.server.news.service.NewsService;
import com.epiprev.server.news.vo.NewsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl extends ServiceImpl<NewsInfoMapper, NewsInfo> implements NewsService {

    @Override
    @Cached(name = "newsList:", key = "#page + '-' + #size", expire = 3600, timeUnit = TimeUnit.SECONDS, cacheType = CacheType.BOTH)
    public Page<NewsVO> getList(int page, int size) {
        // 1. 创建分页对象
        Page<NewsInfo> p = new Page<>(page, size);
        // 2. 构建查询条件，按发布时间倒序
        LambdaQueryWrapper<NewsInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(NewsInfo::getPublishTime);
        // 3. 执行查询
        Page<NewsInfo> newsPage = page(p, wrapper);

        // 4. 转换为VO对象
        Page<NewsVO> result = new Page<>(newsPage.getCurrent(), newsPage.getSize(), newsPage.getTotal());
        result.setRecords(BeanUtil.copyToList(newsPage.getRecords(), NewsVO.class));
        return result;
    }

    @Override
    @Cached(name = "newsDetail:", key = "#id", expire = 3600, timeUnit = TimeUnit.SECONDS, cacheType = CacheType.BOTH)
    public NewsVO getDetail(Long id) {
        // 1. 根据ID查询
        NewsInfo news = getById(id);
        if (news == null) {
            return null;
        }
        // 2. 转换为VO
        NewsVO vo = new NewsVO();
        BeanUtil.copyProperties(news, vo);
        return vo;
    }
}
