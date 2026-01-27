<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";

import request from "@/utils/request";

const route = useRoute();
const router = useRouter();

const article = ref<any>({});

onMounted(async () => {
  try {
    const res = await request.get(`/knowledge/${route.params.id}`);
    article.value = res;
  } catch (error) {
    //
  }
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
