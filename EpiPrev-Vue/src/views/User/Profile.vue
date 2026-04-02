<script setup lang="ts">
import { useUserStore } from "@/stores/user";
import { ref, reactive, onMounted, watch } from "vue";
import type { FormInstance, FormRules } from "element-plus";
import { getUserInfo, updateAvatar, updateNickName, realNameAuth } from "@/api/user";
import { ElMessage, ElMessageBox } from "element-plus";
import { useRouter } from "vue-router";
import { Camera, User, CreditCard } from "@element-plus/icons-vue";

const userStore = useUserStore();
const router = useRouter();

const handleLogout = () => {
    ElMessageBox.confirm('确定要退出登录吗?', '提示', {
        type: 'warning'
    }).then(async () => {
        await userStore.logout();
        router.push('/login');
        ElMessage.success('已退出登录');
    });
};

const nicknameInput = ref(userStore.user?.nickName || "");
const nickNameUpdating = ref(false);
const avatarUpdating = ref(false);

watch(() => userStore.user?.nickName, (newVal) => {
  if (newVal) nicknameInput.value = newVal;
});

const handleUpdateNickName = async () => {
  if (!nicknameInput.value || nicknameInput.value === userStore.user?.nickName) return;
  nickNameUpdating.value = true;
  try {
    await updateNickName(nicknameInput.value);
    ElMessage.success("昵称修改成功");
    const res = await getUserInfo();
    if (res) {
      userStore.setUser(res, userStore.token);
    }
  } finally {
    nickNameUpdating.value = false;
  }
};

const handleAvatarClick = () => {
  const input = document.createElement("input");
  input.type = "file";
  input.accept = "image/*";
  input.onchange = async (e: any) => {
    const file = e.target.files[0];
    if (file) {
      if (file.size > 2 * 1024 * 1024) {
        ElMessage.error("头像大小不能超过 2MB");
        return;
      }
      avatarUpdating.value = true;
      try {
        await updateAvatar(file);
        ElMessage.success("头像上传成功");
        const res = await getUserInfo();
        if (res) {
          userStore.setUser(res, userStore.token);
        }
      } catch (err) {
        console.error(err);
      } finally {
        avatarUpdating.value = false;
      }
    }
  };
  input.click();
};

const formatDate = (val: string | undefined) => {
    if (!val) return '尚未记录';
    const date = new Date(val);
    return date.toLocaleString();
};

const getRoleLabel = (role: string | undefined) => {
  const roles: Record<string, string> = {
    'ADMIN': '管理员',
    'USER': '普通用户'
  };
  return role ? (roles[role] || role) : '未知角色';
};

const getStateLabel = (state: string | undefined) => {
  const states: Record<string, string> = {
    'INIT': '正常',
    'AUTH': '已认证',
    'FROZEN': '已冻结'
  };
  return state ? (states[state] || state) : '未知状态';
};

const authDialogVisible = ref(false);
const authFormRef = ref<FormInstance>();
const authLoading = ref(false);

const authForm = reactive({
  realName: "",
  idCard: "",
});

const validateIdCard = (_rule: unknown, value: string, callback: (error?: Error) => void) => {
  const reg = /^[1-9]\d{5}(18|19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])\d{3}[\dXx]$/;
  if (!value) {
    callback(new Error("请输入身份证号"));
  } else if (!reg.test(value)) {
    callback(new Error("请输入正确的身份证号"));
  } else {
    callback();
  }
};

const authRules: FormRules = {
  realName: [
    { required: true, message: "请输入真实姓名", trigger: "blur" },
    { min: 2, max: 20, message: "姓名长度为2-20个字符", trigger: "blur" },
    { pattern: /^[\u4e00-\u9fa5]+$/, message: "姓名只能包含中文", trigger: "blur" },
  ],
  idCard: [
    { required: true, message: "请输入身份证号", trigger: "blur" },
    { validator: validateIdCard, trigger: "blur" },
  ],
};

const openAuthDialog = () => {
  authForm.realName = "";
  authForm.idCard = "";
  authDialogVisible.value = true;
};

const handleAuthSubmit = async (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  await formEl.validate(async (valid) => {
    if (valid) {
      authLoading.value = true;
      try {
        const result = await realNameAuth({
          realName: authForm.realName,
          idCard: authForm.idCard,
        });
        if (result) {
          ElMessage.success("实名认证成功");
          authDialogVisible.value = false;
          const res = await getUserInfo();
          if (res) {
            userStore.setUser(res, userStore.token);
          }
        } else {
          ElMessage.error("实名认证失败，请检查信息是否正确");
        }
      } catch (error) {
      } finally {
        authLoading.value = false;
      }
    }
  });
};

const handleAuthClose = () => {
  authFormRef.value?.resetFields();
  authDialogVisible.value = false;
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
        </div>

        <div class="profile-content">
          <div class="profile-header-section">
            <div class="avatar-wrapper clickable" @click="handleAvatarClick" v-loading="avatarUpdating">
              <el-avatar :size="100" :src="userStore.user?.avatar" class="user-avatar">
                {{ userStore.user?.nickName?.charAt(0).toUpperCase() }}
              </el-avatar>
              <div class="avatar-hover-mask">
                <el-icon><Camera /></el-icon>
                <span>更换头像</span>
              </div>
              <div class="user-status-badge" :class="userStore.user?.state?.toLowerCase()"></div>
            </div>
            <div class="header-info">
              <h3 class="username">{{ userStore.user?.nickName }}</h3>
              <div class="tag-group">
                <el-tag size="small" :type="userStore.user?.role === 'ADMIN' ? 'danger' : 'success'">
                  {{ getRoleLabel(userStore.user?.role) }}
                </el-tag>
                <el-tag v-if="userStore.user?.certification" size="small" type="primary" effect="plain">
                  已实名
                </el-tag>
                <el-button
                  v-else
                  size="small"
                  type="primary"
                  plain
                  class="auth-btn"
                  @click="openAuthDialog"
                >
                  去认证
                </el-button>
              </div>
            </div>
          </div>

          <div class="right-section">
            <div class="info-group full-width">
              <label>个人昵称</label>
              <div class="nickname-edit-wrapper">
                <el-input 
                  v-model="nicknameInput" 
                  placeholder="请输入新昵称"
                  class="nickname-input"
                >
                  <template #append>
                    <el-button 
                      type="primary" 
                      @click="handleUpdateNickName" 
                      :loading="nickNameUpdating"
                      :disabled="!nicknameInput || nicknameInput === userStore.user?.nickName"
                    >
                      修改
                    </el-button>
                  </template>
                </el-input>
              </div>
            </div>

            <div class="info-group">
              <label>手机号码</label>
              <div class="info-value">{{ userStore.user?.telephone || '未绑定' }}</div>
            </div>

            <div class="info-group">
              <label>用户状态</label>
              <div class="info-value">
                <el-tag :type="userStore.user?.state === 'FROZEN' ? 'danger' : 'success'" size="small">
                  {{ getStateLabel(userStore.user?.state) }}
                </el-tag>
              </div>
            </div>

            <div class="info-group">
              <label>注册时间</label>
              <div class="info-value">{{ formatDate(userStore.user?.createTime) }}</div>
            </div>

             <div class="info-group">
               <label>用户ID</label>
               <div class="info-value mono">{{ userStore.user?.id }}</div>
             </div>
          </div>
        </div>

        <div class="card-footer">
          <el-button type="danger" plain class="logout-btn" @click="handleLogout">退出登录</el-button>
        </div>
      </div>

      <el-dialog
        v-model="authDialogVisible"
        title="实名认证"
        width="480px"
        :close-on-click-modal="false"
        class="auth-dialog"
        @close="handleAuthClose"
      >
        <div class="dialog-content">
          <el-alert
            title="安全提示"
            type="info"
            :closable="false"
            show-icon
            class="security-alert"
          >
            <template #default>
              您的信息将被加密存储，仅用于身份验证，我们承诺保护您的隐私安全
            </template>
          </el-alert>

          <el-form
            ref="authFormRef"
            :model="authForm"
            :rules="authRules"
            label-position="top"
            size="large"
            class="auth-form"
          >
            <el-form-item label="真实姓名" prop="realName">
              <el-input
                v-model="authForm.realName"
                placeholder="请输入您的真实姓名"
                :prefix-icon="User"
                maxlength="20"
                show-word-limit
              />
            </el-form-item>
            <el-form-item label="身份证号" prop="idCard">
              <el-input
                v-model="authForm.idCard"
                placeholder="请输入18位身份证号码"
                :prefix-icon="CreditCard"
                maxlength="18"
                show-word-limit
              />
            </el-form-item>
          </el-form>
        </div>

        <template #footer>
          <span class="dialog-footer">
            <el-button @click="handleAuthClose">取消</el-button>
            <el-button type="primary" :loading="authLoading" @click="handleAuthSubmit(authFormRef)">
              {{ authLoading ? '认证中...' : '立即认证' }}
            </el-button>
          </span>
        </template>
      </el-dialog>
    </div>
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

.tag-group {
  display: flex;
  gap: 8px;
  align-items: center;
}

.auth-btn {
  border-radius: 12px;
  font-weight: 600;
  transition: all 0.3s;
}

.auth-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

.avatar-wrapper {
  position: relative;
  cursor: pointer;
}

.avatar-hover-mask {
  position: absolute;
  top: 0;
  left: 0;
  width: 100px;
  height: 100px;
  border-radius: 50%;
  background: rgba(0, 0, 0, 0.4);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: white;
  opacity: 0;
  transition: opacity 0.3s;
  font-size: 12px;
  gap: 4px;
}

.avatar-wrapper:hover .avatar-hover-mask {
  opacity: 1;
}

.user-avatar {
  border: 4px solid white;
  box-shadow: 0 8px 24px rgba(64, 158, 255, 0.25);
  transition: transform 0.3s ease;
}

.username {
  font-size: 22px;
  font-weight: 600;
  margin-bottom: 8px;
  color: var(--text-main);
}

.user-status-badge {
  position: absolute;
  bottom: 5px;
  right: 5px;
  width: 18px;
  height: 18px;
  border-radius: 50%;
  border: 3px solid white;
  background: #909399;
  z-index: 2;
}

.user-status-badge.init {
  background: #67C23A;
  box-shadow: 0 0 10px rgba(103, 194, 58, 0.5);
}

.user-status-badge.auth {
  background: #409EFF;
  box-shadow: 0 0 10px rgba(64, 158, 255, 0.5);
}

.user-status-badge.frozen {
  background: #F56C6C;
  box-shadow: 0 0 10px rgba(245, 108, 108, 0.5);
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

.nickname-edit-wrapper {
  max-width: 400px;
}

:deep(.nickname-input .el-input-group__append) {
  background-color: var(--el-color-primary);
  color: white;
  border-color: var(--el-color-primary);
  padding: 0;
}

:deep(.nickname-input .el-input-group__append button) {
  margin: 0;
  padding: 0 20px;
  height: 100%;
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

.auth-dialog :deep(.el-dialog__header) {
  padding: 20px 24px 0;
  border-bottom: none;
}

.auth-dialog :deep(.el-dialog__title) {
  font-size: 20px;
  font-weight: 700;
  background: linear-gradient(120deg, #409EFF, #67C23A);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
}

.auth-dialog :deep(.el-dialog__body) {
  padding: 20px 24px;
}

.auth-dialog :deep(.el-dialog__footer) {
  padding: 16px 24px 20px;
  border-top: 1px solid rgba(0,0,0,0.05);
}

.dialog-content {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.security-alert {
  border-radius: 12px;
}

.security-alert :deep(.el-alert__content) {
  font-size: 13px;
}

.auth-form {
  margin-top: 8px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.dialog-footer .el-button {
  border-radius: 8px;
  padding: 10px 24px;
}
</style>
