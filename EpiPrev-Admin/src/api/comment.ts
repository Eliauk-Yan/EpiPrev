import { request } from '@umijs/max';
import type { MultiResult, PageParams, Result } from './common';

export type Comment = {
  id?: number;
  postId?: number;
  userId?: number;
  content?: string;
  createTime?: string;
};

export async function pageComment(
  params: PageParams & { content?: string },
  options?: { [key: string]: any },
) {
  return request<MultiResult<Comment>>('/api/admin/comment/list', {
    method: 'GET',
    params: {
      page: params.current,
      size: params.pageSize,
      content: params.content,
    },
    ...(options || {}),
  });
}

export async function removeComment(
  id: number,
  options?: { [key: string]: any },
) {
  return request<Result<any>>(`/api/admin/comment/${id}`, {
    method: 'DELETE',
    ...(options || {}),
  });
}
