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
  router.push("/login");
};
</script>

<template>
  <header class="header">
    <div class="header-content">
      <div class="logo" @click="router.push('/')">
        <!-- Removed shield icon -->
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
              <span class="username">{{ userStore.user?.username }}</span>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="router.push('/profile')">个人中心</el-dropdown-item>
                <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
      </div>
    </div>
  </header>
</template>

<style scoped>
.header {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
  padding: 0 20px;
  position: sticky;
  top: 0;
  z-index: 1000;
  transition: all 0.3s ease;
}

.header-content {
  max-width: 1400px; /* Wider container */
  margin: 0 auto;
  height: 70px; /* Slightly taller */
  display: flex;
  align-items: center;
  justify-content: space-between;
  position: relative; /* Essential for absolute centering of nav */
}

.logo {
  display: flex;
  align-items: center;
  cursor: pointer;
  font-weight: 700;
  color: var(--primary-color);
  transition: opacity 0.3s;
}

.logo:hover {
  opacity: 0.8;
}

/* Removed logo-icon styles */

.logo-text {
  font-size: 20px;
  background: linear-gradient(135deg, #409EFF, #67C23A);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
  letter-spacing: 0.5px;
}

.nav {
  display: flex;
  gap: 12px;
  background: rgba(240, 242, 245, 0.5); /* Subtle pill container */
  padding: 4px;
  border-radius: 99px;
  /* Centering Logic */
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
}

.nav-item {
  padding: 8px 24px;
  border-radius: 99px; /* Pill shape */
  font-size: 15px;
  font-weight: 500;
  color: var(--text-regular);
  transition: all 0.3s ease;
}

.nav-item:hover {
  color: var(--primary-color);
  background: rgba(255, 255, 255, 0.8);
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}

.nav-item.router-link-active {
  background: white;
  color: var(--primary-color);
  box-shadow: 0 2px 12px rgba(64, 158, 255, 0.2);
  font-weight: 600;
}

.user-area {
  display: flex;
  align-items: center;
  gap: 16px;
  /* Ensure user area stays on the right */
  margin-left: auto; 
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 4px 12px 4px 4px;
  border-radius: 99px;
  transition: background 0.3s;
}

.user-info:hover {
  background: rgba(0,0,0,0.03);
}

.username {
  margin-left: 8px;
  font-weight: 500;
  color: var(--text-regular);
}

:deep(.el-avatar) {
  border: 2px solid white;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}
</style>
