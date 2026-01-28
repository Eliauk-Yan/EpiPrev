import { createApp } from "vue";
// 引入 App
import App from "./App.vue";
// 引入全局样式
import "./style.css";
// 引入 UnoCSS
import "virtual:uno.css";
// 导入路由
import router from "./router";
// 引入 Pinia
import { createPinia } from "pinia";
// 引入持久化插件
import piniaPluginPersistedstate from "pinia-plugin-persistedstate";
// 引入 Element Plus 反馈类组件样式（ElMessage 等需要手动导入样式）
import "element-plus/es/components/message/style/css";
import "element-plus/es/components/message-box/style/css";
import "element-plus/es/components/notification/style/css";

const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)

const app = createApp(App);
app.use(router);
app.use(pinia);
app.mount("#app");

