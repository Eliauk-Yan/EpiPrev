<script setup lang="ts">
import { ref, onMounted, computed } from "vue";
import { useRoute, useRouter } from "vue-router";
import { ArrowLeft, View, Calendar, Folder } from "@element-plus/icons-vue";
import { getArticleDetail, ArticleType, type ArticleVO } from "@/api/article";

const route = useRoute();
const router = useRouter();

const article = ref<ArticleVO | null>(null);
const loading = ref(true);

// 判断是否为视频类型
const isVideo = computed(() => article.value?.type === ArticleType.VIDEO);

// 格式化日期
const formatDate = (dateStr: string | undefined) => {
  if (!dateStr) return '';
  const date = new Date(dateStr);
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  });
};

// 简单的 Markdown 渲染
const renderMarkdown = (text: string | undefined) => {
  if (!text) return '';
  return text
    .replace(/^### (.+)$/gm, '<h3>$1</h3>')
    .replace(/^## (.+)$/gm, '<h2>$1</h2>')
    .replace(/^# (.+)$/gm, '<h1>$1</h1>')
    .replace(/\*\*(.+?)\*\*/g, '<strong>$1</strong>')
    .replace(/\*(.+?)\*/g, '<em>$1</em>')
    .replace(/`(.+?)`/g, '<code>$1</code>')
    .replace(/^- (.+)$/gm, '<li>$1</li>')
    .replace(/^(\d+)\. (.+)$/gm, '<li>$2</li>')
    .replace(/\n\n/g, '</p><p>')
    .replace(/\n/g, '<br>');
};

// 加载文章详情
const fetchDetail = async () => {
  loading.value = true;
  try {
    const id = route.params.id as string;
    article.value = await getArticleDetail(id);
  } catch (error) {
    console.error('加载文章详情失败:', error);
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
    <!-- 加载状态 -->
    <div v-if="loading" class="loading-container">
      <div class="loading-spinner"></div>
      <p>正在加载文章...</p>
    </div>

    <!-- 文章内容 -->
    <div v-else-if="article" class="article-wrapper">
      <!-- 顶部横幅区域 -->
      <header class="article-hero" :style="article.cover ? { backgroundImage: `linear-gradient(to bottom, rgba(0,0,0,0.3), rgba(0,0,0,0.7)), url(${article.cover})` } : {}">
        <!-- 返回按钮 -->
        <button class="back-btn" @click="router.back()">
          <el-icon><ArrowLeft /></el-icon>
          <span>返回列表</span>
        </button>

        <div class="hero-content">
          <!-- 分类标签 -->
          <div class="category-tag">
            <el-icon><Folder /></el-icon>
            <span>{{ article.category || '健康知识' }}</span>
          </div>

          <!-- 标题 -->
          <h1 class="article-title">{{ article.title }}</h1>

          <!-- 摘要 -->
          <p class="article-summary" v-if="article.summary">{{ article.summary }}</p>

          <!-- 元信息 -->
          <div class="article-meta">
            <div class="meta-item">
              <el-icon><Calendar /></el-icon>
              <span>{{ formatDate(article.createTime) }}</span>
            </div>
            <div class="meta-divider">|</div>
            <div class="meta-item">
              <el-icon><View /></el-icon>
              <span>{{ article.views || 0 }} 次阅读</span>
            </div>
          </div>
        </div>
      </header>

      <!-- 主内容区域 -->
      <main class="article-main">
        <!-- 视频播放器 -->
        <div v-if="isVideo && article.videoUrl" class="video-section">
          <video 
            :src="article.videoUrl" 
            controls 
            :poster="article.cover"
            class="video-player"
          >
            您的浏览器不支持视频播放
          </video>
        </div>

        <!-- 文章正文 -->
        <article class="article-content" v-html="renderMarkdown(article.content)"></article>

        <!-- 分隔线和底部信息 -->
        <footer class="article-footer">
          <div class="footer-divider">
            <span class="divider-line"></span>
            <span class="divider-icon">📖</span>
            <span class="divider-line"></span>
          </div>

          <div class="footer-info">
            <span class="update-time" v-if="article.updateTime">
              最后更新于 {{ formatDate(article.updateTime) }}
            </span>
          </div>

          <button class="back-to-list" @click="router.back()">
            <el-icon><ArrowLeft /></el-icon>
            <span>返回文章列表</span>
          </button>
        </footer>
      </main>
    </div>

    <!-- 空状态 -->
    <div v-else class="empty-container">
      <el-empty description="文章不存在或已被删除">
        <el-button type="primary" @click="router.back()">返回列表</el-button>
      </el-empty>
    </div>
  </div>
</template>

<style scoped>
.detail-page {
  min-height: calc(100vh - 64px);
  background: #f8fafc;
}

/* 加载状态 */
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: calc(100vh - 64px);
  color: #667eea;
}

.loading-spinner {
  width: 48px;
  height: 48px;
  border: 4px solid #e0e0e0;
  border-top-color: #667eea;
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

/* 文章包装器 */
.article-wrapper {
  width: 100%;
}

/* 顶部横幅区域 */
.article-hero {
  width: 100%;
  min-height: 400px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  background-size: cover;
  background-position: center;
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  padding: 40px;
  position: relative;
}

.back-btn {
  position: absolute;
  top: 24px;
  left: 40px;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 30px;
  color: white;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.back-btn:hover {
  background: rgba(255, 255, 255, 0.25);
  transform: translateX(-4px);
}

.hero-content {
  max-width: 900px;
  color: white;
}

/* 分类标签 */
.category-tag {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 8px 18px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 25px;
  font-size: 13px;
  font-weight: 500;
  backdrop-filter: blur(10px);
  margin-bottom: 20px;
}

/* 标题 */
.article-title {
  font-size: 42px;
  font-weight: 700;
  margin: 0 0 20px 0;
  line-height: 1.3;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
}

/* 摘要 */
.article-summary {
  font-size: 18px;
  opacity: 0.9;
  margin: 0 0 24px 0;
  line-height: 1.7;
  max-width: 700px;
}

/* 元信息 */
.article-meta {
  display: flex;
  align-items: center;
  gap: 16px;
  font-size: 14px;
  opacity: 0.85;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 6px;
}

.meta-divider {
  opacity: 0.5;
}

/* 主内容区域 */
.article-main {
  max-width: 900px;
  margin: 0 auto;
  padding: 60px 40px 80px;
}

/* 视频区域 */
.video-section {
  margin: -100px 0 50px 0;
  position: relative;
  z-index: 10;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.2);
}

.video-player {
  width: 100%;
  display: block;
  background: #000;
}

/* 文章正文 */
.article-content {
  font-size: 17px;
  line-height: 2;
  color: #374151;
}

.article-content :deep(h1) {
  font-size: 32px;
  color: #1f2937;
  margin: 48px 0 24px;
  padding-bottom: 16px;
  border-bottom: 3px solid #667eea;
  font-weight: 700;
}

.article-content :deep(h2) {
  font-size: 26px;
  color: #1f2937;
  margin: 40px 0 20px;
  padding-left: 16px;
  border-left: 5px solid #667eea;
  font-weight: 600;
}

.article-content :deep(h3) {
  font-size: 22px;
  color: #1f2937;
  margin: 32px 0 16px;
  font-weight: 600;
}

.article-content :deep(p) {
  margin: 20px 0;
}

.article-content :deep(strong) {
  color: #1f2937;
  font-weight: 600;
}

.article-content :deep(em) {
  color: #667eea;
  font-style: italic;
}

.article-content :deep(code) {
  background: #f3f4f6;
  padding: 3px 10px;
  border-radius: 6px;
  color: #e83e8c;
  font-family: 'Fira Code', 'Consolas', monospace;
  font-size: 15px;
}

.article-content :deep(li) {
  margin-left: 28px;
  margin-bottom: 12px;
  position: relative;
  padding-left: 8px;
}

.article-content :deep(li)::before {
  content: '•';
  color: #667eea;
  font-weight: bold;
  font-size: 20px;
  position: absolute;
  left: -20px;
  top: -2px;
}

.article-content :deep(img) {
  max-width: 100%;
  border-radius: 12px;
  margin: 32px 0;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
}

/* 文章底部 */
.article-footer {
  margin-top: 80px;
  text-align: center;
}

.footer-divider {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 20px;
  margin-bottom: 32px;
}

.divider-line {
  width: 100px;
  height: 2px;
  background: linear-gradient(90deg, transparent, #e5e7eb, transparent);
}

.divider-icon {
  font-size: 28px;
}

.footer-info {
  margin-bottom: 32px;
}

.update-time {
  color: #9ca3af;
  font-size: 14px;
}

.back-to-list {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 14px 32px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  border-radius: 30px;
  color: white;
  font-size: 15px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.35);
}

.back-to-list:hover {
  transform: translateY(-3px);
  box-shadow: 0 12px 35px rgba(102, 126, 234, 0.45);
}

/* 空状态 */
.empty-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 64px);
}

/* 响应式适配 */
@media (max-width: 768px) {
  .article-hero {
    min-height: 300px;
    padding: 24px;
  }

  .back-btn {
    top: 16px;
    left: 24px;
    padding: 8px 16px;
    font-size: 13px;
  }

  .article-title {
    font-size: 28px;
  }

  .article-summary {
    font-size: 15px;
  }

  .article-meta {
    flex-wrap: wrap;
    gap: 12px;
  }

  .meta-divider {
    display: none;
  }

  .article-main {
    padding: 40px 20px 60px;
  }

  .article-content {
    font-size: 16px;
  }

  .article-content :deep(h1) {
    font-size: 26px;
  }

  .article-content :deep(h2) {
    font-size: 22px;
  }

  .article-content :deep(h3) {
    font-size: 18px;
  }
}
</style>
