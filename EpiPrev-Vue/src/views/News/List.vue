<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";

import request from "@/utils/request";
const router = useRouter();

const newsList = ref<any[]>([]);

onMounted(async () => {
    const res: any = await request.get("/news/list");
    newsList.value = res.records;
});

const getLevelType = (level: string) => {
  return level === "warning" ? "warning" : "info";
};
</script>

<template>
  <div class="news-page">
    <div class="page-header">
      <h1>疫情动态</h1>
      <p>及时了解最新疫情信息和防控政策</p>
    </div>

    <div class="news-list">
      <div
        v-for="news in newsList"
        :key="news.id"
        class="news-card"
        @click="router.push(`/news/${news.id}`)"
      >
        <div class="news-date">
          <span class="day">{{ news.date.split('-')[2] }}</span>
          <span class="month">{{ news.date.split('-')[1] }}月</span>
        </div>
        <div class="news-content">
          <div class="news-header">
            <el-tag :type="getLevelType(news.level)" size="small">{{ news.source }}</el-tag>
          </div>
          <h3 class="news-title">{{ news.title }}</h3>
          <p class="news-summary">{{ news.summary }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.news-page {
  padding: 20px 0;
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

.news-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.news-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  display: flex;
  gap: 24px;
  cursor: pointer;
  transition: all 0.2s;
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
}

.news-summary {
  color: #606266;
  font-size: 14px;
  line-height: 1.6;
}
</style>
