<script setup lang="ts">
import { ref } from "vue";
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
const router = useRouter();

const article = ref({
  id: Number(route.params.id),
  title: "新冠病毒防护指南",
  category: "呼吸道传染病",
  date: "2024-01-15",
  views: 2341,
  content: `
## 什么是新冠病毒

新型冠状病毒（COVID-19）是一种主要通过呼吸道飞沫和密切接触传播的病毒。

## 传播途径

1. **飞沫传播**：感染者咳嗽、打喷嚏时产生的飞沫
2. **接触传播**：接触被病毒污染的物体表面后触摸口鼻眼
3. **气溶胶传播**：在密闭空间内长时间暴露

## 预防措施

### 日常防护
- 正确佩戴口罩
- 勤洗手，使用肥皂或洗手液
- 保持社交距离
- 避免前往人群密集场所

### 居家防护
- 定期开窗通风
- 对常接触物品进行消毒
- 保持良好的个人卫生习惯

## 出现症状怎么办

如出现发热、咳嗽、乏力等症状，应：
1. 及时佩戴口罩
2. 避免乘坐公共交通
3. 前往发热门诊就诊
4. 如实告知医生旅居史和接触史
  `,
});
</script>

<template>
  <div class="detail-page">
    <div class="back-btn" @click="router.back()">
      <el-icon><ArrowLeft /></el-icon>
      返回列表
    </div>

    <article class="article">
      <header class="article-header">
        <el-tag type="primary">{{ article.category }}</el-tag>
        <h1>{{ article.title }}</h1>
        <div class="article-meta">
          <span>📅 {{ article.date }}</span>
          <span>👁️ {{ article.views }} 次阅读</span>
        </div>
      </header>

      <div class="article-content" v-html="renderMarkdown(article.content)"></div>
    </article>
  </div>
</template>

<script lang="ts">
import { ArrowLeft } from "@element-plus/icons-vue";

// 简单的 Markdown 渲染
function renderMarkdown(text: string) {
  return text
    .replace(/^### (.+)$/gm, '<h3>$1</h3>')
    .replace(/^## (.+)$/gm, '<h2>$1</h2>')
    .replace(/^# (.+)$/gm, '<h1>$1</h1>')
    .replace(/\*\*(.+?)\*\*/g, '<strong>$1</strong>')
    .replace(/^- (.+)$/gm, '<li>$1</li>')
    .replace(/^(\d+)\. (.+)$/gm, '<li>$2</li>')
    .replace(/\n\n/g, '</p><p>')
    .replace(/\n/g, '<br>');
}

export default {
  components: { ArrowLeft },
};
</script>

<style scoped>
.detail-page {
  max-width: 800px;
  margin: 0 auto;
}

.back-btn {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  color: #409EFF;
  cursor: pointer;
  margin-bottom: 20px;
}

.article {
  background: white;
  border-radius: 12px;
  padding: 40px;
}

.article-header {
  text-align: center;
  margin-bottom: 32px;
  padding-bottom: 24px;
  border-bottom: 1px solid #ebeef5;
}

.article-header h1 {
  font-size: 28px;
  margin: 16px 0;
  color: #303133;
}

.article-meta {
  display: flex;
  justify-content: center;
  gap: 24px;
  color: #909399;
  font-size: 14px;
}

.article-content {
  line-height: 1.8;
  color: #606266;
}

.article-content :deep(h2) {
  font-size: 20px;
  margin: 24px 0 12px;
  color: #303133;
}

.article-content :deep(h3) {
  font-size: 16px;
  margin: 16px 0 8px;
  color: #303133;
}

.article-content :deep(li) {
  margin-left: 20px;
  margin-bottom: 8px;
}

.article-content :deep(strong) {
  color: #303133;
}
</style>
