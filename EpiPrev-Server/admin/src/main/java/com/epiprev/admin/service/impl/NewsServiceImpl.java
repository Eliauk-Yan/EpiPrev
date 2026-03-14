package com.epiprev.admin.service.impl;

import com.epiprev.admin.service.NewsService;
import com.epiprev.common.api.news.NewsFacade;
import com.epiprev.common.api.news.request.NewsPageQueryRequest;
import com.epiprev.common.api.news.request.NewsSaveRequest;
import com.epiprev.common.api.news.response.NewsInfo;
import com.epiprev.common.api.news.response.NewsResponse;
import com.epiprev.common.base.response.PageResult;
import com.epiprev.common.web.result.MultiResult;
import com.epiprev.common.web.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 新闻管理服务实现类
 */
@Slf4j
@Service
public class NewsServiceImpl implements NewsService {

    @DubboReference(version = "1.0.0")
    private NewsFacade newsFacade;

    @Override
    public MultiResult<Map<String, Object>> pageNews(Integer page, Integer size, String word, String level) {
        log.info("Admin分页查询新闻: page={}, size={}, word={}, level={}", page, size, word, level);
        NewsPageQueryRequest request = new NewsPageQueryRequest();
        request.setPage(page);
        request.setSize(size);
        request.setWord(word);
        request.setLevel(level);

        NewsResponse<PageResult<NewsInfo>> response = newsFacade.pageNews(request);
        if (Boolean.TRUE.equals(response.getSuccess()) && response.getData() != null) {
            PageResult<NewsInfo> pageResult = response.getData();
            List<Map<String, Object>> list = pageResult.getList().stream()
                    .map(this::convertToMap)
                    .collect(Collectors.toList());
            return MultiResult.multiSuccess(list, pageResult.getTotal(), page, size);
        }
        return MultiResult.multiSuccess(Collections.emptyList(), 0L, page, size);
    }

    @Override
    public Result<Map<String, Object>> getNewsById(Long id) {
        log.info("Admin查询新闻详情: id={}", id);
        NewsResponse<NewsInfo> response = newsFacade.getNewsById(id);
        if (Boolean.TRUE.equals(response.getSuccess()) && response.getData() != null) {
            return Result.success(convertToMap(response.getData()));
        }
        return Result.success(null);
    }

    @Override
    public Result<Boolean> saveOrUpdateNews(Map<String, Object> news) {
        log.info("Admin保存新闻: {}", news);
        NewsSaveRequest request = new NewsSaveRequest();
        Object id = news.get("id");
        if (id instanceof Number) {
            request.setId(((Number) id).longValue());
        }
        request.setTitle((String) news.get("title"));
        request.setSummary((String) news.get("summary"));
        request.setSource((String) news.get("source"));
        request.setLevel((String) news.get("level"));
        request.setContent((String) news.get("content"));
        Object publishTime = news.get("publishTime");
        if (publishTime instanceof String) {
            try {
                String s = (String) publishTime;
                if (s.contains("Z") || s.contains("+") || s.length() > 19) {
                    request.setPublishTime(LocalDateTime.ofInstant(Instant.parse(s), ZoneId.systemDefault()));
                } else {
                    request.setPublishTime(LocalDateTime.parse(s.length() > 19 ? s.substring(0, 19) : s));
                }
            } catch (Exception ignored) {
            }
        } else if (publishTime instanceof LocalDateTime) {
            request.setPublishTime((LocalDateTime) publishTime);
        }

        NewsResponse<Boolean> response = newsFacade.saveOrUpdateNews(request);
        if (Boolean.TRUE.equals(response.getSuccess())) {
            return Result.success(response.getData());
        }
        return Result.error(response.getCode(), response.getMessage());
    }

    @Override
    public Result<Boolean> deleteNews(Long id) {
        log.info("Admin删除新闻: id={}", id);
        NewsResponse<Boolean> response = newsFacade.deleteNews(id);
        if (Boolean.TRUE.equals(response.getSuccess())) {
            return Result.success(response.getData());
        }
        return Result.error(response.getCode(), response.getMessage());
    }

    private Map<String, Object> convertToMap(NewsInfo info) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", info.getId());
        map.put("title", info.getTitle());
        map.put("summary", info.getSummary());
        map.put("source", info.getSource());
        map.put("level", info.getLevel());
        map.put("content", info.getContent());
        map.put("publishTime", info.getPublishTime());
        map.put("createTime", info.getCreateTime());
        map.put("updateTime", info.getUpdateTime());
        return map;
    }
}
