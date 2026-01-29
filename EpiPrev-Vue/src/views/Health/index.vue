<script setup lang="ts">
import { ref, computed, onMounted, watch } from "vue";
import zhCn from "element-plus/es/locale/lang/zh-cn";
import { useUserStore } from "@/stores/user";
import { ElMessage } from "element-plus";
import { Sunny, Moon } from "@element-plus/icons-vue";

const userStore = useUserStore();

import { healthApi } from "@/api/health";

// 状态变量
const todayRecord = ref<any>(null); // 今日记录详情
const sleepRecords = ref<any[]>([]); // 睡眠趋势数据

// 今日记录（用于输入框和仪表盘显示）
const newRecord = ref({
  weight: "",
  height: "",
  sleep: "",
  mood: "😊",
  systolic: "",
  diastolic: "",
  heartRate: "",
  temperature: "",
  bloodSugar: "",
  waterIntake: "",
  steps: "",
});

const moods = ["😊", "😐", "😔", "😴", "💪"];

// AI 相关状态变量
const aiAnalysisLoading = ref<Record<string, boolean>>({
  general: false,
  bmi: false,
  steps: false,
  heartRate: false,
  bp: false,
  water: false,
  sleep: false,
});
const aiSuggestionLoading = ref(false);
const aiDialogVisible = ref(false);
const aiDialogTitle = ref("");
const aiDialogContent = ref("");
const dailyTip = ref("正在生成今日健康小贴士..."); // 今日小贴士

// 恢复 AI 函数
const handleAIAnalysis = async (type: string = "general") => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning("请先登录后使用AI分析功能");
    return;
  }

  if (!stats.value && type === "general") {
    // 仅在通用分析时检查是否有数据，或者根据具体需求
    ElMessage.warning("暂无健康数据，请先打卡记录");
    return;
  }

  aiAnalysisLoading.value[type] = true;
  try {
    let res;
    switch (type) {
      case "bmi":
        res = await healthApi.analyzeBmi();
        aiDialogTitle.value = "🤖 BMI与体重分析";
        break;
      case "steps":
        res = await healthApi.analyzeSteps();
        aiDialogTitle.value = "🤖 步数与运动分析";
        break;
      case "heartRate":
        res = await healthApi.analyzeHeartRate();
        aiDialogTitle.value = "🤖 心率健康分析";
        break;
      case "bp":
        res = await healthApi.analyzeBloodPressure();
        aiDialogTitle.value = "🤖 血压健康分析";
        break;
      case "water":
        res = await healthApi.analyzeWater();
        aiDialogTitle.value = "🤖 饮水习惯分析";
        break;
      case "sleep":
        res = await healthApi.analyzeSleep();
        aiDialogTitle.value = "🤖 睡眠质量分析";
        break;
      default:
        res = await healthApi.getDataAnalysis();
        aiDialogTitle.value = "🤖 今日综合数据分析";
        break;
    }

    aiDialogContent.value = res ? res : "暂无分析结果";
    aiDialogVisible.value = true;
  } catch (error) {
    console.error(error);
    ElMessage.error("AI分析失败，请稍后重试");
  } finally {
    aiAnalysisLoading.value[type] = false;
  }
};

// 获取今日记录
const fetchTodayRecord = async () => {
  if (!userStore.isLoggedIn) return;
  try {
    const res = await healthApi.getTodayRecord();
    todayRecord.value = res;

    // 如果今日已有记录，同步到 newRecord 以供仪表盘使用
    if (res) {
      newRecord.value = {
        weight: res.weight ? String(res.weight) : "",
        height: res.height ? String(res.height) : "",
        sleep: res.sleepHours ? String(res.sleepHours) : "",
        mood: res.mood || "😊",
        systolic: res.systolic ? String(res.systolic) : "",
        diastolic: res.diastolic ? String(res.diastolic) : "",
        heartRate: res.heartRate ? String(res.heartRate) : "",
        temperature: res.temperature ? String(res.temperature) : "",
        bloodSugar: res.bloodSugar ? String(res.bloodSugar) : "",
        waterIntake: res.waterIntake ? String(res.waterIntake) : "",
        steps: res.steps ? String(res.steps) : "",
      };
    }
  } catch (error) {
    console.error("Failed to fetch today record", error);
  }
};

// --- 日历逻辑 (基础) ---

const calendarDate = ref(new Date()); // 日历当前绑定日期
const selectedDate = ref<string | null>(null); // 当前选中的日期
const isDailyDialogVisible = ref(false); // 弹窗可见性
const popupRecord = ref<any>(null); // 弹窗数据


// 监听日历日期变化（点击日历也会触发这个）
watch(calendarDate, (val) => {
  const y = val.getFullYear();
  const m = String(val.getMonth() + 1).padStart(2, "0");
  const d = String(val.getDate()).padStart(2, "0");
  const dateStr = `${y}-${m}-${d}`;

  // 如果月份变化，重新获取打卡记录
  // if (val.getMonth() !== lastMonth) {
  //   lastMonth = val.getMonth();
  //   fetchCalendarRecords();
  // }
  
  console.log(`📅 日历选中日期变更: ${dateStr}`);

  // 自动触发详情弹窗
  handleSelectDate(dateStr);
});

const recordLoading = ref(false); // 详情加载状态

// 处理日期点击
const handleSelectDate = async (day: string) => {
  console.log("👆 用户点击日期:", day);
  selectedDate.value = day;
  popupRecord.value = null; // 重置数据
  isDailyDialogVisible.value = true; // 打开弹窗
  recordLoading.value = true; // 开始加载

  try {
    const res = await healthApi.getRecordByDate(day);
    console.log("📦 获取到详情数据:", res);
    popupRecord.value = res;
  } catch (error) {
    console.error("获取记录详情失败", error);
  } finally {
    recordLoading.value = false; // 结束加载
  }
};

// --- End 日历逻辑 ---

// 获取最近7天睡眠数据
const fetchSleepStats = async () => {
  if (!userStore.isLoggedIn) return;
  try {
    const res = await healthApi.getRecentSleepStats();
    sleepRecords.value = res || [];
  } catch (error) {
    console.error("Failed to fetch sleep stats", error);
  }
};

// 获取今日小贴士
const fetchDailyTip = async () => {
  if (!userStore.isLoggedIn) return;
  try {
    const res = await healthApi.getDailyTip();
    if (res) {
      dailyTip.value = res;
    } else {
      dailyTip.value = "保持健康生活，每天都是新的开始！";
    }
  } catch (error) {
    console.error("Failed to fetch daily tip", error);
    dailyTip.value = "暂无法获取小贴士，请检查网络。";
  }
};

// 提交今日记录
const handleAddRecord = async () => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning("请先登录");
    return;
  }
  const r = newRecord.value;
  if (!r.weight && !r.sleep && !r.steps && !r.height) {
    ElMessage.warning("请至少输入一项主要数据（身高、体重、睡眠或步数）");
    return;
  }

  try {
    const recordData = {
      userId: userStore.user?.id,
      recordDate: new Date().toLocaleDateString("en-CA"), // YYYY-MM-DD
      weight: r.weight ? Number(r.weight) : undefined,
      height: r.height ? Number(r.height) : undefined,
      sleepHours: r.sleep ? Number(r.sleep) : undefined,
      mood: r.mood,
      systolic: r.systolic ? Number(r.systolic) : undefined,
      diastolic: r.diastolic ? Number(r.diastolic) : undefined,
      heartRate: r.heartRate ? Number(r.heartRate) : undefined,
      temperature: r.temperature ? Number(r.temperature) : undefined,
      bloodSugar: r.bloodSugar ? Number(r.bloodSugar) : undefined,
      waterIntake: r.waterIntake ? Number(r.waterIntake) : undefined,
      steps: r.steps ? Number(r.steps) : undefined,
    };

    await healthApi.addRecord(recordData);
    ElMessage.success("记录成功");

    // 刷新数据
    fetchTodayRecord();
    fetchSleepStats();
    // 刷新数据
    fetchTodayRecord();
    fetchSleepStats();
  } catch (error) {
    ElMessage.error("添加失败，请重试");
    console.error(error);
  }
};

// 初始化
onMounted(() => {
  if (userStore.isLoggedIn) {
    fetchTodayRecord();
    fetchSleepStats();
    fetchDailyTip();
    fetchTodayRecord();
    fetchSleepStats();
    fetchDailyTip();
  }
});

// 判断今天是否已打卡（供 UI 使用）
const hasTodayRecord = computed(() => {
  return !!todayRecord.value;
});

// 计算统计数据 (基于最近7天睡眠数据作为样本)
const stats = computed(() => {
  const records = sleepRecords.value;
  if (!records || records.length === 0) return null;
  // 过滤掉没有相关数据的记录，避免NaN
  const validWeight = records.filter((r) => r.weight);
  const validSleep = records.filter((r) => r.sleepHours);

  const avgWeight = validWeight.length
    ? (
        validWeight.reduce((a, b) => a + Number(b.weight), 0) /
        validWeight.length
      ).toFixed(1)
    : "--";
  const avgSleep = validSleep.length
    ? (
        validSleep.reduce((a, b) => a + Number(b.sleepHours), 0) /
        validSleep.length
      ).toFixed(1)
    : "--";

  return { avgWeight, avgSleep, totalDays: records.length };
});

// 最新一条记录 (优先取今日，其次取最近历史)
const latestRecord = computed(() => {
  if (todayRecord.value) return todayRecord.value;
  const records = sleepRecords.value;
  if (!records || records.length === 0) return null;
  return records[records.length - 1]; // sleepRecords 是按日期排序的吗？后端看起来是 LIMIT 7 desc 然后 sort asc。是。
});

// BMI 计算
const bmi = computed(() => {
  const weight =
    Number(newRecord.value.weight) ||
    (latestRecord.value?.weight ? Number(latestRecord.value.weight) : 0);
  const h =
    Number(newRecord.value.height) ||
    (latestRecord.value?.height ? Number(latestRecord.value.height) : 0);
  if (!weight || !h) return null;
  const meter = h / 100;
  if (!meter) return null;
  const value = weight / (meter * meter);
  if (!isFinite(value)) return null;
  return Number(value.toFixed(1));
});

const bmiLevel = computed(() => {
  const v = bmi.value;
  if (v == null) return { text: "待计算", color: "#909399" };
  if (v < 18.5) return { text: "偏瘦", color: "#E6A23C" };
  if (v < 24) return { text: "正常", color: "#67C23A" };
  if (v < 28) return { text: "超重", color: "#E6A23C" };
  return { text: "肥胖", color: "#F56C6C" };
});

const bmiPointerLeft = computed(() => {
  const b = bmi.value || 22;
  const min = 10;
  const max = 40;
  const p = ((b - min) / (max - min)) * 100;
  return Math.min(Math.max(p, 0), 100);
});

// 步数状态
const stepsProgress = computed(() => {
  const val = Number(newRecord.value.steps) || 0;
  const target = 10000;
  const percentage = Math.min((val / target) * 100, 100);
  let text = "加油";
  let color = "#E6A23C"; // 默认黄色

  if (val >= target) {
    text = "达标";
    color = "#67C23A"; // 绿色
  } else if (val > 5000) {
    text = "良好";
    color = "#409EFF"; // 蓝色
  }

  return { val, target, percentage, text, color };
});

// 心率与血压状态（基于当前输入值，模拟实时监测）
const heartStatus = computed(() => {
  const hr = Number(newRecord.value.heartRate);
  if (!hr) return { text: "待输入", color: "#909399" };
  if (hr < 60) return { text: "心率偏低", color: "#E6A23C" };
  if (hr <= 100) return { text: "心率正常", color: "#67C23A" };
  return { text: "心率偏快", color: "#F56C6C" };
});

const bpStatus = computed(() => {
  const s = Number(newRecord.value.systolic);
  const d = Number(newRecord.value.diastolic);
  if (!s || !d) return { text: "待输入", color: "#909399" };
  if (s < 90 || d < 60) return { text: "血压偏低", color: "#E6A23C" };
  if (s <= 120 && d <= 80) return { text: "血压理想", color: "#67C23A" };
  if (s <= 140 && d <= 90) return { text: "血压偏高", color: "#E6A23C" };
  return { text: "血压过高", color: "#F56C6C" };
});

// 饮水达标（默认目标 2000 ml）
const waterProgress = computed(() => {
  const val = Number(newRecord.value.waterIntake);
  const target = 2000;
  if (!val) return { percentage: 0, text: "今日饮水待记录" };
  const p = Math.min(Math.max((val / target) * 100, 0), 120);
  return {
    percentage: Number(p.toFixed(0)),
    text: `今日饮水约 ${val} ml / 2000 ml`,
  };
});

// 近期趋势（sparkline 数据）
// 近期趋势（sparkline 数据）
const recentSleeps = computed(() => {
  // 使用新的睡眠接口数据
  const list = sleepRecords.value || [];
  return list.map((r) => Number(r.sleepHours)).filter((n) => !isNaN(n));
});

const sleepBars = computed(() => {
  const vals = recentSleeps.value;
  if (!vals.length) return [];
  const max = Math.max(...vals, 8);
  const totalWidth = 100;
  const totalHeight = 30;
  const gap = 4;
  const count = vals.length;
  const effectiveCount = Math.max(count, 1);
  const barWidth = (totalWidth - (effectiveCount - 1) * gap) / effectiveCount;

  return vals.map((v, i) => {
    const h = (v / max) * totalHeight;
    const renderHeight = h < 1 ? 1 : h;
    return {
      x: i * (barWidth + gap),
      y: totalHeight - renderHeight,
      width: barWidth,
      height: renderHeight,
      value: v,
    };
  });
});

const handleAISuggestion = async () => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning("请先登录后使用AI建议功能");
    return;
  }

  aiSuggestionLoading.value = true;
  try {
    const res = await healthApi.getHealthSuggestion();
    aiDialogTitle.value = "🤖 今日健康建议";
    aiDialogContent.value = res ? res : "暂无建议";
    aiDialogVisible.value = true;
  } catch (error) {
    console.error(error);
    ElMessage.error("获取AI建议失败，请稍后重试");
  } finally {
    aiSuggestionLoading.value = false;
  }
};
</script>

<template>
  <div class="health-page">
    <div class="dashboard-grid">
      <!-- 1. 全能数据记录站 (Left Column) -->
      <div class="dashboard-column record-station-column">
        <div class="section-header">
          <span class="section-icon">✏️</span>
          <span class="section-label">健康记录</span>
        </div>

        <div v-if="userStore.isLoggedIn" class="record-container">
          <!-- 今日已打卡显示 -->
          <div v-if="hasTodayRecord" class="input-card checked-card">
            <div class="checked-content">
              <div class="checked-icon">🎉</div>
              <h3>今日已打卡</h3>
              <p>做得好！请继续保持健康的生活方式。</p>
            </div>
          </div>

          <!-- 未打卡显示输入表单 -->
          <div v-else class="input-card">
            <h3><span class="dot"></span>今日打卡</h3>
            <div class="input-grid wide">
              <div class="input-group">
                <span class="input-label">体重 (kg)</span>
                <el-input
                  v-model="newRecord.weight"
                  placeholder="0.0"
                  type="number"
                >
                  <template #prefix
                    ><el-icon><Sunny /></el-icon
                  ></template>
                </el-input>
              </div>
              <div class="input-group">
                <span class="input-label">睡眠 (h)</span>
                <el-input
                  v-model="newRecord.sleep"
                  placeholder="0.0"
                  type="number"
                >
                  <template #prefix
                    ><el-icon><Moon /></el-icon
                  ></template>
                </el-input>
              </div>
              <div class="input-group">
                <span class="input-label">今日心情</span>
                <el-select v-model="newRecord.mood" placeholder="选择心情">
                  <el-option
                    v-for="m in moods"
                    :key="m"
                    :label="m"
                    :value="m"
                  />
                </el-select>
              </div>
              <div class="input-group">
                <span class="input-label">血压 (收缩压)</span>
                <el-input
                  v-model="newRecord.systolic"
                  placeholder="如 120"
                  type="number"
                />
              </div>
              <div class="input-group">
                <span class="input-label">血压 (舒张压)</span>
                <el-input
                  v-model="newRecord.diastolic"
                  placeholder="如 80"
                  type="number"
                />
              </div>
              <div class="input-group">
                <span class="input-label">心率 (次/分)</span>
                <el-input
                  v-model="newRecord.heartRate"
                  placeholder="如 72"
                  type="number"
                />
              </div>
              <div class="input-group">
                <span class="input-label">体温 (℃)</span>
                <el-input
                  v-model="newRecord.temperature"
                  placeholder="如 36.5"
                  type="number"
                />
              </div>
              <div class="input-group">
                <span class="input-label">血糖 (mmol/L)</span>
                <el-input
                  v-model="newRecord.bloodSugar"
                  placeholder="如 5.1"
                  type="number"
                />
              </div>
              <div class="input-group">
                <span class="input-label">饮水量 (ml)</span>
                <el-input
                  v-model="newRecord.waterIntake"
                  placeholder="如 1500"
                  type="number"
                />
              </div>
              <div class="input-group">
                <span class="input-label">步数 (步)</span>
                <el-input
                  v-model="newRecord.steps"
                  placeholder="如 8000"
                  type="number"
                />
              </div>
              <div class="input-group">
                <span class="input-label">身高 (cm，用于 BMI)</span>
                <el-input
                  v-model="newRecord.height"
                  placeholder="如 170"
                  type="number"
                />
              </div>
            </div>
            <el-button
              type="primary"
              class="submit-btn"
              @click="handleAddRecord"
              round
            >
              提交记录
            </el-button>
          </div>
        </div>
        <el-alert v-else type="primary" show-icon :closable="false">
          请登录以开始记录您的健康数据
        </el-alert>
      </div>

      <!-- 2. 中间区域：数据分析 + 健康建议 + 打卡记录 -->
      <div class="dashboard-column center-column">
        <!-- 数据分析与健康建议并列 -->
        <div class="top-cards-row">
          <!-- 数据分析卡片 -->
          <div class="center-card ai-card-container">
            <div class="card-header">📊 今日数据分析</div>
            <div class="ai-btn-wrapper">
              <el-button
                type="primary"
                size="large"
                class="ai-btn-large"
                @click="handleAIAnalysis('general')"
                :loading="aiAnalysisLoading.general"
              >
                {{ aiAnalysisLoading.general ? "AI 正在分析中..." : "🤖 AI 智能分析" }}
              </el-button>
            </div>
          </div>

          <!-- 健康建议卡片 -->
          <div class="center-card ai-card-container">
            <div class="card-header">💪 今日健康建议</div>
            <div class="ai-btn-wrapper">
              <el-button
                type="success"
                size="large"
                class="ai-btn-large"
                @click="handleAISuggestion"
                :loading="aiSuggestionLoading"
              >
                {{
                  aiSuggestionLoading ? "AI 正在思考建议..." : "🤖 AI 个性建议"
                }}
              </el-button>
            </div>
          </div>
        </div>

        <div class="center-card calendar-card">
          <div class="card-header">
            📅 打卡记录
            <span style="font-size: 12px; color: #909399; margin-left: 10px; font-weight: normal;">(点击日期可查看当日打卡详情)</span>
          </div>
          <div class="history-calendar">
            <el-config-provider :locale="zhCn">
              <el-calendar v-model="calendarDate">
                <template #dateCell="{ data }">
                  <div class="calendar-cell">
                    <span class="day-number">{{ data.day.split("-")[2] }}</span>
                    <span class="day-number">{{ data.day.split("-")[2] }}</span>
                  </div>
                </template>
              </el-calendar>
            </el-config-provider>
          </div>
        </div>

        <!-- 详情弹窗 -->
        <el-dialog
          v-model="isDailyDialogVisible"
          :title="selectedDate ? `📋 ${selectedDate} 打卡记录` : '当日打卡记录'"
          width="480px"
          class="record-dialog"
          destroy-on-close
          align-center
          append-to-body
        >
         <div v-loading="recordLoading" style="min-height: 150px;">
            <div v-if="popupRecord" class="record-detail-card">
            <div
              class="daily-record-header"
              style="
                display: flex;
                justify-content: space-between;
                margin-bottom: 15px;
                border-bottom: 1px solid #eee;
                padding-bottom: 10px;
              "
            >
              <span
                class="record-label"
                style="font-weight: bold; color: #67c23a"
                >✅ 已打卡</span
              >
              <span class="daily-mood" style="font-size: 24px">{{
                popupRecord.mood
              }}</span>
            </div>
            <div class="daily-body-grid">
              <div class="record-item" v-if="popupRecord.height">
                <span class="record-label">身高</span>
                <span class="record-value">{{ popupRecord.height }} cm</span>
              </div>
              <div class="record-item" v-if="popupRecord.weight">
                <span class="record-label">体重</span>
                <span class="record-value">{{ popupRecord.weight }} kg</span>
              </div>
              <div class="record-item" v-if="popupRecord.sleepHours">
                <span class="record-label">睡眠</span>
                <span class="record-value">{{ popupRecord.sleepHours }} h</span>
              </div>
              <div
                class="record-item"
                v-if="popupRecord.systolic && popupRecord.diastolic"
              >
                <span class="record-label">血压</span>
                <span class="record-value"
                  >{{ popupRecord.systolic }}/{{ popupRecord.diastolic }}</span
                >
              </div>
              <div class="record-item" v-if="popupRecord.heartRate">
                <span class="record-label">心率</span>
                <span class="record-value"
                  >{{ popupRecord.heartRate }} 次/分</span
                >
              </div>
              <div class="record-item" v-if="popupRecord.temperature">
                <span class="record-label">体温</span>
                <span class="record-value"
                  >{{ popupRecord.temperature }} ℃</span
                >
              </div>
              <div class="record-item" v-if="popupRecord.bloodSugar">
                <span class="record-label">血糖</span>
                <span class="record-value"
                  >{{ popupRecord.bloodSugar }} mmol/L</span
                >
              </div>
              <div class="record-item" v-if="popupRecord.waterIntake">
                <span class="record-label">饮水</span>
                <span class="record-value"
                  >{{ popupRecord.waterIntake }} ml</span
                >
              </div>
              <div class="record-item" v-if="popupRecord.steps">
                <span class="record-label">步数</span>
                <span class="record-value">{{ popupRecord.steps }} 步</span>
              </div>
            </div>
          </div>
          <div
            v-else-if="!recordLoading"
            class="loading-content"
            style="text-align: center; padding: 40px; color: #909399"
          >
            <p>暂无数据</p>
          </div>
         </div>
        </el-dialog>
      </div>

      <!-- 3. 右侧：可视化健康仪表盘 -->
      <div class="dashboard-column right-column">
        <!-- 单独的小贴士卡片（在仪表盘大卡片上方） -->
        <div class="right-tip-card">
          <div class="tip-header">💡 每日小贴士</div>
          <p class="tip-content">
            {{ dailyTip }}
          </p>
        </div>

        <!-- 下方整块为仪表盘大卡片 -->
        <div class="right-dashboard-card">
          <div class="section-header">
            <span class="section-icon">💓</span>
            <span class="section-label">健康仪表盘</span>
          </div>

          <div class="visual-panel">
            <!-- BMI 仪表：单独占一行 -->
            <!-- 步数仪表：单独占一行 -->
            <!-- BMI 仪表：单独占一行 -->
            <div class="visual-card bmi-card" style="padding: 15px">
              <div class="visual-title" style="margin-bottom: 10px">
                BMI 指数
              </div>

              <div class="bmi-content-new">
                <!-- 第一行：数值、标签、AI按钮 -->
                <div
                  style="
                    display: flex;
                    justify-content: space-between;
                    align-items: center;
                    margin-bottom: 25px;
                  "
                >
                  <div style="display: flex; align-items: flex-end; gap: 8px">
                    <span
                      class="bmi-num"
                      :style="{
                        color: bmiLevel.color,
                        fontSize: '28px',
                        fontWeight: 'bold',
                        lineHeight: 1,
                      }"
                    >
                      {{ bmi ?? "--" }}
                    </span>
                    <span
                      class="bmi-text-tag"
                      :style="{
                        backgroundColor: bmiLevel.color + '20',
                        color: bmiLevel.color,
                        padding: '2px 8px',
                        borderRadius: '4px',
                        fontSize: '12px',
                      }"
                    >
                      {{ bmiLevel.text }}
                    </span>
                  </div>
                  <el-button
                    link
                    type="primary"
                    size="small"
                    @click="handleAIAnalysis('bmi')"
                    :loading="aiAnalysisLoading.bmi"
                  >
                    🤖 AI分析
                  </el-button>
                </div>

                <!-- 第二行：线性刻度条 -->
                <div style="position: relative; height: 30px">
                  <!-- 色条背景 -->
                  <div
                    style="
                      display: flex;
                      width: 100%;
                      height: 8px;
                      border-radius: 4px;
                      overflow: hidden;
                    "
                  >
                    <!-- 10 ~ 18.5 (Diff 8.5) -->
                    <div style="width: 28.3%; background: #e6a23c"></div>
                    <!-- 18.5 ~ 24 (Diff 5.5) -->
                    <div style="width: 18.3%; background: #67c23a"></div>
                    <!-- 24 ~ 28 (Diff 4) -->
                    <div style="width: 13.3%; background: #e6a23c"></div>
                    <!-- 28 ~ 40 (Diff 12) -->
                    <div style="flex: 1; background: #f56c6c"></div>
                  </div>

                  <!-- 指针 -->
                  <div
                    class="bmi-pointer"
                    :style="{
                      position: 'absolute',
                      top: '-6px',
                      left: bmiPointerLeft + '%',
                      transform: 'translateX(-50%)',
                      transition: 'left 0.5s ease',
                    }"
                  >
                    <div
                      :style="{
                        width: 0,
                        height: 0,
                        borderLeft: '6px solid transparent',
                        borderRight: '6px solid transparent',
                        borderTop: '8px solid ' + bmiLevel.color,
                      }"
                    ></div>
                  </div>

                  <!-- 刻度文字 -->
                  <div
                    style="
                      position: relative;
                      top: 4px;
                      font-size: 10px;
                      color: #909399;
                      height: 12px;
                    "
                  >
                    <span
                      style="
                        position: absolute;
                        left: 28.3%;
                        transform: translateX(-50%);
                      "
                      >18.5</span
                    >
                    <span
                      style="
                        position: absolute;
                        left: 46.6%;
                        transform: translateX(-50%);
                      "
                      >24.0</span
                    >
                    <span
                      style="
                        position: absolute;
                        left: 60%;
                        transform: translateX(-50%);
                      "
                      >28.0</span
                    >
                  </div>
                </div>
              </div>
            </div>

            <!-- 步数仪表：单独占一行 -->
            <div class="visual-card steps-card">
              <div class="visual-title">今日步数</div>
              <div
                class="bmi-gauge-wrapper"
                style="display: flex; align-items: center"
              >
                <div class="bmi-gauge">
                  <svg viewBox="0 0 100 50" class="bmi-svg">
                    <path
                      d="M5 50 A45 45 0 0 1 95 50"
                      fill="none"
                      stroke="#f0f2f5"
                      stroke-width="10"
                    />
                    <path
                      d="M5 50 A45 45 0 0 1 95 50"
                      :stroke="stepsProgress.color"
                      stroke-width="10"
                      fill="none"
                      :stroke-dasharray="`${(stepsProgress.percentage / 100) * 141.37} 141.37`"
                      stroke-linecap="round"
                      class="step-progress-path"
                    />
                  </svg>
                  <div class="bmi-value">
                    <div class="bmi-number">{{ stepsProgress.val }}</div>
                  </div>
                </div>
                <div
                  style="
                    flex: 1;
                    display: flex;
                    justify-content: flex-end;
                    padding-right: 10px;
                  "
                >
                  <el-button
                    link
                    type="primary"
                    size="small"
                    @click="handleAIAnalysis('steps')"
                    :loading="aiAnalysisLoading.steps"
                  >
                    🤖 AI分析
                  </el-button>
                </div>
              </div>
            </div>

            <!-- 心率与血压状态：单独一行 -->
            <div class="visual-card status-card">
              <div class="visual-title">心率与血压监测</div>
              <div class="status-item">
                <span class="status-label">心率</span>
                <span class="status-value"
                  >{{ newRecord.heartRate || "--" }} 次/分</span
                >
                <el-button
                  link
                  type="primary"
                  size="small"
                  @click="handleAIAnalysis('heartRate')"
                  :loading="aiAnalysisLoading.heartRate"
                >
                  🤖 AI分析
                </el-button>
              </div>
              <div class="status-item">
                <span class="status-label">血压</span>
                <span class="status-value">
                  {{ newRecord.systolic || "--" }}/{{
                    newRecord.diastolic || "--"
                  }}
                  mmHg
                </span>
                <el-button
                  link
                  type="primary"
                  size="small"
                  @click="handleAIAnalysis('bp')"
                  :loading="aiAnalysisLoading.bp"
                >
                  🤖 AI分析
                </el-button>
              </div>
            </div>

            <!-- 饮水达标球：单独一行 -->
            <div class="visual-card water-card">
              <div class="visual-title">饮水达标球</div>
              <div class="water-wrapper">
                <div class="water-sphere">
                  <div
                    class="water-fill"
                    :style="{ height: waterProgress.percentage + '%' }"
                  ></div>
                  <div
                    class="water-text"
                    style="font-size: 13px; white-space: nowrap"
                  >
                    {{ newRecord.waterIntake || 0 }} ml
                  </div>
                </div>
                <div
                  class="water-info-right"
                  style="
                    flex: 1;
                    display: flex;
                    flex-direction: column;
                    justify-content: center;
                    gap: 8px;
                  "
                >
                  <el-button
                    link
                    type="primary"
                    size="small"
                    @click="handleAIAnalysis('water')"
                    :loading="aiAnalysisLoading.water"
                    style="align-self: flex-start"
                  >
                    🤖 AI分析
                  </el-button>
                </div>
              </div>
            </div>

            <!-- 近期趋势迷你图：单独一行 -->
            <div class="visual-card trend-card">
              <div class="visual-title">近期睡眠趋势 (7天)</div>
              <div class="trend-item">
                <span class="trend-label">睡眠</span>
                <svg viewBox="0 0 100 30" class="sparkline">
                  <rect
                    v-for="(bar, index) in sleepBars"
                    :key="index"
                    :x="bar.x"
                    :y="bar.y"
                    :width="bar.width"
                    :height="bar.height"
                    fill="#67C23A"
                    rx="1"
                  ></rect>
                </svg>
                <el-button
                  link
                  type="primary"
                  size="small"
                  @click="handleAIAnalysis('sleep')"
                  :loading="aiAnalysisLoading.sleep"
                  style="margin-left: 8px"
                >
                  🤖 AI分析
                </el-button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- AI分析结果对话框 -->
    <el-dialog
      v-model="aiDialogVisible"
      :title="aiDialogTitle"
      width="520px"
      class="ai-dialog"
    >
      <div class="ai-content">
        <pre class="ai-text">{{ aiDialogContent }}</pre>
      </div>
      <template #footer>
        <el-button type="primary" @click="aiDialogVisible = false">
          我知道了
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.health-page {
  padding: 30px 20px;
  max-width: 1400px;
  margin: 0 auto;
  min-height: calc(100vh - 120px);
}

.page-header {
  text-align: center;
  margin-bottom: 30px;
}

.page-header h1 {
  font-size: 28px;
  font-weight: 700;
  color: #2c3e50;
  margin-bottom: 8px;
  letter-spacing: -0.5px;
}

.subtitle {
  color: #7f8c8d;
  font-size: 16px;
}

.dashboard-grid {
  display: grid;
  grid-template-columns: 280px 1fr 340px;
  gap: 24px;
  align-items: start;
}

.dashboard-column {
  background: white;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  min-height: 500px;
  display: flex;
  flex-direction: column;
}

.column-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 2px solid #f0f0f0;
}

.column-header h2 {
  font-size: 18px;
  margin: 0;
  color: #2c3e50;
  font-weight: 600;
}

.column-header .el-icon {
  font-size: 20px;
  color: #409eff;
}

/* 区块标题 - 与其他页面保持一致 */
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

/* Analysis Column Styles */
.analysis-content {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.stat-card {
  background: #f8f9fa;
  border-radius: 12px;
  padding: 16px;
  display: flex;
  align-items: center;
  gap: 16px;
  transition: transform 0.2s;
}

.stat-card:hover {
  transform: translateY(-2px);
  background: #ecf5ff;
}

.stat-icon {
  font-size: 24px;
  background: white;
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.stat-info {
  display: flex;
  flex-direction: column;
}

.stat-info .label {
  font-size: 12px;
  color: #95a5a6;
  margin-bottom: 4px;
}

.stat-info .value {
  font-size: 20px;
  font-weight: 700;
  color: #2c3e50;
}

.stat-info .value small {
  font-size: 12px;
  font-weight: 400;
  color: #95a5a6;
  margin-left: 2px;
}

.divider {
  height: 1px;
  background: #f0f2f5;
  margin: 10px 0;
}

/* 中间列样式 - 透明背景，包含独立卡片 */
.center-column {
  display: flex;
  flex-direction: column;
  gap: 16px;
  background: transparent;
  padding: 0;
  box-shadow: none;
  min-height: auto;
}

/* 顶部卡片并列容器 */
.top-cards-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.top-cards-row .center-card {
  flex: 1;
}

.center-card {
  background: white;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  display: flex;
  flex-direction: column;
}

.top-cards-row .center-card {
  min-height: 180px;
}

.top-cards-row .center-card .card-content {
  flex: 1;
}

.center-card.calendar-card {
  flex: 1;
}

.card-header {
  font-size: 15px;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 12px;
}

.card-content {
  margin: 0;
  font-size: 13px;
  line-height: 1.7;
  color: #606266;
}

.card-content strong {
  color: #409eff;
}

.empty-hint {
  color: #909399;
}

/* AI按钮样式 */
.ai-btn {
  margin-top: 12px;
  width: 100%;
  border-radius: 8px;
}

/* AI对话框样式 */
.ai-content {
  max-height: 60vh;
  overflow-y: auto;
}

.ai-text {
  margin: 0;
  padding: 16px;
  background: #f8f9fb;
  border-radius: 12px;
  font-size: 14px;
  line-height: 1.8;
  color: #2c3e50;
  white-space: pre-wrap;
  word-break: break-word;
  font-family:
    -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
}

.dashboard-tip-text {
  font-size: 12px;
  color: #909399;
  margin-bottom: 8px;
}

.health-score {
  text-align: center;
  padding: 20px 0;
}

.score-circle {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: linear-gradient(135deg, #a8e6cf, #dcedc1);
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 10px auto;
  box-shadow: 0 4px 15px rgba(168, 230, 207, 0.4);
}

.score {
  font-size: 32px;
  font-weight: 800;
  color: #27ae60;
}

.score-desc {
  font-size: 13px;
  color: #7f8c8d;
  margin-top: 10px;
  line-height: 1.5;
}

/* Record Column Styles */
.input-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  padding: 20px;
  color: white;
  margin-bottom: 20px;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.25);
}

.input-card h3 {
  margin: 0 0 16px 0;
  font-size: 16px;
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 600;
}

.dot {
  width: 8px;
  height: 8px;
  background: #ffd93d;
  border-radius: 50%;
  box-shadow: 0 0 8px rgba(255, 217, 61, 0.6);
}

.input-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
  margin-bottom: 16px;
}

.input-grid.wide {
  grid-template-columns: 1fr 1fr;
}

.input-group {
  display: flex;
  flex-direction: column;
}

.input-label {
  display: block;
  font-size: 12px;
  margin-bottom: 6px;
  opacity: 0.9;
}

.submit-btn {
  width: 100%;
  background: white;
  color: #667eea;
  border: none;
  font-weight: 600;
}

.submit-btn:hover {
  background: #f8f9fa;
  color: #764ba2;
}

.history-list h3 {
  font-size: 16px;
  color: #2c3e50;
  margin-bottom: 16px;
  padding-left: 10px;
  border-left: 4px solid #409eff;
}

.date-tag {
  color: #7f8c8d;
  font-family: monospace;
}

.unit {
  font-size: 12px;
  color: #bdc3c7;
}

.sleep-bar {
  display: flex;
  align-items: center;
  gap: 10px;
}

.sleep-bar .el-progress {
  flex: 1;
}

.sleep-text {
  width: 40px;
  font-size: 12px;
  color: #606266;
  text-align: right;
}

.mood-icon {
  font-size: 20px;
}

/* Advice Column Styles */
.advice-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.advice-card {
  background: #fff;
  border: 1px solid #f0f2f5;
  border-left: 4px solid #409eff;
  padding: 16px;
  border-radius: 8px;
  transition: all 0.3s;
}

.advice-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  transform: translateX(4px);
}

.advice-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 8px;
}

.advice-icon {
  font-size: 22px;
}

.advice-name {
  font-weight: 600;
  color: #2c3e50;
  font-size: 15px;
}

.advice-desc {
  font-size: 13px;
  color: #7f8c8d;
  line-height: 1.5;
  margin-bottom: 12px;
}

.advice-tags {
  display: flex;
  gap: 8px;
}

/* 记录容器，用于高度填充 */
.record-container {
  display: flex;
  flex-direction: column;
  flex: 1;
}

/* 已打卡提示样式 */
.checked-card {
  background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%) !important;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  height: auto;
  flex: 1; /* 自动填满剩余空间 */
  min-height: 400px; /* 保证最小高度，避免太扁 */
}

.checked-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}

.checked-icon {
  font-size: 48px;
  margin-bottom: 8px;
  animation: bounce 2s infinite;
}

@keyframes bounce {
  0%,
  20%,
  50%,
  80%,
  100% {
    transform: translateY(0);
  }
  40% {
    transform: translateY(-10px);
  }
  60% {
    transform: translateY(-5px);
  }
}

.checked-content h3 {
  font-size: 20px;
  color: #2c3e50;
  margin: 0;
}

.checked-content p {
  color: #606266;
  margin: 0;
  font-size: 14px;
}

.daily-tip {
  margin-top: auto;
  background: linear-gradient(135deg, #fff3e0, #ffe0b2);
  border-radius: 12px;
  padding: 16px;
  display: flex;
  gap: 12px;
  align-items: flex-start;
}

.tip-icon {
  background: #ff9f43;
  color: white;
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.tip-content h4 {
  margin: 0 0 4px 0;
  color: #d35400;
  font-size: 14px;
}

.tip-content p {
  margin: 0;
  font-size: 12px;
  color: #e67e22;
  line-height: 1.4;
}

/* 右侧仪表盘区域 */
.right-column {
  display: flex;
  flex-direction: column;
  gap: 16px;
  background: transparent;
  padding: 0;
  box-shadow: none;
  min-height: auto;
}

.right-tip-card,
.right-dashboard-card {
  background: white;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}

.tip-header {
  font-size: 15px;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 10px;
}

.right-tip-card p {
  margin: 0;
  font-size: 13px;
  line-height: 1.6;
  color: #606266;
}

.visual-panel {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.visual-row {
  display: grid;
  grid-template-columns: 1.1fr 1.1fr;
  gap: 12px;
}

.visual-card {
  background: #f8f9fb;
  border-radius: 12px;
  padding: 14px 16px;
}

.visual-title {
  font-size: 13px;
  color: #606266;
  margin-bottom: 8px;
  font-weight: 600;
}

.bmi-gauge-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
}

/* 删除旧的 BMI Gauge 相关样式 */
.bmi-gauge-wrapper {
  display: flex;
  align-items: center;
}
.bmi-gauge {
  position: relative;
  width: 130px;
  height: 80px;
}
.bmi-svg {
  width: 100%;
  height: 100%;
}
/* ... 保留用于步数的样式 ... */
.bmi-value {
  position: absolute;
  left: 50%;
  bottom: 0;
  transform: translateX(-50%);
  text-align: center;
}
.bmi-number {
  font-size: 22px;
  font-weight: 700;
  color: #2c3e50;
}
/* 新增 BMI 线性刻度样式 */
.bmi-content-new {
  width: 100%;
}
.bmi-num {
  font-family: "Inter", sans-serif;
}

/* 恢复状态卡片样式 */
.status-card .status-item {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 6px;
}

.status-label {
  font-size: 12px;
  color: #909399;
  width: 40px;
}

.status-value {
  font-size: 13px;
  color: #303133;
  flex: 1;
}

.status-tag {
  font-size: 11px;
  padding: 2px 8px;
  border-radius: 999px;
}

.water-wrapper {
  display: flex;
  align-items: center;
  gap: 12px;
}

.water-sphere {
  position: relative;
  width: 70px;
  height: 70px;
  border-radius: 50%;
  background: #ecf5ff;
  overflow: hidden;
  box-shadow: inset 0 0 10px rgba(64, 158, 255, 0.3);
}

.water-fill {
  position: absolute;
  left: 0;
  bottom: 0;
  width: 100%;
  background: linear-gradient(180deg, #a0cfff, #409eff);
  transition: height 0.4s ease;
}

.water-text {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 16px;
  font-weight: 600;
  color: #fff;
  text-shadow: 0 0 4px rgba(0, 0, 0, 0.25);
}

.water-desc {
  font-size: 12px;
  color: #606266;
  line-height: 1.5;
}

.trend-card .trend-item {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 6px;
}

.trend-label {
  font-size: 12px;
  color: #909399;
  width: 40px;
}

.sparkline {
  flex: 1;
}

.ai-panel {
  display: none;
}

/* 日历与当日记录样式 */
.history-calendar {
  margin-top: 0;
}

.daily-record-panel h3 {
  font-size: 16px;
  color: #2c3e50;
  margin-bottom: 12px;
  padding-left: 10px;
  border-left: 4px solid #409eff;
}

.calendar-cell {
  position: relative;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  border-radius: 8px;
  transition:
    background-color 0.2s,
    transform 0.1s;
}

.calendar-cell:hover {
  background-color: #f5f7fa;
  transform: translateY(-1px);
}

.day-number {
  font-size: 14px;
  color: #303133;
}

.record-detail-card {
  border-radius: 12px;
  border: 1px solid #e4e7ed;
  padding: 16px;
  margin-bottom: 12px;
  background-color: #f8f9fa;
}

.daily-record-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
  padding-bottom: 10px;
  border-bottom: 1px solid #ebeef5;
}

.daily-record-header .record-label {
  font-size: 14px;
  font-weight: 600;
  color: #67c23a;
}

.daily-mood {
  font-size: 24px;
}

/* 记录项网格布局 */
.daily-body-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}

.record-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.04);
}

.record-icon {
  font-size: 16px;
}

.record-item .record-label {
  font-size: 12px;
  color: #909399;
  min-width: 32px;
}

.record-value {
  font-size: 14px;
  font-weight: 600;
  color: #2c3e50;
  margin-left: auto;
}

.no-record-content {
  padding: 20px 0;
}

.daily-body {
  display: flex;
  gap: 16px;
  font-size: 13px;
  color: #303133;
}

/* Responsive */
@media (max-width: 1200px) {
  .dashboard-grid {
    grid-template-columns: 260px 1fr 300px;
    gap: 20px;
  }
}

@media (max-width: 1024px) {
  .dashboard-grid {
    grid-template-columns: 1fr;
  }

  .dashboard-column {
    min-height: auto;
  }

  .right-column {
    order: 2;
  }

  .center-column {
    order: 3;
  }
}

@media (max-width: 768px) {
  .health-page {
    padding: 16px;
  }

  .input-grid {
    grid-template-columns: 1fr;
  }

  .top-cards-row {
    grid-template-columns: 1fr;
  }
}

/* AI Button Styles */
.ai-card-container {
  display: flex;
  flex-direction: column;
}

.ai-btn-wrapper {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px 0;
}

.ai-btn-large {
  width: 80%;
  height: 60px;
  font-size: 18px !important;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
  transition: all 0.3s;
}

.ai-btn-large:hover {
  box-shadow: 0 6px 16px rgba(64, 158, 255, 0.4);
}
</style>
