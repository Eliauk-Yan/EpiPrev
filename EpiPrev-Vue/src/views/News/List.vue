<script setup lang="ts">
import { ref } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();

const newsList = ref([
  {
    id: 1,
    title: "全国流感监测周报（2024年第2周）",
    summary: "本周全国流感活动水平持续下降，但仍处于流行期。各地应继续加强监测和防控。",
    date: "2024-01-15",
    source: "国家疾控中心",
    level: "info",
  },
  {
    id: 2,
    title: "关于加强冬春季传染病防控的通知",
    summary: "冬春季是呼吸道传染病高发季节，各地要做好防控准备工作。",
    date: "2024-01-12",
    source: "国家卫健委",
    level: "warning",
  },
  {
    id: 3,
    title: "诺如病毒感染性腹泻防控提示",
    summary: "近期诺如病毒感染性腹泻进入高发期，请注意饮食卫生。",
    date: "2024-01-10",
    source: "地方疾控中心",
    level: "info",
  },
  {
    id: 4,
    title: "手足口病防控知识手册发布",
    summary: "为帮助家长和学校做好手足口病预防，特发布本防控知识手册。",
    date: "2024-01-08",
    source: "国家卫健委",
    level: "info",
  },
]);

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
