<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import {
    getRecommendedArticles,
    getHotVideos,
    getArticleRanking,
    type ArticleVO
} from "@/api/article";

const router = useRouter();

// 推荐图文数据
const recommendedArticles = ref<ArticleVO[]>([]);
// 热门视频数据
const hotVideos = ref<ArticleVO[]>([]);
// 排行榜数据
const rankingList = ref<ArticleVO[]>([]);

// 分页相关
const loading = ref(false);
const currentPage = ref(1);
const pageSize = ref(6);
const total = ref(0);

const mapCover = (url: string | null) => {
    return url || 'https://images.unsplash.com/photo-1576091160399-112ba8d25d1d?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60';
}

const mapVideoCover = (url: string | null) => {
    return url || 'https://images.unsplash.com/photo-1576091160550-2187d80a1b95?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60';
}

// 获取推荐图文（分页）
const fetchRecommended = async () => {
    loading.value = true;
    try {
        const res = await getRecommendedArticles(currentPage.value, pageSize.value);
        recommendedArticles.value = res?.data || [];
        total.value = res?.total || 0;
    } catch (error) {
        console.error(error);
    } finally {
        loading.value = false;
    }
}

// 获取热门视频（前4个）
const fetchHotVideos = async () => {
    try {
        const res = await getHotVideos(4);
        hotVideos.value = res?.data || [];
    } catch (error) {
        console.error(error);
    }
}

// 获取阅读排行榜（前10）
const fetchRanking = async () => {
    try {
        const res = await getArticleRanking(10);
        // 按浏览量排序
        rankingList.value = (res?.data || [])
            .sort((a: ArticleVO, b: ArticleVO) => (b.views || 0) - (a.views || 0))
            .slice(0, 10);
    } catch (error) {
        console.error(error);
    }
}

// 页码变化处理
const handlePageChange = (page: number) => {
    currentPage.value = page;
    fetchRecommended();
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

onMounted(() => {
    fetchRecommended();
    fetchHotVideos();
    fetchRanking();
});
</script>


<template>
  <div class="knowledge-page">
    <div class="page-layout">
      <!-- 左侧：推荐图文 + 分页器 -->
      <div class="left-section">
        <div class="section-header">
          <span class="section-icon">📖</span>
          <span class="section-label">推荐图文</span>
        </div>

        <div class="article-grid" v-loading="loading">
          <div
            v-for="item in recommendedArticles"
            :key="item.id"
            class="article-card"
            @click="router.push(`/knowledge/${item.id}`)"
          >
            <div class="article-image" :style="{ backgroundImage: `url(${mapCover(item.cover)})` }">
              <div class="article-category">{{ item.category || '健康知识' }}</div>
            </div>
            <div class="article-body">
              <h3 class="article-title">{{ item.title }}</h3>
              <p class="article-summary">{{ item.summary }}</p>
              <div class="article-footer">
                <span class="article-date">{{ item.createTime ? item.createTime.split('T')[0] : '' }}</span>
                <span class="article-views">
                  <i class="view-icon">👁️</i>
                  {{ item.views || 0 }}
                </span>
              </div>
            </div>
          </div>
          <el-empty v-if="recommendedArticles.length === 0 && !loading" description="暂无推荐文章" :image-size="80" />
        </div>

        <!-- 分页器 -->
        <div class="pagination-wrapper" v-if="total > 0">
          <el-pagination
            v-model:current-page="currentPage"
            :page-size="pageSize"
            :total="total"
            layout="prev, pager, next"
            background
            @current-change="handlePageChange"
          />
        </div>
      </div>

      <!-- 右侧：热门视频 + 排行榜 -->
      <div class="right-section">
        <!-- 热门视频 -->
        <div class="sidebar-block">
          <div class="section-header">
            <span class="section-icon">🎬</span>
            <span class="section-label">热门视频</span>
          </div>
          <div class="video-list">
            <div
              v-for="item in hotVideos"
              :key="item.id"
              class="video-card"
              @click="router.push(`/knowledge/${item.id}`)"
            >
              <div class="video-cover" :style="{ backgroundImage: `url(${mapVideoCover(item.cover)})` }">
                <div class="play-button">
                  <span>▶</span>
                </div>
                <div class="video-duration">{{ item.views || 0 }} 播放</div>
              </div>
              <div class="video-title">{{ item.title }}</div>
            </div>
          </div>
          <el-empty v-if="hotVideos.length === 0" description="暂无视频" :image-size="60" />
        </div>

        <!-- 阅读排行榜 -->
        <div class="sidebar-block">
          <div class="section-header">
            <span class="section-icon">🏆</span>
            <span class="section-label">阅读排行</span>
          </div>
          <div class="ranking-list">
            <div
              v-for="(item, index) in rankingList"
              :key="item.id"
              class="ranking-item"
              @click="router.push(`/knowledge/${item.id}`)"
            >
              <span :class="['rank-number', `rank-${index + 1}`]">{{ index + 1 }}</span>
              <span class="rank-title">{{ item.title }}</span>
              <span class="rank-views">{{ item.views || 0 }}</span>
            </div>
          </div>
          <el-empty v-if="rankingList.length === 0" description="暂无数据" :image-size="60" />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.knowledge-page {
  padding: 30px 20px;
  max-width: 1400px;
  margin: 0 auto;
  min-height: calc(100vh - 120px);
}

/* 双栏布局 */
.page-layout {
  display: grid;
  grid-template-columns: 1fr 380px;
  gap: 30px;
}

/* 左侧区域 */
.left-section {
  background: white;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}

/* 右侧区域 */
.right-section {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

/* 区块标题 */
.section-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 2px solid #f0f0f0;
}

.section-icon {
  font-size: 22px;
}

.section-label {
  font-size: 18px;
  font-weight: 600;
  color: #2c3e50;
}

/* 文章网格 */
.article-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  min-height: 300px;
}

/* 空状态居中 */
.article-grid :deep(.el-empty) {
  grid-column: 1 / -1;
  display: flex;
  justify-content: center;
  align-items: center;
}

.article-card {
  background: #fafbfc;
  border-radius: 12px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid #eef0f2;
}

.article-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.1);
  border-color: transparent;
}

.article-image {
  height: 160px;
  background-size: cover;
  background-position: center;
  position: relative;
}

.article-category {
  position: absolute;
  top: 12px;
  left: 12px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
}

.article-body {
  padding: 16px;
}

.article-title {
  font-size: 16px;
  font-weight: 600;
  color: #2c3e50;
  margin: 0 0 8px 0;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.article-summary {
  font-size: 13px;
  color: #7f8c8d;
  margin: 0 0 12px 0;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.article-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 12px;
  color: #a0a0a0;
}

.article-views {
  display: flex;
  align-items: center;
  gap: 4px;
}

.view-icon {
  font-style: normal;
}

/* 分页器 */
.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 24px;
  padding-top: 20px;
  border-top: 1px solid #f0f0f0;
}

/* 侧边栏区块 */
.sidebar-block {
  background: white;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}

/* 视频列表 */
.video-list {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}

.video-card {
  cursor: pointer;
  transition: all 0.3s;
  border-radius: 10px;
  overflow: hidden;
}

.video-card:hover {
  transform: scale(1.02);
}

.video-cover {
  height: 90px;
  background-size: cover;
  background-position: center;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.play-button {
  width: 32px;
  height: 32px;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  color: #333;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
  transition: transform 0.2s;
}

.video-card:hover .play-button {
  transform: scale(1.1);
}

.video-duration {
  position: absolute;
  bottom: 6px;
  right: 6px;
  background: rgba(0, 0, 0, 0.7);
  color: white;
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 10px;
}

.video-title {
  padding: 8px;
  font-size: 12px;
  color: #333;
  background: #f8f9fa;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 排行榜 */
.ranking-list {
  display: flex;
  flex-direction: column;
}

.ranking-item {
  display: flex;
  align-items: center;
  padding: 12px 8px;
  cursor: pointer;
  border-radius: 8px;
  transition: all 0.2s;
}

.ranking-item:hover {
  background: #f5f7fa;
}

.ranking-item:not(:last-child) {
  border-bottom: 1px solid #f5f5f5;
}

.rank-number {
  width: 24px;
  height: 24px;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 600;
  margin-right: 12px;
  background: #f0f0f0;
  color: #666;
}

.rank-1 {
  background: linear-gradient(135deg, #FFD700 0%, #FFA500 100%);
  color: white;
}

.rank-2 {
  background: linear-gradient(135deg, #C0C0C0 0%, #A8A8A8 100%);
  color: white;
}

.rank-3 {
  background: linear-gradient(135deg, #CD7F32 0%, #B87333 100%);
  color: white;
}

.rank-title {
  flex: 1;
  font-size: 13px;
  color: #333;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.rank-views {
  font-size: 12px;
  color: #999;
  margin-left: 8px;
}

.rank-views::after {
  content: ' 阅读';
}

/* 响应式适配 */
@media (max-width: 1024px) {
  .page-layout {
    grid-template-columns: 1fr;
  }

  .right-section {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .article-grid {
    grid-template-columns: 1fr;
  }

  .right-section {
    grid-template-columns: 1fr;
  }

  .video-list {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>
