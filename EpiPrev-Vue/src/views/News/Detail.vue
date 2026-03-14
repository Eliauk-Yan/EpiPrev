<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { ArrowLeft } from "@element-plus/icons-vue";
import { getNewsDetail, type NewsVO } from "@/api/news";

const route = useRoute();
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

onMounted(async () => {
    loading.value = true;
    try {
        const res = await getNewsDetail(route.params.id as string);
        news.value = res ?? null;
    } catch (e) {
        news.value = null;
    } finally {
        loading.value = false;
    }
});
</script>

<template>
  <div class="detail-page">
    <div class="back-btn" @click="router.back()">
      <el-icon><ArrowLeft /></el-icon>
      返回列表
    </div>

    <div v-loading="loading" class="article-wrapper">
      <article v-if="news" class="article">
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
      <el-empty v-else-if="!loading" description="新闻不存在或已被删除" :image-size="120">
        <el-button type="primary" @click="router.back()">返回列表</el-button>
      </el-empty>
    </div>
  </div>
</template>

<style scoped>
.detail-page {
  max-width: 800px;
  margin: 0 auto;
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
