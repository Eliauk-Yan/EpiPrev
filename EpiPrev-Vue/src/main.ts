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
const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)

const app = createApp(App);
app.use(router);
app.use(pinia);
app.mount("#app");
