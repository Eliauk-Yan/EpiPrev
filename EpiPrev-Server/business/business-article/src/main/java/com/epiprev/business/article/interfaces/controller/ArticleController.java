package com.epiprev.business.article.interfaces.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.epiprev.business.article.domain.entity.Article;
import com.epiprev.business.article.domain.enums.ArticleType;
import com.epiprev.business.article.domain.vo.ArticleVO;
import com.epiprev.business.article.service.ArticleService;
import com.epiprev.common.web.result.MultiResult;
import com.epiprev.common.web.result.Result;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/list")
    public MultiResult<ArticleVO> list(@RequestParam(defaultValue = "1") Integer current,
                                       @RequestParam(defaultValue = "10") Integer size,
                                       @RequestParam(required = false) Integer type,
                                       @RequestParam(required = false) String word) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        if (type != null) {
            ArticleType articleType = ArticleType.fromCode(type);
            if (articleType != null) {
                wrapper.eq(Article::getType, articleType);
            }
        }
        if (StringUtils.isNotBlank(word)) {
            wrapper.and(w -> w.like(Article::getTitle, word)
                    .or()
                    .like(Article::getSummary, word));
        }
        wrapper.orderByDesc(Article::getCreateTime);

        Page<Article> articlePage = articleService.page(new Page<>(current, size), wrapper);
        List<ArticleVO> result = articlePage.getRecords().stream().map(item -> {
            ArticleVO articleVO = new ArticleVO();
            BeanUtils.copyProperties(item, articleVO);
            return articleVO;
        }).toList();
        return MultiResult.multiSuccess(result, articlePage.getTotal(), current, size);
    }

    /**
     * 获取文章详情（前台使用）
     */
    @GetMapping("/detail/{id}")
    public Result<ArticleVO> detail(@PathVariable Long id) {
        Article article = articleService.getById(id);
        if (article != null) {
            // 增加阅读量
            article.setViews(article.getViews() + 1);
            articleService.updateById(article);
        }
        ArticleVO articleVO = new ArticleVO();
        BeanUtils.copyProperties(article, articleVO);
        return Result.success(articleVO);
    }
}

