export interface ConversionRequest {
  name: string;
  amountUsd: number;
  accountingDate: string;
}

export interface Computer {
  id: number;
  name: string;
  accountingDate: string;
  amountUsd: number;
  exchangeRate: number;
  rateEffectiveDate: string;
  amountPln: number;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

export interface ComputerSearch {
  name?: string;
  accountingDate?: string;
  sortBy: 'name' | 'accountingDate';
  direction: 'asc' | 'desc';
  page: number;
  size: number;
}
