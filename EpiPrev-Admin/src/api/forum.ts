import { request } from '@umijs/max';
import type { MultiResult, PageParams, Result } from './common';

export type ForumPost = {
  id?: number;
  userId?: number;
  username?: string;
  title?: string;
  content?: string;
  views?: number;
  createTime?: string;
  updateTime?: string;
};

export async function pageForum(
  params: PageParams & { title?: string },
  options?: { [key: string]: any },
) {
  return request<MultiResult<ForumPost>>('/api/admin/post/list', {
    method: 'GET',
    params: {
      page: params.current,
      size: params.pageSize,
      title: params.title,
    },
    ...(options || {}),
  });
}

export async function removeForum(
  id: number,
  options?: { [key: string]: any },
) {
  return request<Result<any>>(`/api/admin/post/${id}`, {
    method: 'DELETE',
    ...(options || {}),
  });
}
