<script setup lang="ts">
import { useUserStore } from "@/stores/user";
import { ref, onMounted } from "vue";
import { getUserInfo, updateUserInfo, type UserUpdateData } from "@/api/user";
import { ElMessage, ElMessageBox } from "element-plus";
import { useRouter } from "vue-router";

const userStore = useUserStore();
const router = useRouter();
const editDialogVisible = ref(false);

const handleLogout = () => {
    ElMessageBox.confirm('确定要退出登录吗?', '提示', {
        type: 'warning'
    }).then(async () => {
        await userStore.logout();
        router.push('/login');
        ElMessage.success('已退出登录');
    });
};
const editForm = ref<UserUpdateData>({});
const updating = ref(false);

const initEdit = () => {
  editForm.value = {
    username: userStore.user?.username,
    email: userStore.user?.email,
    phone: userStore.user?.phone,
    avatar: userStore.user?.avatar
  };
  editDialogVisible.value = true;
};

const handleUpdate = async () => {
  updating.value = true;
  try {
    await updateUserInfo(editForm.value);
    ElMessage.success("修改成功");
    editDialogVisible.value = false;
    // Refresh
    const res = await getUserInfo();
    if (res) {
      userStore.setUser(res, userStore.token);
    }
  } catch (error) {
    // handled
  } finally {
    updating.value = false;
  }
};

const formatDate = (val: string | undefined) => {
    if (!val) return '2024-01-01';
    const date = new Date(val);
    return date.toLocaleDateString();
};

onMounted(async () => {
    try {
        const res = await getUserInfo();
        if (res) {
            userStore.setUser(res, userStore.token);
        }
    } catch(e) {
        console.error(e);
    }
});
</script>

<template>
  <div class="profile-page">
    <div class="profile-bg"></div>
    
    <div class="profile-container">
      <div class="profile-card glass-effect">
        <div class="card-header">
          <h2>个人中心</h2>
          <span class="edit-btn" @click="initEdit">编辑资料</span>
        </div>
        
        <div class="profile-content">
          <div class="profile-header-section">
            <div class="avatar-wrapper">
              <el-avatar :size="100" :src="userStore.user?.avatar" class="user-avatar">
                {{ userStore.user?.username?.charAt(0).toUpperCase() }}
              </el-avatar>
              <div class="user-status-badge"></div>
            </div>
            <div class="header-info">
              <h3 class="username">{{ userStore.user?.username }}</h3>
              <p class="user-role">普通用户</p>
            </div>
          </div>
          
          <div class="right-section">
            <div class="info-group">
              <label>用户名</label>
              <div class="info-value">{{ userStore.user?.username }}</div>
            </div>
            
            <div class="info-group">
              <label>电子邮箱</label>
              <div class="info-value">{{ userStore.user?.email || '未绑定' }}</div>
            </div>

            <div class="info-group">
              <label>手机号码</label>
              <div class="info-value">{{ userStore.user?.phone || '未绑定' }}</div>
            </div>
            
            <div class="info-group">
              <label>注册时间</label>
              <div class="info-value">{{ formatDate(userStore.user?.createTime) }}</div>
            </div>
             
             <div class="info-group full-width">
               <label>用户ID</label>
               <div class="info-value mono">{{ userStore.user?.id }}</div>
             </div>
          </div>
        </div>

        <div class="card-footer">
          <el-button type="danger" plain class="logout-btn" @click="handleLogout">退出登录</el-button>
        </div>
      </div>
    </div>

    <!-- Edit Dialog -->
    <el-dialog v-model="editDialogVisible" title="编辑个人资料" width="500px" custom-class="glass-dialog">
      <el-form :model="editForm" label-width="80px" class="edit-form">
        <el-form-item label="头像链接">
            <el-input v-model="editForm.avatar" placeholder="输入图片URL" />
        </el-form-item>
        <el-form-item label="用户名">
            <el-input v-model="editForm.username" />
        </el-form-item>
        <el-form-item label="手机号">
            <el-input v-model="editForm.phone" />
        </el-form-item>
        <el-form-item label="邮箱">
            <el-input v-model="editForm.email" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
            <el-button @click="editDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="handleUpdate" :loading="updating">保存修改</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.profile-page {
  min-height: calc(100vh - 70px); /* Subtract header height */
  position: relative;
  overflow: hidden;
  padding: 40px 20px;
  display: flex;
  justify-content: center;
  align-items: flex-start;
}

.profile-bg {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 300px;
  background: linear-gradient(135deg, #409EFF 0%, #67C23A 100%);
  mask-image: linear-gradient(to bottom, black 50%, transparent 100%);
  -webkit-mask-image: linear-gradient(to bottom, black 50%, transparent 100%);
  z-index: 0;
  opacity: 0.15;
}

/* Decorative circles */
.profile-page::before {
  content: '';
  position: absolute;
  top: -100px;
  right: -100px;
  width: 500px;
  height: 500px;
  background: radial-gradient(circle, rgba(64,158,255,0.2) 0%, rgba(255,255,255,0) 70%);
  border-radius: 50%;
  z-index: 0;
}

.profile-container {
  width: 100%;
  max-width: 900px;
  position: relative;
  z-index: 1;
  animation: slideUp 0.5s ease-out;
}

.glass-effect {
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.8);
  box-shadow: 0 8px 32px rgba(31, 38, 135, 0.07);
  border-radius: 24px;
}

.profile-card {
  padding: 40px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 40px;
  border-bottom: 1px solid rgba(0,0,0,0.05);
  padding-bottom: 20px;
}

.card-header h2 {
  font-size: 24px;
  font-weight: 700;
  background: linear-gradient(120deg, var(--primary-color), var(--success-color));
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
  margin: 0;
}

.edit-btn {
  font-size: 14px;
  color: var(--primary-color);
  cursor: pointer;
  padding: 8px 16px;
  border-radius: 20px;
  background: rgba(64, 158, 255, 0.1);
  transition: all 0.3s;
}

.edit-btn:hover {
  background: var(--primary-color);
  color: white;
}

.profile-content {
  display: flex;
  flex-direction: column;
  gap: 40px;
}

.profile-header-section {
  display: flex;
  align-items: center;
  gap: 30px;
  padding-bottom: 30px;
  border-bottom: 1px solid rgba(0,0,0,0.05);
}

.header-info {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.left-section {
  /* Keep for backward compatibility or remove if unused */
  display: none; 
}

.avatar-wrapper {
  position: relative;
  margin-bottom: 20px;
}

.user-avatar {
  border: 4px solid white;
  box-shadow: 0 8px 24px rgba(64, 158, 255, 0.25);
  transition: transform 0.3s ease;
}

.user-avatar:hover {
  transform: scale(1.05);
}

.username {
  font-size: 22px;
  font-weight: 600;
  margin-bottom: 8px;
  color: var(--text-main);
}

.user-role {
  color: var(--text-secondary);
  font-size: 14px;
  background: rgba(0,0,0,0.05);
  padding: 4px 12px;
  border-radius: 12px;
}

.right-section {
  flex: 1;
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 30px;
  align-content: start;
}

.info-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.info-group.full-width {
  grid-column: span 2;
}

.info-group label {
  font-size: 13px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  color: var(--text-secondary);
  font-weight: 600;
}

.info-value {
  font-size: 16px;
  color: var(--text-main);
  padding: 12px 16px;
  background: rgba(255, 255, 255, 0.5);
  border-radius: 12px;
  border: 1px solid rgba(0,0,0,0.03);
  transition: all 0.3s;
}

.info-value:hover {
  background: white;
  box-shadow: 0 4px 12px rgba(0,0,0,0.03);
}

.info-value.mono {
  font-family: 'SF Mono', 'Roboto Mono', monospace;
  font-size: 14px;
  color: var(--text-regular);
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@media (max-width: 768px) {
  .profile-content {
    flex-direction: column;
    gap: 30px;
  }
  
  .right-section {
    grid-template-columns: 1fr;
  }
}

:deep(.glass-dialog) {
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border-radius: 16px;
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.6);
}

:deep(.glass-dialog .el-dialog__title) {
  color: var(--text-main);
  font-weight: 600;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.card-footer {
  margin-top: 40px;
  display: flex;
  justify-content: center;
  padding-top: 20px;
  border-top: 1px solid rgba(0,0,0,0.05);
}

.logout-btn {
  width: 100%;
  max-width: 200px;
  border-radius: 12px;
  padding: 12px;
}

.avatar-wrapper {
  position: relative;
}
</style>
