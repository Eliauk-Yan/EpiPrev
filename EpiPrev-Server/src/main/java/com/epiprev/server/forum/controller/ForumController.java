package com.epiprev.server.forum.controller;

import com.epiprev.server.common.result.Result;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.epiprev.server.common.result.enums.ResultCode;
import com.epiprev.server.forum.dto.CommentDTO;
import com.epiprev.server.forum.dto.PostDTO;
import com.epiprev.server.forum.service.ForumService;
import com.epiprev.server.forum.vo.PostVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/forum")
public class ForumController {

    @Autowired
    private ForumService forumService;

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<PostVO> result = forumService.getList(page, size);
        return Result.success(result);
    }

    @PostMapping("/post")
    public Result createPost(@RequestBody PostDTO postDTO) {
        try {
            forumService.createPost(postDTO);
            return Result.success("发布成功");
        } catch (RuntimeException e) {
            return Result.error(ResultCode.FAIL);
        }
    }

    @PostMapping("/comment")
    public Result createComment(@RequestBody CommentDTO commentDTO) {
        try {
            forumService.createComment(commentDTO);
            return Result.success("评论成功");
        } catch (RuntimeException e) {
            return Result.error(ResultCode.FAIL);
        }
    }
}



