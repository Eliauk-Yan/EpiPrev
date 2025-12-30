import { createRouter, createWebHistory } from "vue-router";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: () => import("@/views/Home/index.vue"),
    },
    {
      path: "/login",
      name: "login",
      component: () => import("@/views/Auth/Login.vue"),
    },
    {
      path: "/register",
      name: "register",
      component: () => import("@/views/Auth/Register.vue"),
    },
    {
      path: "/knowledge",
      name: "knowledge",
      component: () => import("@/views/Knowledge/List.vue"),
    },
    {
      path: "/knowledge/:id",
      name: "knowledge-detail",
      component: () => import("@/views/Knowledge/Detail.vue"),
    },
    {
      path: "/news",
      name: "news",
      component: () => import("@/views/News/List.vue"),
    },
    {
      path: "/news/:id",
      name: "news-detail",
      component: () => import("@/views/News/Detail.vue"),
    },
    {
      path: "/forum",
      name: "forum",
      component: () => import("@/views/Forum/index.vue"),
    },
    {
      path: "/health",
      name: "health",
      component: () => import("@/views/Health/index.vue"),
    },
  ],
});

export default router;
