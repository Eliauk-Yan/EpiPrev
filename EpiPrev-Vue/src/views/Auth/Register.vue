<script setup lang="ts">
import { reactive, ref } from "vue";
import { useRouter } from "vue-router";
import type { FormInstance, FormRules } from "element-plus";
import { ElMessage } from "element-plus";
import { register } from "@/api/user";
import illustration from "@/assets/illustration.svg";

const router = useRouter();
const formRef = ref<FormInstance>();
const loading = ref(false);

const form = reactive({
  username: "",
  email: "",
  password: "",
  confirmPassword: "",
});

const validatePass = (_rule: unknown, value: string, callback: (error?: Error) => void) => {
  if (value !== form.password) {
    callback(new Error("两次输入的密码不一致"));
  } else {
    callback();
  }
};

const rules: FormRules = {
  username: [
    { required: true, message: "请输入用户名", trigger: "blur" },
    { min: 3, max: 20, message: "用户名长度为3-20个字符", trigger: "blur" },
  ],
  email: [
    { required: true, message: "请输入邮箱", trigger: "blur" },
    { type: "email", message: "请输入正确的邮箱格式", trigger: "blur" },
  ],
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    { min: 6, message: "密码至少6个字符", trigger: "blur" },
  ],
  confirmPassword: [
    { required: true, message: "请确认密码", trigger: "blur" },
    { validator: validatePass, trigger: "blur" },
  ],
};

const handleSubmit = async (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  await formEl.validate(async (valid) => {
    if (valid) {
      loading.value = true;
      try {
        await register({
            username: form.username,
            password: form.password,
            email: form.email
        });
        ElMessage.success("注册成功，请登录");
        await router.push({
          path: "/login",
          query: {username: form.username},
        });
      } catch (error) {
        // Error handled
      } finally {
        loading.value = false;
      }
    }
  });
};
</script>

<template>
  <div class="auth-container">
    <div class="auth-left">
      <div class="illustration-wrapper">
        <img :src="illustration" alt="Register Illustration" class="illustration" />
        <div class="welcome-text">
          <h1>加入我们</h1>
          <p>注册成为传染病预防宣传系统的一员</p>
        </div>
      </div>
    </div>
    <div class="auth-right">
      <div class="form-wrapper">
        <h2 class="title">创建账号</h2>
        <p class="subtitle">填写以下信息完成注册</p>
        
        <el-form ref="formRef" :model="form" :rules="rules" label-position="top" size="large">
          <el-form-item label="用户名" prop="username">
            <el-input v-model="form.username" placeholder="请输入用户名" />
          </el-form-item>
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="form.email" placeholder="请输入邮箱" />
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password />
          </el-form-item>
          <el-form-item label="确认密码" prop="confirmPassword">
            <el-input v-model="form.confirmPassword" type="password" placeholder="请再次输入密码" show-password />
          </el-form-item>
          
          <el-form-item>
            <el-button type="primary" :loading="loading" class="submit-btn" @click="handleSubmit(formRef)">
              立即注册
            </el-button>
          </el-form-item>
        </el-form>
        
        <div class="form-footer">
          <span class="text-gray">已有账号？</span>
          <RouterLink to="/login" class="link">立即登录</RouterLink>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* ... existing styles ... */
.auth-container {
  display: flex;
  min-height: 100vh;
  /* Seamless continuous gradient to remove segregation */
  background: linear-gradient(to right, #e6f7ff 0%, #f0f9eb 50%, #ffffff 100%);
  position: relative;
}

.auth-left {
  flex: 1;
  /* Removed specific background */
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
  position: relative;
}

.illustration-wrapper {
  text-align: center;
  max-width: 600px;
  z-index: 2;
}

.illustration {
  width: 100%;
  max-width: 500px;
  height: auto;
  margin-bottom: 40px;
  filter: drop-shadow(0 10px 30px rgba(0,0,0,0.1));
  animation: float 6s ease-in-out infinite;
}

.welcome-text h1 {
  font-size: 36px;
  font-weight: 800;
  color: #303133;
  margin-bottom: 16px;
}

.welcome-text p {
  font-size: 18px;
  color: #606266;
}

.auth-right {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
  /* Removed specific background */
}

/* ... existing styles ... */
.form-wrapper {
  width: 100%;
  max-width: 480px; /* Slightly wider for register */
  /* Glass effect matched with Profile page */
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.8);
  box-shadow: 0 8px 32px rgba(31, 38, 135, 0.07);
  border-radius: 24px;
  
  padding: 40px; /* Increased padding */
  position: relative;
  z-index: 2;
}

/* Add a subtle decorative blur element to enhance the 'fade/blur' feel */
.auth-container::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: radial-gradient(circle at 50% 50%, rgba(255, 255, 255, 0) 0%, rgba(255, 255, 255, 0.8) 100%);
  opacity: 0.1;
  pointer-events: none;
  z-index: 1;
}

.title {
  font-size: 32px;
  font-weight: 700;
  color: #303133;
  margin-bottom: 12px;
}

.subtitle {
  font-size: 16px;
  color: #909399;
  margin-bottom: 30px; /* adjusted spacing */
}

.submit-btn {
  width: 100%;
  padding: 22px 0;
  font-size: 16px;
  font-weight: 600;
  margin-top: 10px;
  border-radius: 8px;
  background: linear-gradient(90deg, #409EFF, #3a8ee6);
  border: none;
  transition: all 0.3s;
}

.submit-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(64, 158, 255, 0.3);
}

.form-footer {
  margin-top: 24px;
  text-align: center;
}

.text-gray {
  color: #909399;
}

.link {
  color: #409EFF;
  font-weight: 600;
  margin-left: 8px;
  text-decoration: none;
}

.link:hover {
  text-decoration: underline;
}

@keyframes float {
  0% { transform: translateY(0px); }
  50% { transform: translateY(-20px); }
  100% { transform: translateY(0px); }
}

@media (max-width: 900px) {
  .auth-left {
    display: none;
  }
}
</style>
