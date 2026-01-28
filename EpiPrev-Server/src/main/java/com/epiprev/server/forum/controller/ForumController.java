package com.epiprev.server.forum.controller;

import com.epiprev.server.common.result.MultiResult;
import com.epiprev.server.common.result.Result;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.epiprev.server.forum.domain.dto.CommentDTO;
import com.epiprev.server.forum.domain.dto.PostDTO;
import com.epiprev.server.forum.domain.dto.PostListDTO;
import com.epiprev.server.forum.service.ForumService;
import com.epiprev.server.forum.domain.vo.PostVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/forum")
public class ForumController {

    private final ForumService forumService;

    @GetMapping("/list")
    public MultiResult<PostVO> list(PostListDTO dto) {
        Page<PostVO> page = forumService.getList(dto);
        return MultiResult.multiSuccess(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
    }

    @GetMapping("/hot")
    public Result<List<PostVO>> hotList(@RequestParam(value = "limit", defaultValue = "5") int limit) {
        return Result.success(forumService.getHotList(limit));
    }

    @PostMapping("/post")
    public Result<Boolean> createPost(@RequestBody PostDTO postDTO) {
        forumService.createPost(postDTO);
        return Result.success(true);
    }

    @PostMapping("/comment")
    public Result<Boolean> createComment(@RequestBody CommentDTO commentDTO) {
        forumService.createComment(commentDTO);
        return Result.success(true);
    }

    @GetMapping("/detail/{postId}")
    public Result<PostVO> getDetail(@PathVariable("postId") Long postId) {
        return Result.success(forumService.getDetail(postId));
    }
}
