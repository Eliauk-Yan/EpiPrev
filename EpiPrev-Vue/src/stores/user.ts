import { defineStore } from "pinia";
import { ref, computed } from "vue";

interface User {
    id: number;
    username: string;
    email: string;
}

export const useUserStore = defineStore(
    "user",
    () => {
        const user = ref<User | null>(null);
        const token = ref<string>("");

        const isLoggedIn = computed(() => !!token.value);

        const login = (userData: User, userToken: string) => {
            user.value = userData;
            token.value = userToken;
        };

        const logout = () => {
            user.value = null;
            token.value = "";
        };

        return { user, token, isLoggedIn, login, logout };
    },
    { persist: true }
);
