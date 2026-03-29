<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import { ArrowLeft } from "@element-plus/icons-vue";
import { type NewsVO } from "@/api/news";

const router = useRouter();

const news = ref<NewsVO | null>(null);
const loading = ref(true);

function formatPara(text: string) {
  return text
    .replace(/^### (.+)$/gm, '<h3>$1</h3>')
    .replace(/^## (.+)$/gm, '<h2>$1</h2>')
    .replace(/\*\*(.+?)\*\*/g, '<strong>$1</strong>')
    .replace(/^- (.+)$/gm, '<li>$1</li>')
    .replace(/^(\d+)\. (.+)$/gm, '<li>$2</li>')
    .replace(/\n/g, '<br>');
}

const fetchDetail = async () => {
  loading.value = true;
  try {
    const state = history.state as { news?: NewsVO };
    if (state.news) {
      news.value = state.news;
      return;
    }
  } catch (error) {
    console.error('加载新闻详情失败:', error);
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchDetail();
});
</script>

<template>
  <div class="detail-page">
    <div class="back-btn" @click="router.back()">
      <el-icon><ArrowLeft /></el-icon>
      返回列表
    </div>

    <div v-if="loading" class="loading-container">
      <div class="loading-spinner"></div>
      <p>正在加载新闻...</p>
    </div>

    <div v-else-if="news" class="article-wrapper">
      <article class="article">
        <header class="article-header">
          <el-tag type="info">{{ news.source }}</el-tag>
          <h1>{{ news.title }}</h1>
          <div class="article-meta">
            <span>📅 {{ news.publishTime ? String(news.publishTime).replace('T', ' ') : '' }}</span>
          </div>
        </header>

        <div class="article-content">
          <p v-for="(para, idx) in (news.content || '').split('\n\n')" :key="idx" v-html="formatPara(para)"></p>
        </div>
      </article>
    </div>

    <el-empty v-else description="新闻不存在或已被删除" :image-size="120">
      <el-button type="primary" @click="router.back()">返回列表</el-button>
    </el-empty>
  </div>
</template>

<style scoped>
.detail-page {
  max-width: 800px;
  margin: 0 auto;
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 300px;
  color: #409EFF;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 3px solid #e0e0e0;
  border-top-color: #409EFF;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.loading-container p {
  margin-top: 16px;
  font-size: 14px;
  color: #909399;
}

.article-wrapper {
  min-height: 200px;
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
