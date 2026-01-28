import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";
// 别名
import path from "node:path";
// UnoCSS
import UnoCSS from "unocss/vite";
// 引入ElementPlus自动导入
import AutoImport from "unplugin-auto-import/vite";
import Components from "unplugin-vue-components/vite";
import { ElementPlusResolver } from "unplugin-vue-components/resolvers";


export default defineConfig({
  plugins: [
    vue(),
    // UnoCSS
    UnoCSS(),
    // 引入ElementPlus自动导入（包含反馈类组件样式）
    AutoImport({
      resolvers: [ElementPlusResolver()],
    }),
    Components({
      resolvers: [ElementPlusResolver()],
    }),
  ],
  server: {
    proxy: {
      "/api": {
        target: "http://localhost:8888",
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ""),
      },
    },
  },
  // 开发运行时别名配置
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "src"), // @ 指向 src 目录
    },
  },
});
