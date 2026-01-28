import request from "@/utils/request";

/** 帖子信息类型 */
export interface PostVO {
    id: number;
    title: string;
    content: string;
    authorId: number;
    authorName: string;
    views: number;
    replies: number;
    createTime: string;
    comments?: CommentVO[];
}

/** 评论信息类型 */
export interface CommentVO {
    id: number;
    postId: number;
    content: string;
    authorId: number;
    authorName: string;
    createTime: string;
}

/** 帖子列表查询参数 */
export interface PostListParams {
    current?: number;   // 当前页码
    size?: number;      // 每页条数
    word?: string;      // 搜索关键词
}

/** 发帖参数 */
export interface PostDTO {
    title: string;
    content: string;
}

/** 评论参数 */
export interface CommentDTO {
    postId: number;
    content: string;
}

/** 分页结果类型 - 与后端 MultiResult 格式匹配 */
export interface PageResult<T> {
    data: T[];          // 后端返回的是 data 字段
    total: number;
    page: number;       // 后端返回的是 page 字段
    size: number;
}

/**
 * 获取帖子列表（分页）
 * @param params 查询参数
 */
export function getPostList(params?: PostListParams) {
    return request.get<any, PageResult<PostVO>>("/forum/list", { params });
}

/**
 * 获取热门帖子列表
 * @param limit 限制条数，默认5条
 */
export function getHotPosts(limit: number = 5) {
    return request.get<any, PostVO[]>("/forum/hot", { params: { limit } });
}

/**
 * 获取帖子详情
 * @param postId 帖子ID
 */
export function getPostDetail(postId: number | string) {
    return request.get<any, PostVO>(`/forum/detail/${postId}`);
}

/**
 * 发布帖子
 * @param data 帖子数据
 */
export function createPost(data: PostDTO) {
    return request.post<any, boolean>("/forum/post", data);
}

/**
 * 发表评论
 * @param data 评论数据
 */
export function createComment(data: CommentDTO) {
    return request.post<any, boolean>("/forum/comment", data);
}
