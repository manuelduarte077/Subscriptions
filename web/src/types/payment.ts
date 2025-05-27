
// Definición del tipo Payment para la aplicación
export interface Payment {
  id: string;
  name: string;
  amount: number;
  provider: string;
  category: PaymentCategory;
  frequency: PaymentFrequency;
  nextDueDate: Date;
  dueDate?: string; // Para compatibilidad con el nuevo servicio
  description?: string;
  isActive: boolean;
  isPaid?: boolean; // Para compatibilidad con el nuevo servicio
  createdAt: Date;
  cardLastFour?: string;
  paymentHistory: PaymentHistoryEntry[];
  userId?: string; // ID del usuario propietario del pago
}

// Tipo para conversión entre el servicio y la aplicación
export type PaymentServiceType = Omit<Payment, 'dueDate'> & {
  dueDate?: string;
};

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
  startDate: Date; // Fecha de inicio de la suscripción
  nextDueDate: Date; // Próxima fecha de pago calculada
  description?: string;
  cardLastFour?: string;
  isPaid?: boolean; // Indica si el pago inicial ya se realizó
}
