import request from "@/utils/request";

/** 文章类型常量 - 与数据库定义保持一致 */
export const ArticleType = {
    IMAGE: 0,  // 图文 (数据库: type=0)
    VIDEO: 1   // 视频 (数据库: type=1)
} as const;

/** 文章信息类型 */
export interface ArticleVO {
    id: number;
    title: string;
    category: string;
    summary: string;
    content: string;
    cover: string;
    videoUrl: string;
    type: number;
    views: number;
    createTime: string;
    updateTime: string;
}

/** 文章列表查询参数 */
export interface ArticleListParams {
    type?: number;      // 文章类型：0=图文，1=视频
    current?: number;   // 当前页码
    size?: number;      // 每页条数
    word?: string;      // 搜索关键词
}

/** 分页结果类型 - 与后端 MultiResult 格式匹配 */
export interface PageResult<T> {
    data: T[];          // 后端返回的是 data 字段
    total: number;
    page: number;       // 后端返回的是 page 字段
    size: number;
}

/**
 * 获取文章列表（分页）
 * @param params 查询参数
 */
export function getArticleList(params: ArticleListParams) {
  return request.get<any, PageResult<ArticleVO>>("/article/list", { params });
}

/**
 * 获取文章详情
 * @param id 文章ID
 */
export function getArticleDetail(id: number | string) {
    return request.get<any, ArticleVO>(`/article/${id}`);
}

/**
 * 获取推荐图文列表（分页）
 * @param current 当前页码
 * @param size 每页条数
 */
export function getRecommendedArticles(current: number = 1, size: number = 6) {
    return getArticleList({ type: ArticleType.IMAGE, current, size });
}

/**
 * 获取热门视频列表
 * @param size 获取条数，默认4条
 */
export function getHotVideos(size: number = 4) {
    return getArticleList({ type: ArticleType.VIDEO, current: 1, size });
}

/**
 * 获取文章排行榜
 * @param size 获取条数，默认10条
 */
export function getArticleRanking(size: number = 10) {
    return getArticleList({ type: ArticleType.IMAGE, current: 1, size });
}
