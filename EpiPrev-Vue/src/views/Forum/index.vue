<script setup lang="ts">
import { ref, computed, onMounted } from "vue";
import { useUserStore } from "@/stores/user";
import { ElMessage } from "element-plus";
import {
  getPostList,
  getHotPosts,
  getPostDetail,
  createPost,
  createComment,
  type PostVO,
  type PostDTO,
  type CommentDTO
} from "@/api/forum";

const userStore = useUserStore();

const newPostVisible = ref(false);
const newPost = ref<PostDTO>({ title: "", content: "" });

const posts = ref<PostVO[]>([]);
const hotPosts = ref<PostVO[]>([]);
const searchKeyword = ref("");

// 根据关键词过滤帖子（前端过滤，也可以传word参数到后端）
const filteredPosts = computed(() => {
  if (!searchKeyword.value.trim()) {
    return posts.value;
  }
  const keyword = searchKeyword.value.toLowerCase();
  return posts.value.filter(
    (post) =>
      post.title?.toLowerCase().includes(keyword) ||
      post.content?.toLowerCase().includes(keyword)
  );
});

const fetchPosts = async () => {
  try {
    const res = await getPostList();
    posts.value = res.data || [];
  } catch (error) {
    console.error("Failed to fetch posts", error);
  }
};

const fetchHotPosts = async () => {
  try {
    const res = await getHotPosts(5);
    hotPosts.value = res || [];
  } catch (error) {
    console.error("Failed to fetch hot posts", error);
  }
};

onMounted(() => {
  fetchPosts();
  fetchHotPosts();
});

const selectedPost = ref<PostVO | null>(null);
const newComment = ref("");

// 查看帖子详情
const viewPostDetail = async (post: PostVO) => {
  try {
    const detail = await getPostDetail(post.id);
    selectedPost.value = detail;
  } catch (error) {
    console.error("Failed to fetch post detail", error);
    // 如果获取详情失败，使用列表数据
    selectedPost.value = post;
  }
};

const handleSubmitPost = async () => {
  if (!newPost.value.title || !newPost.value.content) {
    ElMessage.warning("请填写完整信息");
    return;
  }
  try {
    await createPost(newPost.value);
    newPost.value = { title: "", content: "" };
    newPostVisible.value = false;
    ElMessage.success("发布成功");
    fetchPosts();
    fetchHotPosts();
  } catch (error) {
    console.error("Failed to create post", error);
  }
};

const handleSubmitComment = async () => {
  if (!newComment.value || !selectedPost.value) return;

  const commentData: CommentDTO = {
    postId: selectedPost.value.id,
    content: newComment.value
  };

  try {
    await createComment(commentData);
    // 重新获取帖子详情以刷新评论列表
    const detail = await getPostDetail(selectedPost.value.id);
    selectedPost.value = detail;
    newComment.value = "";
    ElMessage.success("评论成功");
  } catch (error) {
    console.error("Failed to create comment", error);
  }
};

const goBackToList = () => {
  selectedPost.value = null;
};

// 格式化日期
const formatDate = (dateStr: string) => {
  if (!dateStr) return "";
  const date = new Date(dateStr);
  return date.toLocaleDateString("zh-CN", {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit"
  });
};
</script>

<template>
  <div class="forum-page">
    <div class="forum-container">
      
      <!-- Left Column: Actions -->
      <aside class="left-sidebar">
        <div class="action-card">
          <el-button type="primary" class="publish-btn" @click="newPostVisible = true">
            ✏️ 发布帖子
          </el-button>
          <div class="menu-list">
             <div class="menu-item active">🏠 全部帖子</div>
             <div class="menu-item">🔥 热门讨论</div>
             <div class="menu-item">⭐ 我的收藏</div>
          </div>
        </div>
      </aside>

      <!-- Middle Column: Content -->
      <main class="main-content">
        <div class="content-wrapper">
          <!-- Search Box -->
          <div class="search-box">
            <el-input
              v-model="searchKeyword"
              placeholder="搜索帖子标题或内容..."
              prefix-icon="Search"
              clearable
              class="search-input"
            >
              <template #prefix>
                <span class="search-icon">🔍</span>
              </template>
            </el-input>
          </div>

          <!-- Post List View -->
          <div class="post-list" v-if="!selectedPost">
            <div
              v-for="post in filteredPosts"
              :key="post.id"
              class="post-card"
              @click="viewPostDetail(post)"
            >
              <h3 class="post-title">{{ post.title }}</h3>
              <p class="post-preview">{{ post.content?.substring(0, 100) }}...</p>
              <div class="post-meta">
                <span>👤 {{ post.authorName }}</span>
                <span>📅 {{ formatDate(post.createTime) }}</span>
                <span>💬 {{ post.replies || 0 }} 回复</span>
              </div>
            </div>
            <el-empty v-if="filteredPosts.length === 0" :description="searchKeyword ? '未找到匹配的帖子' : '暂无帖子'" />
          </div>

        <!-- Post Detail View -->
        <div class="post-detail" v-else>
          <div class="detail-header">
            <el-button link @click="goBackToList" class="back-btn">
              ← 返回列表
            </el-button>
            <h2>{{ selectedPost.title }}</h2>
            <div class="post-meta">
              <span>👤 {{ selectedPost.authorName }}</span>
              <span>📅 {{ formatDate(selectedPost.createTime) }}</span>
            </div>
          </div>
          <div class="detail-content">
            {{ selectedPost.content }}
          </div>

          <div class="comments-section">
            <h3>评论 ({{ selectedPost.comments ? selectedPost.comments.length : 0 }})</h3>
            <div class="comment" v-for="(comment, idx) in selectedPost.comments" :key="comment.id || idx">
              <div class="comment-header">
                <span class="comment-author">{{ comment.authorName }}</span>
                <span class="comment-date">{{ formatDate(comment.createTime) }}</span>
              </div>
              <p class="comment-content">{{ comment.content }}</p>
            </div>

            <div class="comment-form" v-if="userStore.isLoggedIn">
              <el-input
                v-model="newComment"
                type="textarea"
                :rows="2"
                placeholder="写下你的评论..."
              />
              <div class="form-actions">
                <el-button type="primary" size="small" @click="handleSubmitComment">
                  发表评论
                </el-button>
              </div>
            </div>
            <el-alert v-else type="info" :closable="false" class="mt-4">
              请先登录后再发表评论
            </el-alert>
          </div>
        </div>
        </div>
      </main>

      <!-- Right Column: Hot Posts -->
      <aside class="right-sidebar">
        <div class="hot-card">
          <h3 class="card-title">🔥 热门帖子</h3>
          <div class="hot-list">
            <div 
              v-for="(post, index) in hotPosts" 
              :key="post.id" 
              class="hot-item"
              @click="selectedPost = post"
            >
              <span class="hot-index" :class="{'top-3': index < 3}">{{ index + 1 }}</span>
              <span class="hot-title">{{ post.title }}</span>
            </div>
            <div v-if="hotPosts.length === 0" class="no-data">暂无热门数据</div>
          </div>
        </div>
      </aside>

    </div>

    <!-- Create Post Dialog -->
    <el-dialog v-model="newPostVisible" title="发布帖子" width="500px">
      <el-form label-position="top">
        <el-form-item label="标题">
          <el-input v-model="newPost.title" placeholder="请输入帖子标题" />
        </el-form-item>
        <el-form-item label="内容">
          <el-input v-model="newPost.content" type="textarea" :rows="4" placeholder="请输入帖子内容" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="newPostVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitPost">发布</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.forum-page {
  padding: 30px 20px;
  max-width: 1400px;
  margin: 0 auto;
  min-height: calc(100vh - 120px);
}

.forum-container {
  display: grid;
  grid-template-columns: 240px 1fr 280px;
  gap: 30px;
}

/* Common Card Style */
.action-card,
.post-card,
.post-detail,
.hot-card {
  background: white;
  border-radius: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  transition: all 0.3s ease;
  border: 1px solid transparent;
}

/* Left Sidebar */
.left-sidebar .action-card {
  padding: 24px;
  position: sticky;
  top: 30px;
}

.publish-btn {
  width: 100%;
  margin-bottom: 24px;
  height: 44px;
  font-size: 16px;
  border-radius: 10px;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

.menu-item {
  padding: 14px 16px;
  margin-bottom: 8px;
  cursor: pointer;
  border-radius: 10px;
  color: #606266;
  font-weight: 500;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  gap: 10px;
}

.menu-item:hover, .menu-item.active {
  background-color: #ecf5ff;
  color: #409EFF;
}

/* Main Content */
.main-content {
  min-width: 0;
}

/* Content Wrapper Card */
.content-wrapper {
  background: white;
  border-radius: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  padding: 28px;
  border: 1px solid rgba(0, 0, 0, 0.04);
}

/* Search Box */
.search-box {
  margin-bottom: 24px;
}

.search-input {
  width: 100%;
}

.search-input :deep(.el-input__wrapper) {
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  padding: 12px 16px;
  background: #f8f9fb;
  border: 1px solid transparent;
  transition: all 0.3s ease;
}

.search-input :deep(.el-input__wrapper:hover) {
  background: #fff;
  border-color: #e0e0e0;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.search-input :deep(.el-input__wrapper.is-focus) {
  background: #fff;
  border-color: #409EFF;
  box-shadow: 0 4px 16px rgba(64, 158, 255, 0.15);
}

.search-input :deep(.el-input__inner) {
  font-size: 15px;
}

.search-input :deep(.el-input__inner::placeholder) {
  color: #a0a0a0;
}

.search-icon {
  font-size: 16px;
  margin-right: 4px;
}

.post-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.post-card {
  padding: 24px;
  cursor: pointer;
}

.post-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.1);
}

.post-title {
  font-size: 18px;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 10px;
  line-height: 1.4;
}

.post-preview {
  color: #7f8c8d;
  font-size: 14px;
  margin-bottom: 16px;
  line-height: 1.6;
}

.post-meta {
  display: flex;
  align-items: center;
  gap: 16px;
  font-size: 13px;
  color: #a0a0a0;
}

.post-meta span {
    display: flex;
    align-items: center;
    gap: 4px;
}

.post-detail {
  padding: 32px;
}

.back-btn {
  margin-bottom: 16px;
  padding: 0;
  font-size: 14px;
  color: #909399;
}

.back-btn:hover {
  color: #409EFF;
}

.detail-header {
  border-bottom: 2px solid #f0f0f0;
  padding-bottom: 24px;
  margin-bottom: 24px;
}

.detail-header h2 {
  font-size: 24px;
  color: #2c3e50;
  margin-bottom: 16px;
}

.detail-content {
  font-size: 16px;
  line-height: 1.8;
  color: #2c3e50;
  margin-bottom: 40px;
  min-height: 200px;
}

.comments-section {
  border-top: 2px solid #f0f0f0;
  padding-top: 24px;
}

.comments-section h3 {
    font-size: 18px;
    margin-bottom: 20px;
    color: #2c3e50;
}

.comment {
  padding: 20px;
  background: #f8f9fa;
  border-radius: 12px;
  margin-bottom: 16px;
  transition: all 0.2s;
}

.comment:hover {
    background: #f0f2f5;
}

.comment-header {
    display: flex;
    justify-content: space-between;
    margin-bottom: 10px;
}

.comment-author {
    font-weight: 600;
    color: #409EFF;
}

.comment-date {
    font-size: 12px;
    color: #909399;
}

.comment-content {
    color: #606266;
    font-size: 14px;
    line-height: 1.6;
}

/* Right Sidebar */
.right-sidebar .hot-card {
  padding: 24px;
  position: sticky;
  top: 30px;
}

.card-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 20px;
  color: #2c3e50;
  display: flex;
  align-items: center;
  gap: 8px;
}

.hot-list {
    display: flex;
    flex-direction: column;
}

.hot-item {
  display: flex;
  align-items: center;
  padding: 12px 8px;
  cursor: pointer;
  border-radius: 8px;
  transition: all 0.2s;
}

.hot-item:hover {
  background: #f5f7fa;
}

.hot-item:not(:last-child) {
    border-bottom: 1px solid #f5f5f5;
}

.hot-index {
  width: 24px;
  height: 24px;
  line-height: 24px;
  text-align: center;
  background: #f0f0f0;
  color: #909399;
  border-radius: 6px;
  font-size: 12px;
  font-weight: 600;
  margin-right: 12px;
  flex-shrink: 0;
}

.hot-index.top-3 {
  background: linear-gradient(135deg, #FFD700 0%, #FFA500 100%);
  color: white;
}

/* Custom colors for top 3 */
.hot-item:nth-child(2) .hot-index.top-3 {
    background: linear-gradient(135deg, #C0C0C0 0%, #A8A8A8 100%);
}
.hot-item:nth-child(3) .hot-index.top-3 {
    background: linear-gradient(135deg, #CD7F32 0%, #B87333 100%);
}

.hot-title {
  font-size: 14px;
  color: #2c3e50;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  flex: 1;
}

.form-actions {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
}

.no-data {
  color: #909399;
  text-align: center;
  padding: 20px 0;
  font-size: 13px;
}

/* Responsive */
@media (max-width: 1200px) {
  .forum-container {
    grid-template-columns: 200px 1fr 240px;
    gap: 20px;
  }
}

@media (max-width: 992px) {
  .forum-container {
    grid-template-columns: 1fr;
  }
  
  .left-sidebar, .right-sidebar {
    display: none; /* For simplicity on small screens, or stack them */
  }
}
</style>
