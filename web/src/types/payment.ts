
export interface Payment {
  id: string;
  name: string;
  amount: number;
  provider: string;
  category: PaymentCategory;
  frequency: PaymentFrequency;
  nextDueDate: Date;
  description?: string;
  isActive: boolean;
  createdAt: Date;
  cardLastFour?: string;
  paymentHistory: PaymentHistoryEntry[];
}

export interface PaymentHistoryEntry {
  id: string;
  date: Date;
  status: 'paid' | 'canceled';
  amount: number;
  cardLastFour?: string;
}

export type PaymentCategory = 
  | 'streaming'
  | 'insurance'
  | 'utilities'
  | 'transportation'
  | 'housing'
  | 'subscription'
  | 'other';

export type PaymentFrequency = 
  | 'monthly'
  | 'yearly'
  | 'quarterly'
  | 'weekly'
  | 'one-time';

export interface PaymentFormData {
  name: string;
  amount: number;
  provider: string;
  category: PaymentCategory;
  frequency: PaymentFrequency;
  nextDueDate: Date;
  description?: string;
  cardLastFour?: string;
}
