<script setup lang="ts">
import {ref, onMounted} from "vue";
import {useRouter} from "vue-router";
import {getNewsList, type NewsVO} from "@/api/news";
import {ArrowRight} from "@element-plus/icons-vue";

const router = useRouter();

const newsList = ref<NewsVO[]>([]);
const total = ref(0);
const loading = ref(false);
const queryParams = ref({
  page: 1,
  size: 12,
});

const loadNews = async () => {
  loading.value = true;
  try {
    const res = await getNewsList({
      page: queryParams.value.page,
      size: queryParams.value.size,
    });
    // 适配后端 MultiResult 分页返回结构
    newsList.value = res.data || [];
    total.value = res.total || 0;
  } catch (error) {
    console.error("Failed to load news:", error);
  } finally {
    loading.value = false;
  }
};

const handleCurrentChange = (page: number) => {
  queryParams.value.page = page;
  loadNews();
  window.scrollTo({top: 0, behavior: 'smooth'});
};

onMounted(() => {
  loadNews();
});

const goToDetail = (news: NewsVO) => {
  // 通过路由 state 传递新闻数据，避免冗余API调用
  router.push({
    name: 'news-detail',
    params: { id: news.id },
    state: { news: JSON.parse(JSON.stringify(news)) } as any,
  });
};

</script>

<template>
  <div class="news-page">
    <div class="page-header-section">
      <div class="header-content">
        <h1 class="main-title">疫情动态</h1>
        <p class="subtitle">实时掌握最新疫情资讯与防控指南</p>
      </div>
      <div class="header-bg-decoration"></div>
    </div>

    <div class="content-container">
      <div v-loading="loading" class="news-list">
        <div
            v-for="(news, index) in newsList"
            :key="news.id"
            class="news-card"
            :style="{ animationDelay: `${index * 0.1}s` }"
            @click="goToDetail(news)"
        >
          <div class="date-badge">
            <span class="month">{{ news.publishTime ? news.publishTime.substring(5, 7) : '01' }}月</span>
            <span class="day">{{ news.publishTime ? news.publishTime.substring(8, 10) : '01' }}</span>
          </div>

          <div class="card-content">
            <div class="card-header">
              <span :class="'source-tag primary'">
                {{ news.source }}
              </span>
              <span class="time-ago">{{ news.publishTime?.split(' ')[0] }}</span>
            </div>

            <h3 class="news-title">{{ news.title }}</h3>
            <p class="news-summary">{{ news.summary }}</p>

            <div class="card-footer">
              <span class="read-more">
                阅读全文 <el-icon><ArrowRight/></el-icon>
              </span>
            </div>
          </div>
        </div>

        <el-empty
            v-if="!loading && newsList.length === 0"
            description="暂无相关新闻"
            image-size="200"
        />
      </div>

      <!-- Pagination -->
      <div v-if="total > 0" class="pagination-section">
        <el-pagination
            v-model:current-page="queryParams.page"
            background
            layout="prev, pager, next, jumper"
            :total="total"
            :page-size="queryParams.size"
            :pager-count="9"
            @current-change="handleCurrentChange"
        />
      </div>
    </div>
  </div>
</template>

<style scoped>
.news-page {
  min-height: 100vh;
  background-color: #f8fafc;
  font-family: 'Inter', 'PingFang SC', sans-serif;
}

/* Header Section */
.page-header-section {
  position: relative;
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  padding: 60px 20px 80px;
  text-align: center;
  color: white;
  overflow: hidden;
  margin-bottom: -40px; /* Overlap effect */
}

.header-bg-decoration {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image: radial-gradient(circle at 20% 30%, rgba(255, 255, 255, 0.1) 0%, transparent 20%),
  radial-gradient(circle at 80% 70%, rgba(255, 255, 255, 0.1) 0%, transparent 20%);
  pointer-events: none;
}

.header-content {
  position: relative;
  z-index: 2;
  max-width: 800px;
  margin: 0 auto;
}

.main-title {
  font-size: 36px;
  font-weight: 800;
  margin-bottom: 12px;
  letter-spacing: -0.5px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.subtitle {
  font-size: 16px;
  color: rgba(255, 255, 255, 0.9);
  margin-bottom: 0;
  font-weight: 300;
}

/* Content Container */
.content-container {
  max-width: 900px;
  margin: 0 auto;
  padding: 0 24px 60px;
  position: relative;
  z-index: 3;
}

.news-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* News Card */
.news-card {
  background: white;
  border-radius: 16px;
  padding: 24px;
  display: flex;
  gap: 24px;
  cursor: pointer;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05), 0 2px 4px -1px rgba(0, 0, 0, 0.03);
  transition: all 0.3s ease;
  border: 1px solid rgba(226, 232, 240, 0.8);
  animation: slideUpFade 0.5s ease-out backwards;
}

.news-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
  border-color: #bfdbfe;
}

.date-badge {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: #eff6ff;
  color: #3b82f6;
  border-radius: 12px;
  width: 70px;
  height: 70px;
  flex-shrink: 0;
}

.date-badge .day {
  font-size: 24px;
  font-weight: 700;
  line-height: 1;
}

.date-badge .month {
  font-size: 12px;
  font-weight: 600;
  text-transform: uppercase;
  margin-bottom: 2px;
}

.card-content {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.source-tag {
  font-size: 12px;
  font-weight: 600;
  padding: 4px 10px;
  border-radius: 20px;
  background: #f1f5f9;
  color: #64748b;
}

.source-tag.primary {
  background: #eff6ff;
  color: #3b82f6;
}

.source-tag.success {
  background: #ecfdf5;
  color: #10b981;
}

.time-ago {
  font-size: 12px;
  color: #94a3b8;
}

.news-title {
  font-size: 18px;
  color: #1e293b;
  margin: 0 0 10px 0;
  font-weight: 700;
  line-height: 1.4;
  transition: color 0.2s;
}

.news-card:hover .news-title {
  color: #2563eb;
}

.news-summary {
  color: #64748b;
  font-size: 14px;
  line-height: 1.6;
  margin: 0 0 16px 0;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
}

.card-footer {
  margin-top: auto;
}

.read-more {
  display: inline-flex;
  align-items: center;
  font-size: 13px;
  font-weight: 600;
  color: #3b82f6;
  gap: 4px;
  opacity: 0;
  transform: translateX(-10px);
  transition: all 0.3s ease;
}

.news-card:hover .read-more {
  opacity: 1;
  transform: translateX(0);
}

/* Pagination */
.pagination-section {
  display: flex;
  justify-content: center;
  margin-top: 40px;
}

:deep(.el-pagination.is-background .el-pager li:not(.is-disabled).is-active) {
  background-color: #3b82f6;
}

:deep(.el-pagination.is-background .el-pager li) {
  background-color: white;
  border: 1px solid #e2e8f0;
}

@keyframes slideUpFade {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* Responsive */
@media (max-width: 640px) {
  .page-header-section {
    padding: 40px 20px 60px;
  }

  .main-title {
    font-size: 28px;
  }

  .news-card {
    flex-direction: column;
    gap: 16px;
    padding: 20px;
  }

  .date-badge {
    width: auto;
    height: auto;
    background: transparent;
    color: #64748b;
    flex-direction: row;
    gap: 6px;
    justify-content: flex-start;
  }

  .date-badge .day, .date-badge .month {
    font-size: 13px;
    font-weight: 500;
  }

  .read-more {
    opacity: 1;
    transform: none;
  }
}
</style>
