import request from "@/utils/request";

// 健康记录提交数据类型
export interface HealthRecordDTO {
    weight?: number | string;
    height?: number | string;
    sleepHours?: number | string;
    mood?: string;
    systolic?: number | string;
    diastolic?: number | string;
    heartRate?: number | string;
    temperature?: number | string;
    bloodSugar?: number | string;
    waterIntake?: number | string;
    steps?: number | string;
    recordDate?: string;
}

// 健康记录返回数据类型
export interface HealthRecordVO {
    id: number;
    userId: number;
    weight: number;
    height: number;
    sleepHours: number;
    mood: string;
    systolic: number;
    diastolic: number;
    heartRate: number;
    temperature: number;
    bloodSugar: number;
    waterIntake: number;
    steps: number;
    recordDate: string;
    createTime: string;
}




export const healthApi = {
    // 获取指定日期的详细记录
    getRecordByDate(date: string) {
        return request.get<any, HealthRecordVO>("/health/detail", { params: { date } });
    },

    // 获取今日记录
    getTodayRecord() {
        return request.get<any, HealthRecordVO>("/health/today");
    },

    // 获取最近7天的睡眠数据
    getRecentSleepStats() {
        return request.get<any, HealthRecordVO[]>("/health/sleep/recent");
    },

    // 添加新的健康记录
    addRecord(data: HealthRecordDTO) {
        return request.post("/health/add", data);
    },

    // AI 数据分析
    getDataAnalysis() {
        return request.get<any, string>("/health/ai/analysis", { timeout: 120000 });
    },

    analyzeBmi() {
        return request.get<any, string>("/health/ai/analysis/bmi", { timeout: 120000 });
    },

    analyzeSteps() {
        return request.get<any, string>("/health/ai/analysis/steps", { timeout: 120000 });
    },

    analyzeHeartRate() {
        return request.get<any, string>("/health/ai/analysis/heart-rate", { timeout: 120000 });
    },

    analyzeBloodPressure() {
        return request.get<any, string>("/health/ai/analysis/bp", { timeout: 120000 });
    },

    analyzeWater() {
        return request.get<any, string>("/health/ai/analysis/water", { timeout: 120000 });
    },

    analyzeSleep() {
        return request.get<any, string>("/health/ai/analysis/sleep", { timeout: 120000 });
    },

    // AI 健康建议
    getHealthSuggestion() {
        return request.get<any, string>("/health/ai/suggestion", { timeout: 120000 });
    },

    // 获取每日小贴士
    getDailyTip() {
        return request.get<any, string>("/health/ai/tip", { timeout: 120000 });
    }
};
