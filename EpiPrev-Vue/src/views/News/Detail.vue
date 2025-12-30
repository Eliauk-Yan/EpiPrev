<script setup lang="ts">
import { ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import { ArrowLeft } from "@element-plus/icons-vue";

const route = useRoute();
const router = useRouter();

const news = ref({
  id: Number(route.params.id),
  title: "全国流感监测周报（2024年第2周）",
  date: "2024-01-15",
  source: "国家疾控中心",
  content: `
一、全国流感活动水平

本周全国流感活动水平持续下降，但仍处于流行期。南方省份和北方省份流感活动水平均呈下降趋势。

二、监测数据

- 全国报告流感样病例占门急诊病例总数百分比为5.2%，较上周下降0.8个百分点
- 流感病毒检测阳性率为15.3%，较上周下降3.2个百分点
- 主要流行毒株为甲型H3N2亚型

三、防控建议

1. **个人防护**
   - 保持良好的个人卫生习惯
   - 勤洗手，咳嗽或打喷嚏时用纸巾遮住口鼻
   - 保持室内空气流通

2. **重点人群**
   - 老年人、儿童、孕妇等重点人群应尽量减少外出
   - 如有发热等症状及时就医

3. **接种疫苗**
   - 建议高风险人群及时接种流感疫苗

四、下周预测

预计下周全国流感活动水平将继续下降，但部分地区仍需保持警惕。
  `,
});
</script>

<template>
  <div class="detail-page">
    <div class="back-btn" @click="router.back()">
      <el-icon><ArrowLeft /></el-icon>
      返回列表
    </div>

    <article class="article">
      <header class="article-header">
        <el-tag type="info">{{ news.source }}</el-tag>
        <h1>{{ news.title }}</h1>
        <div class="article-meta">
          <span>📅 {{ news.date }}</span>
        </div>
      </header>

      <div class="article-content">
        <p v-for="(para, idx) in news.content.split('\n\n')" :key="idx" v-html="formatPara(para)"></p>
      </div>
    </article>
  </div>
</template>

<script lang="ts">
function formatPara(text: string) {
  return text
    .replace(/^### (.+)$/gm, '<h3>$1</h3>')
    .replace(/^## (.+)$/gm, '<h2>$1</h2>')
    .replace(/\*\*(.+?)\*\*/g, '<strong>$1</strong>')
    .replace(/^- (.+)$/gm, '<li>$1</li>')
    .replace(/^(\d+)\. (.+)$/gm, '<li>$2</li>')
    .replace(/\n/g, '<br>');
}

export default {};
</script>

<style scoped>
.detail-page {
  max-width: 800px;
  margin: 0 auto;
}

.back-btn {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  color: #409EFF;
  cursor: pointer;
  margin-bottom: 20px;
}

.article {
  background: white;
  border-radius: 12px;
  padding: 40px;
}

.article-header {
  text-align: center;
  margin-bottom: 32px;
  padding-bottom: 24px;
  border-bottom: 1px solid #ebeef5;
}

.article-header h1 {
  font-size: 24px;
  margin: 16px 0;
  color: #303133;
}

.article-meta {
  color: #909399;
  font-size: 14px;
}

.article-content {
  line-height: 1.8;
  color: #606266;
}

.article-content :deep(h2) {
  font-size: 18px;
  margin: 20px 0 12px;
  color: #303133;
}

.article-content :deep(h3) {
  font-size: 16px;
  margin: 16px 0 8px;
  color: #303133;
}

.article-content :deep(li) {
  margin-left: 20px;
  margin-bottom: 8px;
}

.article-content :deep(strong) {
  color: #303133;
}
</style>
