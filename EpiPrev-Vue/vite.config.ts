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
    // 引入ElementPlus自动导入
    AutoImport({
      resolvers: [ElementPlusResolver()],
    }),
    Components({
      resolvers: [ElementPlusResolver()],
    }),
  ],
  // 开发运行时别名配置
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "src"), // @ 指向 src 目录
    },
  },
});
