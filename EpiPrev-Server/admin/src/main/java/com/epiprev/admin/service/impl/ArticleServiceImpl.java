package com.epiprev.admin.service.impl;

import com.epiprev.admin.service.ArticleService;
import com.epiprev.common.api.article.ArticleFacade;
import com.epiprev.common.api.article.request.ArticlePageQueryRequest;
import com.epiprev.common.api.article.request.ArticleSaveRequest;
import com.epiprev.common.api.article.response.ArticleInfo;
import com.epiprev.common.api.article.response.ArticleResponse;
import com.epiprev.common.base.response.PageResult;
import com.epiprev.common.web.result.MultiResult;
import com.epiprev.common.web.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 文章管理服务实现类
 */
@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService {

    @DubboReference(version = "1.0.0")
    private ArticleFacade articleFacade;

    @Override
    public MultiResult<Map<String, Object>> pageArticle(Integer page, Integer size, String title, String category) {
        log.info("Admin分页查询文章: page={}, size={}, title={}, category={}", page, size, title, category);
        ArticlePageQueryRequest request = new ArticlePageQueryRequest();
        request.setPage(page);
        request.setSize(size);
        request.setTitle(title);
        request.setCategory(category);

        ArticleResponse<PageResult<ArticleInfo>> response = articleFacade.pageArticle(request);
        if (Boolean.TRUE.equals(response.getSuccess()) && response.getData() != null) {
            PageResult<ArticleInfo> pageResult = response.getData();
            List<Map<String, Object>> list = pageResult.getList().stream()
                    .map(this::convertToMap)
                    .collect(Collectors.toList());
            return MultiResult.multiSuccess(list, pageResult.getTotal(), page, size);
        }
        return MultiResult.multiSuccess(Collections.emptyList(), 0, page, size);
    }

    @Override
    public Result<Map<String, Object>> getArticleById(Long id) {
        log.info("Admin查询文章详情: id={}", id);
        ArticleResponse<ArticleInfo> response = articleFacade.getArticleById(id);
        if (Boolean.TRUE.equals(response.getSuccess()) && response.getData() != null) {
            return Result.success(convertToMap(response.getData()));
        }
        return Result.success(null);
    }

    @Override
    public Result<Boolean> saveOrUpdateArticle(Map<String, Object> article) {
        log.info("Admin保存文章: {}", article);
        ArticleSaveRequest request = new ArticleSaveRequest();
        Object id = article.get("id");
        if (id instanceof Number) {
            request.setId(((Number) id).longValue());
        }
        request.setTitle((String) article.get("title"));
        request.setCategory((String) article.get("category"));
        request.setSummary((String) article.get("summary"));
        request.setContent((String) article.get("content"));
        request.setCover((String) article.get("cover"));
        request.setVideoUrl((String) article.get("videoUrl"));
        Object type = article.get("type");
        if (type instanceof Number) {
            request.setType(((Number) type).intValue());
        } else if (type instanceof String) {
            // 前端可能传 IMAGE/VIDEO 字符串，这里做一个简单映射
            if ("IMAGE".equalsIgnoreCase((String) type)) {
                request.setType(0);
            } else if ("VIDEO".equalsIgnoreCase((String) type)) {
                request.setType(1);
            }
        }
        ArticleResponse<Boolean> response = articleFacade.saveOrUpdateArticle(request);
        if (Boolean.TRUE.equals(response.getSuccess())) {
            return Result.success(response.getData());
        }
        return Result.error(response.getCode(), response.getMessage());
    }

    @Override
    public Result<Boolean> deleteArticle(Long id) {
        log.info("Admin删除文章: id={}", id);
        ArticleResponse<Boolean> response = articleFacade.deleteArticle(id);
        if (Boolean.TRUE.equals(response.getSuccess())) {
            return Result.success(response.getData());
        }
        return Result.error(response.getCode(), response.getMessage());
    }

    private Map<String, Object> convertToMap(ArticleInfo info) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", info.getId());
        map.put("title", info.getTitle());
        map.put("category", info.getCategory());
        map.put("summary", info.getSummary());
        map.put("content", info.getContent());
        map.put("cover", info.getCover());
        map.put("videoUrl", info.getVideoUrl());
        map.put("type", info.getType());
        map.put("views", info.getViews());
        map.put("createTime", info.getCreateTime());
        map.put("updateTime", info.getUpdateTime());
        return map;
    }
}

