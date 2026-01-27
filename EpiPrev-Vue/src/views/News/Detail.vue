<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { ArrowLeft } from "@element-plus/icons-vue";

import request from "@/utils/request";

const route = useRoute();
const router = useRouter();

const news = ref<any>({});

onMounted(async () => {
    const res = await request.get(`/news/${route.params.id}`);
    news.value = res;
    // Handle split for content if needed or assume backend sends full string. 
    // Backend creates NewsInfo with one 'content' string. 
    // If backend data needs formatting, Handle it here.
    // Assuming backend returns proper string for now.
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
        <el-tag type="info">{{ news.source }}</el-tag>
        <h1>{{ news.title }}</h1>
        <div class="article-meta">
          <span>📅 {{ news.publishTime ? news.publishTime.replace('T', ' ') : '' }}</span>
        </div>
      </header>

      <div class="article-content">
        <p v-for="(para, idx) in (news.content || '').split('\n\n')" :key="idx" v-html="formatPara(para)"></p>
      </div>
    </article>
  </div>
</template>

<script lang="ts">
function formatPara(text: string) {
  return text
    .replace(/^### (.+)$/gm, '<h3>$1</h3>')
    .replace(/^## (.+)$/gm, '<h2>$1</h2>')
    .replace(/\*\*(.+?)\*\*/g, '<strong>$1</strong>')
    .replace(/^- (.+)$/gm, '<li>$1</li>')
    .replace(/^(\d+)\. (.+)$/gm, '<li>$2</li>')
    .replace(/\n/g, '<br>');
}

export default {};
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
  font-size: 24px;
  margin: 16px 0;
  color: #303133;
}

.article-meta {
  color: #909399;
  font-size: 14px;
}

.article-content {
  line-height: 1.8;
  color: #606266;
}

.article-content :deep(h2) {
  font-size: 18px;
  margin: 20px 0 12px;
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
