<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import { getNewsList } from "@/api/news"; // Import the API function

const router = useRouter();

const newsList = ref<any[]>([]);
const total = ref(0);
const loading = ref(false);
const queryParams = ref({
  page: 1,
  size: 10,
  keyword: "",
});

const loadNews = async () => {
  loading.value = true;
  try {
    const res: any = await getNewsList(queryParams.value);
    newsList.value = res.records;
    total.value = res.total;
  } catch (error) {
    console.error("Failed to load news:", error);
  } finally {
    loading.value = false;
  }
};

const handleSearch = () => {
  queryParams.value.page = 1;
  loadNews();
};

const handleCurrentChange = (page: number) => {
  queryParams.value.page = page;
  loadNews();
};

onMounted(() => {
  loadNews();
});

const getLevelType = (source: string) => {
    // Simple logic to color code sources if needed, or stick to 'info'
    if (source.includes('卫健委') || source.includes('CDC')) return 'primary';
    return 'info';
};
</script>

<template>
  <div class="news-page">
    <div class="page-header">
      <h1>疫情动态</h1>
      <p>及时了解最新疫情信息和防控政策</p>
    </div>

    <!-- Search Section -->
    <div class="search-bar">
      <el-input
        v-model="queryParams.keyword"
        placeholder="搜索新闻标题或内容..."
        class="search-input"
        clearable
        @clear="handleSearch"
        @keyup.enter="handleSearch"
      >
        <template #append>
          <el-button @click="handleSearch">
             <el-icon><Search /></el-icon>
          </el-button>
        </template>
      </el-input>
    </div>

    <div class="news-list" v-loading="loading">
      <div
        v-for="news in newsList"
        :key="news.id"
        class="news-card"
        @click="router.push(`/news/${news.id}`)"
      >
        <div class="news-date">
          <span class="day">{{ news.publishTime ? news.publishTime.substring(8, 10) : '01' }}</span>
          <span class="month">{{ news.publishTime ? news.publishTime.substring(5, 7) : '01' }}月</span>
        </div>
        <div class="news-content">
          <div class="news-header">
            <el-tag :type="getLevelType(news.source)" size="small">{{ news.source }}</el-tag>
          </div>
          <h3 class="news-title">{{ news.title }}</h3>
          <p class="news-summary">{{ news.summary }}</p>
        </div>
      </div>
      <el-empty v-if="!loading && newsList.length === 0" description="暂无相关新闻" />
    </div>
    
    <!-- Pagination -->
    <div class="pagination-container" v-if="total > 0">
      <el-pagination
        background
        layout="prev, pager, next"
        :total="total"
        v-model:current-page="queryParams.page"
        :page-size="queryParams.size"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<style scoped>
.news-page {
  padding: 20px 0;
  max-width: 800px;
  margin: 0 auto;
}

.page-header {
  text-align: center;
  margin-bottom: 32px;
}

.page-header h1 {
  font-size: 28px;
  color: #303133;
  margin-bottom: 8px;
}

.page-header p {
  color: #909399;
}

.search-bar {
  margin-bottom: 24px;
  display: flex;
  justify-content: center;
}

.search-input {
  width: 100%;
  max-width: 500px;
}

.news-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
  min-height: 200px;
}

.news-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  display: flex;
  gap: 24px;
  cursor: pointer;
  transition: all 0.2s;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05); /* Added shadow for better visibility */
}

.news-card:hover {
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  transform: translateX(4px);
}

.news-date {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-width: 60px;
  padding-right: 24px;
  border-right: 2px solid #409EFF;
}

.news-date .day {
  font-size: 28px;
  font-weight: 600;
  color: #409EFF;
}

.news-date .month {
  font-size: 14px;
  color: #909399;
}

.news-content {
  flex: 1;
}

.news-header {
  margin-bottom: 8px;
}

.news-title {
  font-size: 18px;
  color: #303133;
  margin-bottom: 8px;
  font-weight: bold; /* Make title bold */
}

.news-summary {
  color: #606266;
  font-size: 14px;
  line-height: 1.6;
  display: -webkit-box; /* Ellipsis for multiple lines */
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 24px;
}
</style>
