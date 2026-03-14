import { request } from '@umijs/max';
import type { Result } from './common';

export type LoginParams = {
  username?: string;
  password?: string;
  autoLogin?: boolean;
};

export type CurrentUser = {
  id?: number;
  nickName?: string;
  avatar?: string;
  telephone?: string;
  state?: string;
  certification?: boolean;
  role?: string;
  createTime?: string;
};

/** 管理员登录接口 POST /api/auth/login/admin */
export async function adminLogin(
  body: LoginParams,
  options?: { [key: string]: any },
) {
  return request<Result<any>>('/api/auth/login/admin', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 获取当前的用户 GET /api/user/getUserInfo */
export async function currentUser(options?: { [key: string]: any }) {
  return request<Result<CurrentUser>>('/api/user/getUserInfo', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 退出登录接口 POST /api/auth/logout */
export async function outLogin(options?: { [key: string]: any }) {
  return request<Result<any>>('/api/auth/logout', {
    method: 'POST',
    ...(options || {}),
  });
}
