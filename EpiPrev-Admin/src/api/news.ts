import { request } from '@umijs/max';
import type { MultiResult, PageParams, Result } from './common';

export type News = {
  id?: number;
  title?: string;
  summary?: string;
  source?: string;
  level?: string;
  content?: string;
  publishTime?: string;
  createTime?: string;
  updateTime?: string;
};

export async function pageNews(
  params: PageParams & { word?: string; level?: string },
  options?: { [key: string]: any },
) {
  return request<MultiResult<News>>('/api/admin/news/list', {
    method: 'GET',
    params: {
      page: params.current,
      size: params.pageSize,
      word: params.word,
      level: params.level,
    },
    ...(options || {}),
  });
}

export async function addNews(data: News, options?: { [key: string]: any }) {
  return request<Result<any>>('/api/admin/news/save', {
    method: 'POST',
    data,
    ...(options || {}),
  });
}

export async function updateNews(data: News, options?: { [key: string]: any }) {
  return request<Result<any>>('/api/admin/news/save', {
    method: 'POST',
    data,
    ...(options || {}),
  });
}

export async function removeNews(id: number, options?: { [key: string]: any }) {
  return request<Result<any>>(`/api/admin/news/${id}`, {
    method: 'DELETE',
    ...(options || {}),
  });
}
