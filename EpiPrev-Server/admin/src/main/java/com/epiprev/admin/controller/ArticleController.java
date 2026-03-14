package com.epiprev.admin.controller;

import com.epiprev.admin.service.ArticleService;
import com.epiprev.common.file.service.FileService;
import com.epiprev.common.web.result.MultiResult;
import com.epiprev.common.web.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    private final FileService fileService;

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

    /**
     * 文章文件上传（封面 / 视频），管理端使用
     */
    @PostMapping("/upload")
    public Result<String> upload(@RequestPart("file") MultipartFile file,
                                 @RequestParam("type") String type) {
        String prefix;
        if ("cover".equalsIgnoreCase(type)) {
            prefix = "article/cover/";
        } else if ("video".equalsIgnoreCase(type)) {
            prefix = "article/video/";
        } else {
            prefix = "article/other/";
        }
        String filePath = prefix + System.currentTimeMillis();
        String url = fileService.uploadFile(file, filePath);
        return Result.success(url);
    }
}
