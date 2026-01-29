import request from "@/utils/request";

/** User info type */
export interface UserInfo {
    id: number | string;
    username: string;
    email: string;
    avatar: string;
    phone: string;
    createTime: string;
}

/** Login Data */
export interface LoginData {
    username: string;
    password?: string;
    rememberMe?: boolean;
}

/** Register Data */
export interface RegisterData {
    username: string;
    password?: string;
    email?: string;
    phone?: string;
}

/** Login Result */
export interface LoginResult {
    token: string;
    userInfo: UserInfo;
}

/** Login API */
export function login(data: LoginData) {
    return request.post<any, LoginResult>("/auth/login", data);
}

/** Register API */
export function register(data: RegisterData) {
    return request.post<any, boolean>("/auth/register", data);
}

/** Logout API */
export function logout() {
    return request.post<any, boolean>("/auth/logout");
}

/** User Update Data */
export interface UserUpdateData {
    username?: string;
    email?: string;
    phone?: string;
    avatar?: string;
}

/** Get User Info API */
export function getUserInfo() {
    return request.get<any, UserInfo>("/user/info");
}

/** Update User Info API */
export function updateUserInfo(data: UserUpdateData) {
    return request.put<any, void>("/user/info", data);
}
