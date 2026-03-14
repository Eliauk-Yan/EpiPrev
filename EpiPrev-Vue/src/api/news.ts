import request from "@/utils/request";

/** 新闻信息类型（与后端 NewsVO/NewsInfo 一致） */
export interface NewsVO {
  id: number;
  title: string;
  summary: string;
  source: string;
  level: string;
  content: string;
  publishTime: string;
  createTime?: string;
  updateTime?: string;
}

/** 新闻列表查询参数 */
export interface NewsListParams {
  page?: number;
  size?: number;
  word?: string;
  level?: string;
}

/** 分页结果（与后端 MultiResult 一致） */
export interface NewsPageResult {
  data: NewsVO[];
  total: number;
  page: number;
  size: number;
}

/**
 * 获取新闻列表（分页），返回 { data, total, page, size }
 */
export function getNewsList(params: NewsListParams) {
  return request.get<NewsPageResult>("/news/list", { params });
}

/**
 * 获取新闻详情，直接返回新闻对象
 */
export function getNewsDetail(id: string | number) {
  return request.get<NewsVO>(`/news/${id}`);
}
