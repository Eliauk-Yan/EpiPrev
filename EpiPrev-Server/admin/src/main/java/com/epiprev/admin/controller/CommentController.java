package com.epiprev.admin.controller;

import com.epiprev.admin.service.CommentService;
import com.epiprev.common.web.result.MultiResult;
import com.epiprev.common.web.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/comment")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/list")
    public MultiResult<Map<String, Object>> list(@RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String content) {
        return commentService.pageComment(page, size, content);
    }

    @GetMapping("/{id}")
    public Result<Map<String, Object>> getById(@PathVariable Long id) {
        return commentService.getCommentById(id);
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return commentService.deleteComment(id);
    }
}
