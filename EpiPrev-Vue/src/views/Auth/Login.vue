<script setup lang="ts">
import { reactive, ref } from "vue";
import { useRouter } from "vue-router";
import { useUserStore } from "@/stores/user";
import type { FormInstance, FormRules } from "element-plus";
import { ElMessage } from "element-plus";

const router = useRouter();
const userStore = useUserStore();
const formRef = ref<FormInstance>();
const loading = ref(false);

const form = reactive({
  username: "",
  password: "",
});

const rules: FormRules = {
  username: [{ required: true, message: "请输入用户名", trigger: "blur" }],
  password: [{ required: true, message: "请输入密码", trigger: "blur" }],
};

const handleSubmit = async (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  await formEl.validate((valid) => {
    if (valid) {
      loading.value = true;
      // 模拟登录
      setTimeout(() => {
        userStore.login(
          { id: 1, username: form.username, email: `${form.username}@example.com` },
          "mock-token"
        );
        ElMessage.success("登录成功");
        router.push("/");
        loading.value = false;
      }, 500);
    }
  });
};
</script>

<template>
  <div class="auth-page">
    <div class="auth-card">
      <h2 class="title">用户登录</h2>
      <el-form ref="formRef" :model="form" :rules="rules" label-position="top">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" class="w-full" @click="handleSubmit(formRef)">
            登录
          </el-button>
        </el-form-item>
      </el-form>
      <p class="link">
        还没有账号？<RouterLink to="/register">立即注册</RouterLink>
      </p>
    </div>
  </div>
</template>

<style scoped>
.auth-page {
  min-height: 60vh;
  display: flex;
  align-items: center;
  justify-content: center;
}

.auth-card {
  background: white;
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  width: 100%;
  max-width: 400px;
}

.title {
  text-align: center;
  margin-bottom: 24px;
  color: #303133;
}

.link {
  text-align: center;
  margin-top: 16px;
  color: #909399;
  font-size: 14px;
}

.link a {
  color: #409EFF;
}
</style>
