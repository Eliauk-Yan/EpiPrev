<script setup lang="ts">
import { ref, computed, onMounted } from "vue";
import { useUserStore } from "@/stores/user";
import { ElMessage } from "element-plus";

const userStore = useUserStore();

import request from "@/utils/request";

// 健康记录
const healthRecords = ref<any[]>([]);

const newRecord = ref({
  weight: "",
  sleep: "",
  mood: "😊",
});

const moods = ["😊", "😐", "😔", "😴", "💪"];

// 锻炼建议
const exercises = [
  {
    name: "晨间拉伸",
    duration: "10分钟",
    difficulty: "简单",
    description: "早起后进行简单的全身拉伸，唤醒身体。",
    icon: "🧘",
  },
  {
    name: "快走",
    duration: "30分钟",
    difficulty: "简单",
    description: "每天快走30分钟，有助于增强心肺功能。",
    icon: "🚶",
  },
  {
    name: "跳绳",
    duration: "15分钟",
    difficulty: "中等",
    description: "跳绳是高效的有氧运动，能有效提高免疫力。",
    icon: "🤾",
  },
  {
    name: "深蹲",
    duration: "10分钟",
    difficulty: "中等",
    description: "每组20个，做3组，锻炼下肢力量。",
    icon: "🏋️",
  },
];

const fetchRecords = async () => {
    if (userStore.isLoggedIn) {
        const res: any = await request.get("/health/list");
        healthRecords.value = res;
    }
};

onMounted(() => {
    fetchRecords();
});

const handleAddRecord = async () => {
  if (!newRecord.value.weight || !newRecord.value.sleep) {
    ElMessage.warning("请填写完整信息");
    return;
  }
  
  await request.post("/health/add", {
    weight: newRecord.value.weight,
    sleepHours: newRecord.value.sleep,
    mood: newRecord.value.mood
  });
  
  newRecord.value = { weight: "", sleep: "", mood: "😊" };
  ElMessage.success("记录成功");
  fetchRecords();
};

// 计算统计数据
const stats = computed(() => {
  const records = healthRecords.value;
  if (records.length === 0) return null;
  const avgWeight = (records.reduce((a, b) => a + Number(b.weight), 0) / records.length).toFixed(1);
  const avgSleep = (records.reduce((a, b) => a + Number(b.sleepHours), 0) / records.length).toFixed(1);
  return { avgWeight, avgSleep, totalDays: records.length };
});
</script>

<template>
  <div class="health-page">
    <div class="page-header">
      <h1>健康管理</h1>
      <p>记录健康状况，科学锻炼</p>
    </div>

    <div class="health-grid">
      <!-- 健康记录模块 -->
      <div class="health-section">
        <h2>📊 健康记录</h2>

        <div class="stats-cards" v-if="stats">
          <div class="stat-card">
            <span class="stat-value">{{ stats.avgWeight }}</span>
            <span class="stat-label">平均体重 (kg)</span>
          </div>
          <div class="stat-card">
            <span class="stat-value">{{ stats.avgSleep }}</span>
            <span class="stat-label">平均睡眠 (h)</span>
          </div>
          <div class="stat-card">
            <span class="stat-value">{{ stats.totalDays }}</span>
            <span class="stat-label">记录天数</span>
          </div>
        </div>

        <div class="record-form" v-if="userStore.isLoggedIn">
          <h3>添加今日记录</h3>
          <div class="form-row">
            <el-input v-model="newRecord.weight" placeholder="体重(kg)" type="number" />
            <el-input v-model="newRecord.sleep" placeholder="睡眠(h)" type="number" />
            <el-select v-model="newRecord.mood" placeholder="心情" style="width: 100px">
              <el-option v-for="m in moods" :key="m" :label="m" :value="m" />
            </el-select>
            <el-button type="primary" @click="handleAddRecord">记录</el-button>
          </div>
        </div>
        <el-alert v-else type="info" :closable="false" style="margin-bottom: 16px;">
          请先登录后再记录健康数据
        </el-alert>

        <div class="record-table">
          <el-table :data="healthRecords" stripe>
            <el-table-column prop="date" label="日期" width="120" />
            <el-table-column prop="weight" label="体重(kg)" width="100" />
            <el-table-column prop="sleepHours" label="睡眠(h)" width="100" />
            <el-table-column prop="mood" label="心情" width="80" />
          </el-table>
        </div>
      </div>

      <!-- 锻炼建议模块 -->
      <div class="health-section">
        <h2>💪 锻炼建议</h2>
        <p class="section-desc">适当锻炼有助于增强免疫力，预防疾病</p>

        <div class="exercise-list">
          <div v-for="ex in exercises" :key="ex.name" class="exercise-card">
            <span class="exercise-icon">{{ ex.icon }}</span>
            <div class="exercise-info">
              <h3>{{ ex.name }}</h3>
              <p>{{ ex.description }}</p>
              <div class="exercise-meta">
                <el-tag size="small">{{ ex.duration }}</el-tag>
                <el-tag size="small" :type="ex.difficulty === '简单' ? 'success' : 'warning'">
                  {{ ex.difficulty }}
                </el-tag>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.health-page {
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

.health-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;
}

.health-section {
  background: white;
  border-radius: 12px;
  padding: 24px;
}

.health-section h2 {
  font-size: 18px;
  margin-bottom: 16px;
  color: #303133;
}

.section-desc {
  color: #909399;
  font-size: 14px;
  margin-bottom: 16px;
}

.stats-cards {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
  margin-bottom: 20px;
}

.stat-card {
  background: linear-gradient(135deg, #409EFF, #67C23A);
  color: white;
  border-radius: 8px;
  padding: 16px;
  text-align: center;
}

.stat-value {
  display: block;
  font-size: 24px;
  font-weight: 600;
}

.stat-label {
  font-size: 12px;
  opacity: 0.9;
}

.record-form {
  background: #f5f7fa;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 16px;
}

.record-form h3 {
  font-size: 14px;
  margin-bottom: 12px;
  color: #606266;
}

.form-row {
  display: flex;
  gap: 8px;
}

.form-row .el-input {
  flex: 1;
}

.exercise-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.exercise-card {
  display: flex;
  gap: 16px;
  padding: 16px;
  border-radius: 8px;
  background: #f5f7fa;
  transition: all 0.2s;
}

.exercise-card:hover {
  background: #ecf5ff;
}

.exercise-icon {
  font-size: 32px;
}

.exercise-info h3 {
  font-size: 15px;
  margin-bottom: 4px;
  color: #303133;
}

.exercise-info p {
  font-size: 13px;
  color: #606266;
  margin-bottom: 8px;
}

.exercise-meta {
  display: flex;
  gap: 8px;
}

@media (max-width: 768px) {
  .health-grid {
    grid-template-columns: 1fr;
  }
}
</style>
