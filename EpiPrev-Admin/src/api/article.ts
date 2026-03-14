import { request } from '@umijs/max';
import type { MultiResult, PageParams, Result } from './common';

export type Article = {
  id?: number;
  title?: string;
  category?: string;
  summary?: string;
  content?: string;
  cover?: string;
  videoUrl?: string;
  /**
   * 类型：0=图文，1=视频
   */
  type?: number;
  views?: number;
};

export async function pageArticle(
  params: PageParams & { title?: string; category?: string },
  options?: { [key: string]: any },
) {
  return request<MultiResult<Article>>('/api/admin/article/list', {
    method: 'GET',
    params: {
      page: params.current,
      size: params.pageSize,
      title: params.title,
      category: params.category,
    },
    ...(options || {}),
  });
}

export async function addArticle(
  data: Article,
  options?: { [key: string]: any },
) {
  return request<Result<any>>('/api/admin/article/save', {
    method: 'POST',
    data,
    ...(options || {}),
  });
}

export async function updateArticle(
  data: Article,
  options?: { [key: string]: any },
) {
  return request<Result<any>>('/api/admin/article/save', {
    method: 'POST',
    data,
    ...(options || {}),
  });
}

export async function removeArticle(
  id: number,
  options?: { [key: string]: any },
) {
  return request<Result<any>>(`/api/admin/article/${id}`, {
    method: 'DELETE',
    ...(options || {}),
  });
}

/**
 * 文章文件上传（封面/视频），走 admin 模块接口
 */
export async function uploadArticleFile(
  file: File,
  type: 'cover' | 'video',
  options?: { [key: string]: any },
) {
  const formData = new FormData();
  formData.append('file', file);
  formData.append('type', type);
  return request<Result<string>>('/api/admin/article/upload', {
    method: 'POST',
    data: formData,
    requestType: 'form',
    ...(options || {}),
  });
}
