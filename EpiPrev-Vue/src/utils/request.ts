import axios from "axios";
import { ElMessage } from "element-plus";
import { useUserStore } from "@/stores/user";

const instance = axios.create({
    baseURL: "/api",
    timeout: 10000,
});

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
            ElMessage.error(res.message || "请求出错");
            return Promise.reject(new Error(res.message || "Error"));
        }
    },
    (error) => {
        ElMessage.error(error.message || "请求失败");
        return Promise.reject(error);
    }
);

export default instance;
