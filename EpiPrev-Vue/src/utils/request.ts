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
        console.log(res);
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
            const errorMessage = res.message || "操作失败";
            console.log(errorMessage);
            if (res.code === "401") {
                const store = useUserStore();
                store.clearUserInfo();
                if (router.currentRoute.value.path !== "/login") {
                    router.push("/login");
                }
            }
            ElMessage.error(errorMessage);
        }
    }
);

export default instance;
