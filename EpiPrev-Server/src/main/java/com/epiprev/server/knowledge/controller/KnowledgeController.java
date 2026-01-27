package com.epiprev.server.knowledge.controller;
import com.epiprev.server.common.result.Result;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.epiprev.server.knowledge.entity.KnowledgeArticle;
import com.epiprev.server.knowledge.service.KnowledgeService;
import com.epiprev.server.knowledge.vo.ArticleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/knowledge")
public class KnowledgeController {

    @Autowired
    private KnowledgeService knowledgeService;

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String category) {
        Page<ArticleVO> result = knowledgeService.getList(page, size, category);
        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        ArticleVO article = knowledgeService.getDetail(id);

        if (article != null) {
            // Increments views in DB but returns cached article for speed.
            // Ideally we'd update cache or use a separate view counter service.
            // For simplicity, we just trigger DB update here.
            KnowledgeArticle update = new KnowledgeArticle();
            update.setId(id);
            update.setViews(article.getViews() + 1);
            knowledgeService.updateById(update);

            // Update local object to show user the increment
            article.setViews(article.getViews() + 1);
        }

        return Result.success(article);
    }
}



