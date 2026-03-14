package com.epiprev.admin.controller;

import com.epiprev.admin.service.NewsService;
import com.epiprev.common.web.result.MultiResult;
import com.epiprev.common.web.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @GetMapping("/list")
    public MultiResult<Map<String, Object>> list(@RequestParam(defaultValue = "1") Integer page,
                                                  @RequestParam(defaultValue = "10") Integer size,
                                                  @RequestParam(required = false) String word,
                                                  @RequestParam(required = false) String level) {
        return newsService.pageNews(page, size, word, level);
    }

    @GetMapping("/{id}")
    public Result<Map<String, Object>> getById(@PathVariable Long id) {
        return newsService.getNewsById(id);
    }

    @PostMapping("/save")
    public Result<Boolean> save(@RequestBody Map<String, Object> news) {
        return newsService.saveOrUpdateNews(news);
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return newsService.deleteNews(id);
    }
}
