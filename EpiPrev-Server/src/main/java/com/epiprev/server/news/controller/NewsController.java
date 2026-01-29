package com.epiprev.server.news.controller;

import com.epiprev.server.common.result.Result;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.epiprev.server.news.service.NewsService;
import com.epiprev.server.news.vo.NewsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @GetMapping("/list")
    public Result<Page<NewsVO>> list(@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(newsService.getList(page, size));
    }

    @GetMapping("/{id}")
    public Result<NewsVO> getById(@PathVariable Long id) {
        return Result.success(newsService.getDetail(id));
    }
}
