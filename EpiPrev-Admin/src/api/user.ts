import { request } from '@umijs/max';
import type { MultiResult, PageParams, Result } from './common';

export type SysUser = {
  id?: number;
  nickName?: string;
  telephone?: string;
  email?: string;
  avatar?: string;
  state?: string | number;
  role?: string;
  createTime?: string;
};

export async function pageUser(
  params: PageParams & { keyword?: string; role?: string; state?: string },
  options?: { [key: string]: any },
) {
  return request<MultiResult<SysUser>>('/api/admin/user/list', {
    method: 'GET',
    params: {
      page: params.current,
      size: params.pageSize,
      keyword: params.keyword,
      role: params.role,
      state: params.state,
    },
    ...(options || {}),
  });
}

export async function updateUserStatus(
  id: number,
  status: string,
  options?: { [key: string]: any },
) {
  return request<Result<any>>('/api/admin/user/status', {
    method: 'PUT',
    data: {
      id,
      status,
    },
    ...(options || {}),
  });
}
