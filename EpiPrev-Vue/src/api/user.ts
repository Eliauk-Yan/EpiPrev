import request from "@/utils/request";

/** User info type */
export interface UserInfo {
    id: number | string;
    nickName: string;
    avatar: string;
    telephone: string;
    state: string;
    certification: boolean;
    role: string;
    createTime: string;
}

/** Login Data */
export interface LoginData {
    phone: string;
    password?: string;
    rememberMe?: boolean;
}

/** Register Data */
export interface RegisterData {
    nickName: string;
    password?: string;
    email?: string;
    phone?: string;
}

/** Login Result */
export interface LoginResult {
    token: string;
    userInfo: UserInfo;
}

/** User Update Data */
export interface UserUpdateData {
    nickName?: string;
    avatar?: string;
    telephone?: string;
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

/** Get User Info API */
export function getUserInfo() {
    return request.get<any, UserInfo>("/user/getUserInfo");
}

/** Update Avatar API */
export function updateAvatar(file: File) {
    const formData = new FormData();
    formData.append("avatar", file);
    return request.put<any, boolean>("/user/avatar", formData, {
        headers: { "Content-Type": "multipart/form-data" }
    });
}

/** Update Nickname API */
export function updateNickName(nickName: string) {
    return request.post<any, boolean>("/user/modifyNickName", null, { params: { nickName } });
}
