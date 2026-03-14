package com.epiprev.admin.service.impl;

import com.epiprev.admin.service.ArticleService;
import com.epiprev.common.web.result.MultiResult;
import com.epiprev.common.web.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

/**
 * 文章管理服务实现类
 */
@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService {

    // @DubboReference
    // private ArticleFacade articleFacade;

    @Override
    public MultiResult<Map<String, Object>> pageArticle(Integer page, Integer size, String title, String category) {
        log.info("Admin分页查询文章: page={}, size={}, title={}, category={}", page, size, title, category);
        return MultiResult.multiSuccess(Collections.emptyList(), 0, page, size);
    }

    @Override
    public Result<Map<String, Object>> getArticleById(Long id) {
        log.info("Admin查询文章详情: id={}", id);
        return Result.success(null);
    }

    @Override
    public Result<Boolean> saveOrUpdateArticle(Map<String, Object> article) {
        log.info("Admin保存文章: {}", article);
        return Result.success(true);
    }

    @Override
    public Result<Boolean> deleteArticle(Long id) {
        log.info("Admin删除文章: id={}", id);
        return Result.success(true);
    }
}
