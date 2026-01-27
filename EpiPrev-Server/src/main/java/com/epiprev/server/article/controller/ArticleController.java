package com.epiprev.server.article.controller;

import com.epiprev.server.article.domain.dto.ArticleListDTO;
import com.epiprev.server.common.result.MultiResult;
import com.epiprev.server.common.result.Result;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.epiprev.server.article.service.ArticleService;
import com.epiprev.server.article.domain.vo.ArticleVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/list")
    public MultiResult<ArticleVO> list(ArticleListDTO dto) {
        // 1. 获取分页对象
        Page<ArticleVO> page = articleService.getList(dto);
        // 2. 组装并返回分页对象
        return MultiResult.multiSuccess(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
    }

    @GetMapping("/{id}")
    public Result<ArticleVO> getById(@PathVariable("id") Long articleId) {
        return Result.success(articleService.getDetail(articleId));
    }
}
