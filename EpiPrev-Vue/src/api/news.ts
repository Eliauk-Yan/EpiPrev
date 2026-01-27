import request from "@/utils/request";

export const getNewsList = (params: any) => {
    return request.get("/news/list", { params });
};
