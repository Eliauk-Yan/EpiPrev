import request from "@/utils/request";

/** User info type */
export interface UserInfo {
    id: number | string;
    username: string;
    email: string;
    avatar: string;
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
