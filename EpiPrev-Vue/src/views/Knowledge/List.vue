<script setup lang="ts">
import { ref, watch, onMounted } from "vue";
import { useRouter } from "vue-router";

import request from "@/utils/request";

const router = useRouter();

const categories = ["全部", "呼吸道传染病", "消化道传染病", "血液传染病", "接触传染病"];
const activeCategory = ref("全部");

const articles = ref<any[]>([]);

const fetchArticles = async () => {
  try {
    const res: any = await request.get("/knowledge/list", {
      params: {
        category: activeCategory.value === "全部" ? undefined : activeCategory.value,
      },
    });
    articles.value = res.records;
  } catch (error) {
    // handled
  }
};

watch(activeCategory, () => {
  fetchArticles();
});

onMounted(() => {
  fetchArticles();
});

</script>

<template>
  <div class="knowledge-page">
    <div class="page-header">
      <h1>知识普及</h1>
      <p>了解传染病知识，提高防护意识</p>
    </div>

    <div class="category-tabs">
      <span
        v-for="cat in categories"
        :key="cat"
        :class="['tab', { active: activeCategory === cat }]"
        @click="activeCategory = cat"
      >
        {{ cat }}
      </span>
    </div>

    <div class="article-list">
      <div
        v-for="article in articles"
        :key="article.id"
        class="article-card"
        @click="router.push(`/knowledge/${article.id}`)"
      >
        <div class="article-content">
          <el-tag size="small" type="primary">{{ article.category }}</el-tag>
          <h3 class="article-title">{{ article.title }}</h3>
          <p class="article-summary">{{ article.summary }}</p>
          <div class="article-meta">
            <span>{{ article.date }}</span>
            <span>👁️ {{ article.views }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.knowledge-page {
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

.category-tabs {
  display: flex;
  gap: 12px;
  margin-bottom: 24px;
  flex-wrap: wrap;
}

.tab {
  padding: 8px 20px;
  border-radius: 20px;
  background: white;
  cursor: pointer;
  transition: all 0.2s;
  font-size: 14px;
}

.tab:hover {
  background: #ecf5ff;
}

.tab.active {
  background: #409EFF;
  color: white;
}

.article-list {
  display: grid;
  gap: 20px;
}

.article-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  cursor: pointer;
  transition: all 0.2s;
}

.article-card:hover {
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.article-title {
  font-size: 18px;
  margin: 12px 0;
  color: #303133;
}

.article-summary {
  color: #606266;
  font-size: 14px;
  line-height: 1.6;
  margin-bottom: 12px;
}

.article-meta {
  display: flex;
  gap: 16px;
  color: #909399;
  font-size: 13px;
}
</style>
