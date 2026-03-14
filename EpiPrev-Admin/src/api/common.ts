export type Result<T> = {
  success: boolean;
  data: T;
  message?: string;
  code?: number | string;
};

export type PageResult<T> = {
  records: T[];
  total: number;
  size: number;
  current: number;
};

export type PageParams = {
  current?: number;
  pageSize?: number;
};

export type MultiResult<T> = Result<T[]> & {
  total: number;
  page: number;
  size: number;
};
