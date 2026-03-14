package com.epiprev.business.news.interfaces.facade;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.epiprev.business.news.domain.entity.NewsInfo;
import com.epiprev.business.news.mapper.NewsInfoMapper;
import com.epiprev.business.news.service.NewsInfoService;
import com.epiprev.common.api.news.NewsFacade;
import com.epiprev.common.api.news.request.NewsPageQueryRequest;
import com.epiprev.common.api.news.request.NewsSaveRequest;
import com.epiprev.common.api.news.response.NewsResponse;
import com.epiprev.common.base.response.PageResult;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 新闻服务 Dubbo RPC 实现
 */
@DubboService(version = "1.0.0")
@RequiredArgsConstructor
public class NewsFacadeImpl implements NewsFacade {

    private final NewsInfoService newsInfoService;

    private final NewsInfoMapper newsInfoMapper;

    @Override
    public NewsResponse<PageResult<com.epiprev.common.api.news.response.NewsInfo>> pageNews(NewsPageQueryRequest request) {
        Page<NewsInfo> page = new Page<>(request.getPage(), request.getSize());
        LambdaQueryWrapper<NewsInfo> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(request.getWord())) {
            wrapper.and(w -> w.like(NewsInfo::getTitle, request.getWord())
                    .or()
                    .like(NewsInfo::getSummary, request.getWord())
                    .or()
                    .like(NewsInfo::getSource, request.getWord()));
        }
        if (StringUtils.hasText(request.getLevel())) {
            wrapper.eq(NewsInfo::getLevel, request.getLevel());
        }
        wrapper.orderByDesc(NewsInfo::getPublishTime).orderByDesc(NewsInfo::getCreateTime);

        Page<NewsInfo> newsPage = newsInfoMapper.selectPage(page, wrapper);
        List<com.epiprev.common.api.news.response.NewsInfo> list = newsPage.getRecords().stream()
                .map(this::convertToInfo)
                .collect(Collectors.toList());
        PageResult<com.epiprev.common.api.news.response.NewsInfo> pageResult = new PageResult<>(list, newsPage.getTotal());

        NewsResponse<PageResult<com.epiprev.common.api.news.response.NewsInfo>> response = new NewsResponse<>();
        response.setSuccess(true);
        response.setData(pageResult);
        return response;
    }

    @Override
    public NewsResponse<com.epiprev.common.api.news.response.NewsInfo> getNewsById(Long id) {
        NewsInfo entity = newsInfoService.getById(id);
        NewsResponse<com.epiprev.common.api.news.response.NewsInfo> response = new NewsResponse<>();
        if (entity == null) {
            response.setSuccess(false);
            response.setMessage("新闻不存在");
            return response;
        }
        response.setSuccess(true);
        response.setData(convertToInfo(entity));
        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public NewsResponse<Boolean> saveOrUpdateNews(NewsSaveRequest request) {
        NewsInfo entity;
        if (request.getId() != null) {
            entity = newsInfoService.getById(request.getId());
            if (entity == null) {
                entity = new NewsInfo();
                entity.setId(request.getId());
            }
        } else {
            entity = new NewsInfo();
        }
        entity.setTitle(request.getTitle());
        entity.setSummary(request.getSummary());
        entity.setSource(request.getSource());
        entity.setLevel(request.getLevel());
        entity.setContent(request.getContent());
        entity.setPublishTime(request.getPublishTime());
        boolean success = newsInfoService.saveOrUpdate(entity);

        NewsResponse<Boolean> response = new NewsResponse<>();
        response.setSuccess(success);
        response.setData(success);
        return response;
    }

    @Override
    public NewsResponse<Boolean> deleteNews(Long id) {
        boolean success = newsInfoService.removeById(id);
        NewsResponse<Boolean> response = new NewsResponse<>();
        response.setSuccess(success);
        response.setData(success);
        return response;
    }

    private com.epiprev.common.api.news.response.NewsInfo convertToInfo(NewsInfo entity) {
        com.epiprev.common.api.news.response.NewsInfo info = new com.epiprev.common.api.news.response.NewsInfo();
        BeanUtils.copyProperties(entity, info);
        return info;
    }
}
