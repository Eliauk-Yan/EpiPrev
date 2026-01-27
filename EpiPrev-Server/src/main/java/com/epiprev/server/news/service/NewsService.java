package com.epiprev.server.news.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.epiprev.server.news.entity.NewsInfo;
import com.epiprev.server.news.vo.NewsVO;

public interface NewsService extends IService<NewsInfo> {
    Page<NewsVO> getList(int page, int size);

    NewsVO getDetail(Long id);
}


