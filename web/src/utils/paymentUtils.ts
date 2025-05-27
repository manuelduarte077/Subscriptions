
export const formatCurrency = (amount: number): string => {
  return new Intl.NumberFormat('es-ES', {
    style: 'currency',
    currency: 'EUR',
  }).format(amount);
};

export const getDaysUntilDue = (dueDate: Date): number => {
  const today = new Date();
  const due = new Date(dueDate);
  
  // Reset time to compare only dates
  today.setHours(0, 0, 0, 0);
  due.setHours(0, 0, 0, 0);
  
  const diffTime = due.getTime() - today.getTime();
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
  
  return diffDays;
};

export const getPaymentUrgency = (daysUntilDue: number): 'urgent' | 'upcoming' | 'normal' => {
  if (daysUntilDue <= 3) return 'urgent';
  if (daysUntilDue <= 7) return 'upcoming';
  return 'normal';
};

export const getNextPaymentDate = (currentDate: Date, frequency: string): Date => {
  const nextDate = new Date(currentDate);
  
  switch (frequency) {
    case 'weekly':
      nextDate.setDate(nextDate.getDate() + 7);
      break;
    case 'monthly':
      nextDate.setMonth(nextDate.getMonth() + 1);
      break;
    case 'quarterly':
      nextDate.setMonth(nextDate.getMonth() + 3);
      break;
    case 'yearly':
      nextDate.setFullYear(nextDate.getFullYear() + 1);
      break;
    default:
      // For one-time payments, don't change the date
      break;
  }
  
  return nextDate;
};
