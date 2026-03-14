package com.epiprev.admin.controller;

import com.epiprev.admin.service.ArticleService;
import com.epiprev.common.web.result.MultiResult;
import com.epiprev.common.web.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/list")
    public MultiResult<Map<String, Object>> list(@RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String category) {
        return articleService.pageArticle(page, size, title, category);
    }

    @GetMapping("/{id}")
    public Result<Map<String, Object>> getById(@PathVariable Long id) {
        return articleService.getArticleById(id);
    }

    @PostMapping("/save")
    public Result<Boolean> save(@RequestBody Map<String, Object> article) {
        return articleService.saveOrUpdateArticle(article);
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return articleService.deleteArticle(id);
    }
}
