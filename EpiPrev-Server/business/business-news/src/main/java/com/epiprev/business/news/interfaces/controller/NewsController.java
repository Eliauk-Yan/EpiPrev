package com.epiprev.business.news.interfaces.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.epiprev.business.news.domain.entity.NewsInfo;
import com.epiprev.business.news.domain.vo.NewsVO;
import com.epiprev.business.news.service.NewsInfoService;
import com.epiprev.common.web.result.MultiResult;
import com.epiprev.common.web.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
public class NewsController {

    private final NewsInfoService newsInfoService;

    /**
     * 新闻详情（前台使用）
     */
    @GetMapping("/{id}")
    public Result<NewsVO> detail(@PathVariable Long id) {
        NewsInfo info = newsInfoService.getById(id);
        if (info == null) {
            return Result.success(null);
        }
        NewsVO vo = new NewsVO();
        BeanUtils.copyProperties(info, vo);
        return Result.success(vo);
    }

    /**
     * 新闻列表（前台使用）
     */
    @GetMapping("/list")
    public MultiResult<NewsVO> list(@RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "10") Integer size,
                                    @RequestParam(required = false) String word,
                                    @RequestParam(required = false) String level) {
        LambdaQueryWrapper<NewsInfo> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(word)) {
            wrapper.and(w -> w.like(NewsInfo::getTitle, word)
                    .or()
                    .like(NewsInfo::getSummary, word));
        }
        if (StringUtils.hasText(level)) {
            wrapper.eq(NewsInfo::getLevel, level);
        }
        wrapper.orderByDesc(NewsInfo::getPublishTime)
                .orderByDesc(NewsInfo::getCreateTime);

        Page<NewsInfo> newsPage = newsInfoService.page(new Page<>(page, size), wrapper);
        List<NewsVO> data = newsPage.getRecords().stream().map(item -> {
            NewsVO vo = new NewsVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).toList();

        return MultiResult.multiSuccess(data, newsPage.getTotal(), page, size);
    }
}

