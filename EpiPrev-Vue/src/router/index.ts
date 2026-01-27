import { createRouter, createWebHistory } from "vue-router";
import { useUserStore } from "@/stores/user";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: () => import("@/views/Home/index.vue"),
      meta: { requiresAuth: true },
    },
    {
      path: "/login",
      name: "login",
      component: () => import("@/views/Auth/Login.vue"),
      meta: { requiresAuth: false },
    },
    {
      path: "/register",
      name: "register",
      component: () => import("@/views/Auth/Register.vue"),
      meta: { requiresAuth: false },
    },
    {
      path: "/knowledge",
      name: "knowledge",
      component: () => import("@/views/Knowledge/List.vue"),
      meta: { requiresAuth: true },
    },
    {
      path: "/knowledge/:id",
      name: "knowledge-detail",
      component: () => import("@/views/Knowledge/Detail.vue"),
      meta: { requiresAuth: true },
    },
    {
      path: "/news",
      name: "news",
      component: () => import("@/views/News/List.vue"),
      meta: { requiresAuth: true },
    },
    {
      path: "/news/:id",
      name: "news-detail",
      component: () => import("@/views/News/Detail.vue"),
      meta: { requiresAuth: true },
    },
    {
      path: "/forum",
      name: "forum",
      component: () => import("@/views/Forum/index.vue"),
      meta: { requiresAuth: true },
    },
    {
      path: "/health",
      name: "health",
      component: () => import("@/views/Health/index.vue"),
      meta: { requiresAuth: true },
    },
    {
      path: "/profile",
      name: "profile",
      component: () => import("@/views/User/Profile.vue"),
      meta: { requiresAuth: true },
    },
  ],
});

router.beforeEach((to, _from, next) => {
  const userStore = useUserStore();
  const isAuthenticated = userStore.isLoggedIn; // Assuming isLoggedIn is a getter or state

  if (to.meta.requiresAuth && !isAuthenticated) {
    next("/login");
  } else if (isAuthenticated && (to.path === "/login" || to.path === "/register")) {
    next("/"); // Redirect to home if already logged in
  } else {
    next();
  }
});

export default router;
