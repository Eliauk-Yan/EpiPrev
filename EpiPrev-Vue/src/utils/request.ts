import axios from "axios";
import { ElMessage } from "element-plus";
import { useUserStore } from "@/stores/user";
import router from "@/router";

const instance = axios.create({
    baseURL: "/api",
    timeout: 10000,
});

// 用于防止重复弹窗
let isShowingError = false;
const showError = (message: string) => {
    if (!isShowingError) {
        isShowingError = true;
        ElMessage.error(message);
        setTimeout(() => {
            isShowingError = false;
        }, 300);
    }
};

instance.interceptors.request.use((config) => {
    const store = useUserStore();
    if (store.token) {
        config.headers.satoken = store.token;
    }
    return config;
});

instance.interceptors.response.use(
    (response) => {
        const res = response.data;

        // 检查响应格式是否正确
        if (res.success === undefined) {
            // 非标准响应格式，直接返回
            return response.data;
        }

        if (res.success) {
            // 判断是否是分页响应（包含 total, page, size 字段）
            if (res.total !== undefined && res.page !== undefined && res.size !== undefined) {
                // 分页响应，返回完整的分页对象
                return {
                    data: res.data,
                    total: res.total,
                    page: res.page,
                    size: res.size
                };
            }
            // 普通响应，只返回 data
            return res.data;
        } else {
            // 处理业务错误
            const errorCode = res.code;
            const errorMessage = res.message || "操作失败";

            // 未登录或Token过期，跳转登录页
            if (errorCode === "401") {
                const store = useUserStore();
                store.clearUserInfo();
                showError(errorMessage);
                // 如果不是在登录页，则跳转到登录页
                if (router.currentRoute.value.path !== "/login") {
                    router.push("/login");
                }
            } else {
                // 其他业务错误，弹窗提示
                showError(errorMessage);
            }
            return Promise.reject(new Error(errorMessage));
        }
    },
    (error) => {
        // 处理网络错误等
        let message = "网络异常，请检查网络连接";

        if (error.response) {
            // 尝试从响应体获取错误信息（后端统一异常处理返回的格式）
            const resData = error.response.data;
            if (resData && resData.message) {
                message = resData.message;
                // 检查是否需要跳转登录页
                if (resData.code === "401") {
                    const store = useUserStore();
                    store.clearUserInfo();
                    if (router.currentRoute.value.path !== "/login") {
                        router.push("/login");
                    }
                }
            } else {
                // 根据状态码设置默认消息
                switch (error.response.status) {
                    case 401:
                        message = "未授权，请重新登录";
                        const store = useUserStore();
                        store.clearUserInfo();
                        if (router.currentRoute.value.path !== "/login") {
                            router.push("/login");
                        }
                        break;
                    case 403:
                        message = "拒绝访问";
                        break;
                    case 404:
                        message = "请求的资源不存在";
                        break;
                    case 500:
                        message = "服务器内部错误";
                        break;
                    default:
                        message = "请求失败";
                }
            }
        } else if (error.code === "ECONNABORTED") {
            message = "请求超时，请稍后重试";
        } else if (error.message && error.message.includes("Network Error")) {
            message = "网络连接失败，请检查网络";
        }

        showError(message);
        return Promise.reject(error);
    }
);

export default instance;
