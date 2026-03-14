package com.epiprev.business.article.interfaces.facade;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.epiprev.business.article.domain.entity.Article;
import com.epiprev.business.article.domain.enums.ArticleType;
import com.epiprev.business.article.mapper.ArticleMapper;
import com.epiprev.business.article.service.ArticleService;
import com.epiprev.common.api.article.ArticleFacade;
import com.epiprev.common.api.article.request.ArticlePageQueryRequest;
import com.epiprev.common.api.article.request.ArticleSaveRequest;
import com.epiprev.common.api.article.response.ArticleInfo;
import com.epiprev.common.api.article.response.ArticleResponse;
import com.epiprev.common.base.response.PageResult;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 文章服务 Dubbo RPC 实现
 */
@DubboService(version = "1.0.0")
@RequiredArgsConstructor
public class ArticleFacadeImpl implements ArticleFacade {

    private final ArticleService articleService;

    private final ArticleMapper articleMapper;

    @Override
    public ArticleResponse<PageResult<ArticleInfo>> pageArticle(ArticlePageQueryRequest request) {
        Page<Article> page = new Page<>(request.getPage(), request.getSize());
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(request.getTitle())) {
            wrapper.like(Article::getTitle, request.getTitle());
        }
        if (StringUtils.hasText(request.getCategory())) {
            wrapper.eq(Article::getCategory, request.getCategory());
        }
        wrapper.orderByDesc(Article::getCreateTime);

        Page<Article> articlePage = articleMapper.selectPage(page, wrapper);
        List<ArticleInfo> list = articlePage.getRecords().stream().map(this::convertToInfo).collect(Collectors.toList());
        PageResult<ArticleInfo> pageResult = new PageResult<>(list, articlePage.getTotal());

        ArticleResponse<PageResult<ArticleInfo>> response = new ArticleResponse<>();
        response.setSuccess(true);
        response.setData(pageResult);
        return response;
    }

    @Override
    public ArticleResponse<ArticleInfo> getArticleById(Long id) {
        Article article = articleService.getById(id);
        ArticleResponse<ArticleInfo> response = new ArticleResponse<>();
        if (article == null) {
            response.setSuccess(false);
            response.setMessage("文章不存在");
            return response;
        }
        response.setSuccess(true);
        response.setData(convertToInfo(article));
        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ArticleResponse<Boolean> saveOrUpdateArticle(ArticleSaveRequest request) {
        Article article;
        if (request.getId() != null) {
            article = articleService.getById(request.getId());
            if (article == null) {
                article = new Article();
                article.setId(request.getId());
            }
        } else {
            article = new Article();
        }
        article.setTitle(request.getTitle());
        article.setCategory(request.getCategory());
        article.setSummary(request.getSummary());
        article.setContent(request.getContent());
        article.setCover(request.getCover());
        article.setVideoUrl(request.getVideoUrl());
        if (request.getType() != null) {
            article.setType(ArticleType.fromCode(request.getType()));
        }
        boolean success = articleService.saveOrUpdate(article);

        ArticleResponse<Boolean> response = new ArticleResponse<>();
        response.setSuccess(success);
        response.setData(success);
        return response;
    }

    @Override
    public ArticleResponse<Boolean> deleteArticle(Long id) {
        boolean success = articleService.removeById(id);
        ArticleResponse<Boolean> response = new ArticleResponse<>();
        response.setSuccess(success);
        response.setData(success);
        return response;
    }

    private ArticleInfo convertToInfo(Article article) {
        ArticleInfo info = new ArticleInfo();
        BeanUtils.copyProperties(article, info);
        if (article.getType() != null) {
            info.setType(article.getType().getCode());
        }
        return info;
    }
}

