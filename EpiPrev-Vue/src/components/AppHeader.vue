<script setup lang="ts">
import { useUserStore } from "@/stores/user";
import { useRouter } from "vue-router";


const userStore = useUserStore();
const router = useRouter();

const navItems = [
  { name: "首页", path: "/" },
  { name: "知识普及", path: "/knowledge" },
  { name: "疫情动态", path: "/news" },
  { name: "互动交流", path: "/forum" },
  { name: "健康管理", path: "/health" },
];

const handleLogout = async () => {
  await userStore.logout();
  router.push("/");
};
</script>

<template>
  <header class="header">
    <div class="header-content">
      <div class="logo" @click="router.push('/')">
        <span class="logo-icon">🛡️</span>
        <span class="logo-text">传染病预防宣传系统</span>
      </div>

      <nav class="nav">
        <RouterLink
          v-for="item in navItems"
          :key="item.path"
          :to="item.path"
          class="nav-item"
        >
          {{ item.name }}
        </RouterLink>
      </nav>

      <div class="user-area">
        <template v-if="userStore.isLoggedIn">
          <el-dropdown>
            <span class="user-info">
              <el-avatar :size="32">{{ userStore.user?.username?.charAt(0) }}</el-avatar>
              <span class="ml-2">{{ userStore.user?.username }}</span>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
        <template v-else>
          <el-button type="primary" text @click="router.push('/login')">登录</el-button>
          <el-button type="primary" @click="router.push('/register')">注册</el-button>
        </template>
      </div>
    </div>
  </header>
</template>

<style scoped>
.header {
  background: linear-gradient(135deg, #409EFF, #67C23A);
  color: white;
  padding: 0 20px;
  position: sticky;
  top: 0;
  z-index: 100;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.logo {
  display: flex;
  align-items: center;
  cursor: pointer;
  font-weight: 600;
}

.logo-icon {
  font-size: 24px;
  margin-right: 8px;
}

.logo-text {
  font-size: 18px;
}

.nav {
  display: flex;
  gap: 8px;
}

.nav-item {
  padding: 8px 16px;
  border-radius: 4px;
  transition: background 0.2s;
}

.nav-item:hover {
  background: rgba(255, 255, 255, 0.2);
}

.nav-item.router-link-active {
  background: rgba(255, 255, 255, 0.3);
}

.user-area {
  display: flex;
  align-items: center;
  gap: 8px;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
}
</style>
