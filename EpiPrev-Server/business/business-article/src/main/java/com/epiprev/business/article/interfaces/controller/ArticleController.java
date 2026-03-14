package com.epiprev.business.article.interfaces.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.epiprev.business.article.domain.entity.Article;
import com.epiprev.business.article.domain.vo.ArticleVO;
import com.epiprev.business.article.service.ArticleService;
import com.epiprev.common.web.result.Result;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleController {

    private final ArticleService articleService;

    /**
     * 分页查询文章列表
     */
    @GetMapping("/list")
    public Result<List<ArticleVO>> list(@RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String category) {
        Page<Article> articlePage = articleService.page(new Page<>(page, size),
                new LambdaQueryWrapper<Article>()
                        .eq(StringUtils.isNotBlank(category), Article::getCategory, category)
                        .orderByDesc(Article::getCreateTime));
        List<ArticleVO> result = articlePage.getRecords().stream().map(item -> {
            ArticleVO articleVO = new ArticleVO();
            BeanUtils.copyProperties(item, articleVO);
            return articleVO;
        }).toList();
        return Result.success(result);
    }

    /**
     * 获取文章详情
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
