<script setup lang="ts">
import { useRouter } from "vue-router";

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

const latestNews = [
  { id: 1, title: "全国流感监测周报（2024年第2周）", date: "01-15" },
  { id: 2, title: "关于加强冬春季传染病防控的通知", date: "01-12" },
  { id: 3, title: "诺如病毒感染性腹泻防控提示", date: "01-10" },
];

const hotArticles = [
  { id: 1, title: "新冠病毒防护指南", views: 2341 },
  { id: 2, title: "流感季节如何预防", views: 1856 },
  { id: 3, title: "诺如病毒防治知识", views: 1234 },
];
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
</template>

<style scoped>
.home-page {
  padding-bottom: 40px;
}

/* Hero */
.hero {
  background: linear-gradient(135deg, #409EFF 0%, #67C23A 100%);
  border-radius: 16px;
  padding: 60px 40px;
  text-align: center;
  color: white;
  margin-bottom: 40px;
}

.hero h1 {
  font-size: 36px;
  margin-bottom: 12px;
}

.hero p {
  font-size: 18px;
  opacity: 0.9;
  margin-bottom: 24px;
}

.hero-actions {
  display: flex;
  gap: 12px;
  justify-content: center;
}

/* Features */
.features {
  margin-bottom: 40px;
}

.features h2 {
  text-align: center;
  font-size: 24px;
  margin-bottom: 24px;
  color: #303133;
}

.feature-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.feature-card {
  background: white;
  border-radius: 12px;
  padding: 32px 24px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
}

.feature-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.feature-icon {
  font-size: 40px;
  display: block;
  margin-bottom: 16px;
}

.feature-card h3 {
  font-size: 18px;
  margin-bottom: 8px;
  color: #303133;
}

.feature-card p {
  font-size: 14px;
  color: #909399;
  line-height: 1.5;
}

/* Info Section */
.info-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  margin-bottom: 40px;
}

.info-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
}

.info-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.info-header h3 {
  font-size: 16px;
  color: #303133;
}

.info-list {
  list-style: none;
}

.info-list li {
  display: flex;
  justify-content: space-between;
  padding: 12px 0;
  border-bottom: 1px solid #ebeef5;
  cursor: pointer;
  transition: all 0.2s;
}

.info-list li:last-child {
  border-bottom: none;
}

.info-list li:hover {
  color: #409EFF;
}

.info-title {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.info-date,
.info-views {
  font-size: 13px;
  color: #909399;
  margin-left: 12px;
}

/* Stats */
.stats-section {
  background: white;
  border-radius: 12px;
  padding: 40px;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  text-align: center;
}

.stat-item {
  display: flex;
  flex-direction: column;
}

.stat-num {
  font-size: 32px;
  font-weight: 600;
  background: linear-gradient(135deg, #409EFF, #67C23A);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-top: 8px;
}

@media (max-width: 768px) {
  .feature-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .info-grid,
  .stats-section {
    grid-template-columns: 1fr;
  }
}
</style>
