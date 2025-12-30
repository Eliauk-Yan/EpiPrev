<script setup lang="ts">
import { ref, computed } from "vue";
import { useUserStore } from "@/stores/user";

const userStore = useUserStore();

const newPostVisible = ref(false);
const newPost = ref({ title: "", content: "" });

const posts = ref([
  {
    id: 1,
    title: "流感疫苗有必要每年打吗？",
    author: "健康小助手",
    date: "2024-01-14",
    replies: 12,
    content: "最近看到很多关于流感疫苗的讨论，想问一下大家流感疫苗是不是需要每年都打？",
    comments: [
      { author: "医学达人", content: "是的，流感病毒变异较快，建议每年接种。", date: "2024-01-14" },
      { author: "疾控专家", content: "尤其是老人、儿童等高风险人群，建议每年接种。", date: "2024-01-14" },
    ],
  },
  {
    id: 2,
    title: "如何正确佩戴口罩？",
    author: "防护先锋",
    date: "2024-01-13",
    replies: 8,
    content: "很多人戴口罩的方式都不对，分享一下正确的佩戴方法。",
    comments: [
      { author: "护士小张", content: "口罩要完全覆盖口鼻，鼻夹要压紧。", date: "2024-01-13" },
    ],
  },
  {
    id: 3,
    title: "孩子得了手足口病怎么护理？",
    author: "宝妈小王",
    date: "2024-01-12",
    replies: 15,
    content: "孩子幼儿园有小朋友得了手足口病，想了解一下如何预防和护理。",
    comments: [],
  },
]);

const selectedPost = ref<typeof posts.value[0] | null>(null);
const newComment = ref("");

const handleSubmitPost = () => {
  if (!newPost.value.title || !newPost.value.content) {
    ElMessage.warning("请填写完整信息");
    return;
  }
  posts.value.unshift({
    id: Date.now(),
    title: newPost.value.title,
    author: userStore.user?.username || "匿名用户",
    date: new Date().toISOString().split("T")[0],
    replies: 0,
    content: newPost.value.content,
    comments: [],
  });
  newPost.value = { title: "", content: "" };
  newPostVisible.value = false;
  ElMessage.success("发布成功");
};

const handleSubmitComment = () => {
  if (!newComment.value || !selectedPost.value) return;
  selectedPost.value.comments.push({
    author: userStore.user?.username || "匿名用户",
    content: newComment.value,
    date: new Date().toISOString().split("T")[0],
  });
  selectedPost.value.replies++;
  newComment.value = "";
  ElMessage.success("评论成功");
};
</script>

<template>
  <div class="forum-page">
    <div class="page-header">
      <h1>互动交流</h1>
      <p>分享经验，解答疑问</p>
    </div>

    <div class="forum-actions">
      <el-button type="primary" @click="newPostVisible = true">
        ✏️ 发布帖子
      </el-button>
    </div>

    <div class="forum-container">
      <div class="post-list">
        <div
          v-for="post in posts"
          :key="post.id"
          :class="['post-card', { active: selectedPost?.id === post.id }]"
          @click="selectedPost = post"
        >
          <h3 class="post-title">{{ post.title }}</h3>
          <div class="post-meta">
            <span>👤 {{ post.author }}</span>
            <span>📅 {{ post.date }}</span>
            <span>💬 {{ post.replies }}</span>
          </div>
        </div>
      </div>

      <div class="post-detail" v-if="selectedPost">
        <div class="detail-header">
          <h2>{{ selectedPost.title }}</h2>
          <div class="post-meta">
            <span>👤 {{ selectedPost.author }}</span>
            <span>📅 {{ selectedPost.date }}</span>
          </div>
        </div>
        <div class="detail-content">
          {{ selectedPost.content }}
        </div>

        <div class="comments-section">
          <h3>评论 ({{ selectedPost.comments.length }})</h3>
          <div class="comment" v-for="(comment, idx) in selectedPost.comments" :key="idx">
            <div class="comment-header">
              <span class="comment-author">{{ comment.author }}</span>
              <span class="comment-date">{{ comment.date }}</span>
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
            <el-button type="primary" size="small" @click="handleSubmitComment">
              发表评论
            </el-button>
          </div>
          <el-alert v-else type="info" :closable="false">
            请先登录后再发表评论
          </el-alert>
        </div>
      </div>
      <div class="post-placeholder" v-else>
        <p>👈 选择一个帖子查看详情</p>
      </div>
    </div>

    <!-- 发帖弹窗 -->
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
  padding: 20px 0;
}

.page-header {
  text-align: center;
  margin-bottom: 24px;
}

.page-header h1 {
  font-size: 28px;
  color: #303133;
  margin-bottom: 8px;
}

.page-header p {
  color: #909399;
}

.forum-actions {
  margin-bottom: 20px;
}

.forum-container {
  display: grid;
  grid-template-columns: 350px 1fr;
  gap: 20px;
  min-height: 500px;
}

.post-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.post-card {
  background: white;
  border-radius: 8px;
  padding: 16px;
  cursor: pointer;
  transition: all 0.2s;
  border: 2px solid transparent;
}

.post-card:hover {
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.post-card.active {
  border-color: #409EFF;
}

.post-title {
  font-size: 15px;
  color: #303133;
  margin-bottom: 8px;
}

.post-meta {
  display: flex;
  gap: 12px;
  font-size: 12px;
  color: #909399;
}

.post-detail,
.post-placeholder {
  background: white;
  border-radius: 12px;
  padding: 24px;
}

.post-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  color: #909399;
}

.detail-header {
  border-bottom: 1px solid #ebeef5;
  padding-bottom: 16px;
  margin-bottom: 16px;
}

.detail-header h2 {
  font-size: 20px;
  margin-bottom: 8px;
}

.detail-content {
  line-height: 1.8;
  color: #606266;
  margin-bottom: 24px;
}

.comments-section h3 {
  font-size: 16px;
  margin-bottom: 16px;
  color: #303133;
}

.comment {
  background: #f5f7fa;
  border-radius: 8px;
  padding: 12px;
  margin-bottom: 12px;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
}

.comment-author {
  font-weight: 500;
  color: #409EFF;
}

.comment-date {
  font-size: 12px;
  color: #909399;
}

.comment-content {
  color: #606266;
  font-size: 14px;
}

.comment-form {
  display: flex;
  flex-direction: column;
  gap: 12px;
  align-items: flex-end;
}

.comment-form .el-input {
  width: 100%;
}
</style>
