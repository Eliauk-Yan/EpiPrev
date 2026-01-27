<script setup lang="ts">
import { useRouter } from "vue-router";
import { ref, onMounted } from "vue";
import request from "@/utils/request";
import { getArticleList, ArticleType } from "@/api/article";

const router = useRouter();

const features = [
  {
    icon: "📚",
    title: "知识普及",
    desc: "了解传染病的传播途径、症状和预防措施",
    path: "/knowledge",
  },
  {
    icon: "📢",
    title: "疫情动态",
    desc: "获取最新的疫情信息和防控政策",
    path: "/news",
  },
  {
    icon: "💬",
    title: "互动交流",
    desc: "在线问答，与专家和网友交流经验",
    path: "/forum",
  },
  {
    icon: "❤️",
    title: "健康管理",
    desc: "记录健康状况，获取科学锻炼建议",
    path: "/health",
  },
];

const latestNews = ref<any[]>([]);
const hotArticles = ref<any[]>([]);

onMounted(async () => {
  try {
    // 获取最新动态
    const newsRes: any = await request.get("/news/list", {
      params: { size: 3 }
    });
    // 后端返回 data 字段
    const newsData = newsRes.data || newsRes.records || [];
    latestNews.value = newsData.map((item: any) => ({
      id: item.id,
      title: item.title,
      date: item.publishTime ? item.publishTime.substring(5, 10) : "MM-DD"
    }));

    // 获取热门知识 - 使用正确的 article API
    const knowledgeRes = await getArticleList({ 
      type: ArticleType.IMAGE, 
      current: 1, 
      size: 3 
    });
    // 后端 MultiResult 返回 data 字段
    hotArticles.value = (knowledgeRes.data || []).map((item: any) => ({
      id: item.id,
      title: item.title,
      views: item.views || 0
    }));

  } catch (e) {
    console.error("Failed to fetch home data", e);
  }
});
</script>

<template>
  <div class="home-page">
    <!-- Hero Banner -->
    <section class="hero">
      <div class="hero-content">
        <h1>传染病预防宣传系统</h1>
        <p>提高公众健康意识，共建健康中国</p>
        <div class="hero-actions">
          <el-button type="primary" size="large" @click="router.push('/knowledge')">
            开始了解
          </el-button>
          <el-button size="large" @click="router.push('/news')">
            查看动态
          </el-button>
        </div>
      </div>
    </section>

    <!-- 功能模块 -->
    <div class="container">
      <section class="features">
        <h2>核心功能</h2>
        <div class="feature-grid">
          <div
            v-for="feature in features"
            :key="feature.title"
            class="feature-card"
            @click="router.push(feature.path)"
          >
            <span class="feature-icon">{{ feature.icon }}</span>
            <h3>{{ feature.title }}</h3>
            <p>{{ feature.desc }}</p>
          </div>
        </div>
      </section>

      <!-- 信息展示 -->
      <section class="info-section">
        <div class="info-grid">
          <div class="info-card">
            <div class="info-header">
              <h3>📢 最新动态</h3>
              <el-link type="primary" @click="router.push('/news')">更多 &gt;</el-link>
            </div>
            <ul class="info-list">
              <li v-for="news in latestNews" :key="news.id" @click="router.push(`/news/${news.id}`)">
                <span class="info-title">{{ news.title }}</span>
                <span class="info-date">{{ news.date }}</span>
              </li>
            </ul>
          </div>

          <div class="info-card">
            <div class="info-header">
              <h3>🔥 热门知识</h3>
              <el-link type="primary" @click="router.push('/knowledge')">更多 &gt;</el-link>
            </div>
            <ul class="info-list">
              <li v-for="article in hotArticles" :key="article.id" @click="router.push(`/knowledge/${article.id}`)">
                <span class="info-title">{{ article.title }}</span>
                <span class="info-views">👁️ {{ article.views }}</span>
              </li>
            </ul>
          </div>
        </div>
      </section>

      <!-- 统计数据 -->
      <section class="stats-section">
        <div class="stat-item">
          <span class="stat-num">1000+</span>
          <span class="stat-label">知识文章</span>
        </div>
        <div class="stat-item">
          <span class="stat-num">50000+</span>
          <span class="stat-label">服务用户</span>
        </div>
        <div class="stat-item">
          <span class="stat-num">100+</span>
          <span class="stat-label">专家入驻</span>
        </div>
        <div class="stat-item">
          <span class="stat-num">24h</span>
          <span class="stat-label">实时更新</span>
        </div>
      </section>
    </div>
  </div>
</template>

<style scoped>
.home-page {
  padding-bottom: 60px;
  overflow-x: hidden;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

/* Hero Section */
.hero {
  position: relative;
  background: url(../../assets/background.png) no-repeat center center;
  background-size: cover;
  padding: 100px 20px 80px;
  text-align: center;
  color: white;
  margin-bottom: 60px;
  overflow: hidden;
}

.hero::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 60px;
  background: var(--bg-color); /* Match body background */
  clip-path: ellipse(60% 80% at 50% 100%);
}

.hero-content {
  position: relative;
  z-index: 2;
  animation: fadeInDown 0.8s ease-out;
}

.hero h1 {
  font-size: 48px;
  font-weight: 800;
  margin-bottom: 20px;
  letter-spacing: -1px;
  text-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.hero p {
  font-size: 20px;
  opacity: 0.9;
  margin-bottom: 40px;
  max-width: 600px;
  margin-left: auto;
  margin-right: auto;
}

.hero-actions {
  display: flex;
  gap: 20px;
  justify-content: center;
}

/* Features */
.features {
  margin-bottom: 80px;
}

.features h2 {
  text-align: center;
  font-size: 32px;
  font-weight: 700;
  margin-bottom: 40px;
  color: var(--text-main);
}

.feature-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 30px;
}

.feature-card {
  background: white;
  border-radius: 20px;
  padding: 40px 24px;
  text-align: center;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  border: 1px solid rgba(0,0,0,0.03);
  box-shadow: 0 4px 20px rgba(0,0,0,0.03);
}

.feature-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 20px 40px rgba(64, 158, 255, 0.15);
  border-color: rgba(64, 158, 255, 0.2);
}

.feature-icon {
  font-size: 48px;
  display: block;
  margin-bottom: 24px;
  transition: transform 0.3s;
}

.feature-card:hover .feature-icon {
  transform: scale(1.1);
}

.feature-card h3 {
  font-size: 20px;
  margin-bottom: 12px;
  color: var(--text-main);
}

.feature-card p {
  font-size: 14px;
  color: var(--text-secondary);
  line-height: 1.6;
}

/* Info Section */
.info-section {
  margin-bottom: 80px;
}

.info-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 30px;
}

.info-card {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  padding: 30px;
  box-shadow: 0 8px 32px rgba(0,0,0,0.05);
  border: 1px solid rgba(255,255,255,0.6);
  transition: transform 0.3s;
}

.info-card:hover {
  transform: translateY(-5px);
}

.info-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid rgba(0,0,0,0.05);
}

.info-header h3 {
  font-size: 18px;
  font-weight: 700;
  color: var(--text-main);
  display: flex;
  align-items: center;
  gap: 8px;
}

.info-list {
  list-style: none;
}

.info-list li {
  display: flex;
  justify-content: space-between;
  padding: 16px 12px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;
  margin-bottom: 4px;
}

.info-list li:hover {
  background: rgba(64, 158, 255, 0.05);
  color: var(--primary-color);
  transform: translateX(5px);
}

.info-title {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  font-weight: 500;
}

.info-date,
.info-views {
  font-size: 13px;
  color: var(--text-secondary);
  margin-left: 16px;
}

/* Stats */
.stats-section {
  background: linear-gradient(120deg, white, #f8fbfd);
  border-radius: 24px;
  padding: 60px;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  text-align: center;
  box-shadow: 0 10px 40px rgba(0,0,0,0.05);
}

.stat-item {
  display: flex;
  flex-direction: column;
  position: relative;
}

.stat-item:not(:last-child)::after {
  content: '';
  position: absolute;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
  height: 40px;
  width: 1px;
  background: rgba(0,0,0,0.05);
}

.stat-num {
  font-size: 42px;
  font-weight: 800;
  background: linear-gradient(135deg, #409EFF, #67C23A);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 15px;
  font-weight: 600;
  color: var(--text-secondary);
  text-transform: uppercase;
  letter-spacing: 1px;
}

/* Animations */
@keyframes fadeInDown {
  from {
    opacity: 0;
    transform: translateY(-30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@media (max-width: 1024px) {
  .feature-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .hero h1 {
    font-size: 32px;
  }
  
  .info-grid,
  .stats-section {
    grid-template-columns: 1fr;
    padding: 30px;
  }
  
  .stat-item:not(:last-child)::after {
    display: none;
  }
  
  .stat-item {
    margin-bottom: 30px;
  }
}
</style>
