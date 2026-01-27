import { defineStore } from "pinia";
import { ref, computed } from "vue";
import { login as loginApi, logout as logoutApi, type LoginData, type UserInfo } from "@/api/user";

export const useUserStore = defineStore(
    "user",
    () => {
        const user = ref<UserInfo | null>(null);
        const token = ref<string>("");

        const isLoggedIn = computed(() => !!token.value);

        const login = async (form: LoginData) => {
            const res = await loginApi(form);
            if (res) {
                user.value = res.userInfo;
                token.value = res.token;
                return true;
            }
            return false;
        };

        const setUser = (userData: UserInfo, userToken: string) => {
            user.value = userData;
            token.value = userToken;
        }

        const logout = async () => {
            try {
                await logoutApi();
            } finally {
                user.value = null;
                token.value = "";
            }
        };

        return { user, token, isLoggedIn, login, logout, setUser };
    },
    { persist: true }
);
