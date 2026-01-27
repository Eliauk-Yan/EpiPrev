package com.epiprev.server.news.controller;
import com.epiprev.server.common.result.Result;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.epiprev.server.news.service.NewsService;
import com.epiprev.server.news.vo.NewsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<NewsVO> result = newsService.getList(page, size);
        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return Result.success(newsService.getDetail(id));
    }
}



