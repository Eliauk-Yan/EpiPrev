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
