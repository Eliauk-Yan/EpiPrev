package com.epiprev.business.news.interfaces.controller;

import com.epiprev.business.news.domain.vo.NewsVO;
import com.epiprev.common.web.result.MultiResult;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
public class NewsController {

    /**
     * 新闻列表（前台使用）
     */
    @GetMapping("/list")
    public MultiResult<NewsVO> list(@RequestParam(defaultValue = "1") Integer page) throws IOException {
        List<NewsVO> list = new ArrayList<>();
        Document doc = Jsoup.connect("https://www.chinacdc.cn/zxyw/" + "index_" + page + ".html")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .timeout(10000)
                .get();
        Elements newsItems = doc.select("ul.xw_list > li");
        for (int i = 0; i < newsItems.size(); i++) {
            NewsVO newsVO = new NewsVO();
            newsVO.setId((page - 1) * 12L + i + 1);
            Element item = newsItems.get(i);
            // 提取标题
            Element titleLink = item.select("dd > a").first();
            if (titleLink == null) continue;
            String title = titleLink.ownText();  // 获取标题文本（不含日期span）
            newsVO.setTitle(title);
            // 日期
            Element dateSpan = titleLink.select("span").first();
            if (dateSpan != null) {
                newsVO.setPublishTime(LocalDate.parse(dateSpan.text()).atStartOfDay());
            }
            // 简介
            Element summaryPara = item.select("p.zy").first();
            if (summaryPara != null) {
                newsVO.setSummary(summaryPara.text().trim());

            }
            // 提取链接（可选）
            String link = titleLink.attr("href");
            // 爬取内容
            if (link.startsWith("./")) {
                link = link.substring(2);
            }
            Document contentDoc = Jsoup.connect("https://www.chinacdc.cn/zxyw/" + link)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .timeout(10000)
                    .get();
            Element contentDiv = contentDoc.select("div.content#articleCon").first();
            if (contentDiv != null) {
                newsVO.setContent(contentDiv.text());
            }
            // 设置来源
            newsVO.setSource("中国疾病预防控制中心");
            list.add(newsVO);
        }
        int total = 0;
        Element tailLink = doc.select("a#pagenav_tail").first();
        if (tailLink != null) {
            String href = tailLink.attr("href");
            // 正则表达式提取数字
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(href);
            if (matcher.find()) {
                String pageNumStr = matcher.group();
                total = Integer.parseInt(pageNumStr) * 12;
            }
        }
        return MultiResult.multiSuccess(list, total, page, 12);
    }
}

