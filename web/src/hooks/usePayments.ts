import { useState, useEffect } from 'react';
import { Payment, PaymentFormData, PaymentHistoryEntry } from '@/types/payment';

// Clave constante para localStorage
const STORAGE_KEY = 'payment-reminders';

export const usePayments = () => {
  const [payments, setPayments] = useState<Payment[]>([]);

  // Load payments from localStorage on mount
  useEffect(() => {
    console.log('usePayments - Loading payments from localStorage');
    try {
      const savedPayments = localStorage.getItem(STORAGE_KEY);
      console.log('usePayments - Raw saved data:', savedPayments);
      
      if (savedPayments) {
        try {
          const parsed = JSON.parse(savedPayments);
          console.log('usePayments - Parsed data:', parsed);
          
          if (!Array.isArray(parsed)) {
            console.error('usePayments - Saved data is not an array');
            return;
          }
          
          // Convert date strings back to Date objects
          const paymentsWithDates = parsed.map((payment: any) => {
            try {
              return {
                ...payment,
                nextDueDate: new Date(payment.nextDueDate),
                createdAt: new Date(payment.createdAt),
                paymentHistory: Array.isArray(payment.paymentHistory) 
                  ? payment.paymentHistory.map((entry: any) => ({
                      ...entry,
                      date: new Date(entry.date)
                    })) 
                  : [],
              };
            } catch (err) {
              console.error('usePayments - Error processing payment:', payment, err);
              return null;
            }
          }).filter(Boolean); // Remove any null entries
          
          console.log('usePayments - Payments with dates:', paymentsWithDates);
          setPayments(paymentsWithDates);
        } catch (error) {
          console.error('usePayments - Error parsing saved payments:', error);
          // Limpiar localStorage si hay un error de parseo
          localStorage.removeItem(STORAGE_KEY);
        }
      } else {
        console.log('usePayments - No saved payments found');
      }
    } catch (error) {
      console.error('usePayments - Error accessing localStorage:', error);
    }
  }, []);

  // Save to localStorage whenever payments change
  useEffect(() => {
    try {
      if (payments.length > 0) {
        console.log('usePayments - Saving payments to localStorage:', payments);
        const serializedData = JSON.stringify(payments);
        localStorage.setItem(STORAGE_KEY, serializedData);
        console.log('usePayments - Data saved successfully, size:', serializedData.length);
      } else {
        console.log('usePayments - No payments to save');
      }
    } catch (error) {
      console.error('usePayments - Error saving to localStorage:', error);
    }
  }, [payments]);

  const addPayment = (paymentData: PaymentFormData) => {
    try {
      console.log('usePayments - Adding new payment:', paymentData);
      const newPayment: Payment = {
        id: crypto.randomUUID(),
        ...paymentData,
        isActive: true,
        createdAt: new Date(),
        paymentHistory: [],
      };
      console.log('usePayments - New payment created:', newPayment);
      setPayments(prev => {
        const updated = [...prev, newPayment];
        console.log('usePayments - Updated payments array:', updated);
        return updated;
      });
      
      // Verificar inmediatamente si se guardó correctamente
      setTimeout(() => {
        try {
          const savedData = localStorage.getItem(STORAGE_KEY);
          console.log('usePayments - Verification after save:', savedData ? 'Data saved' : 'No data found');
        } catch (error) {
          console.error('usePayments - Error verifying save:', error);
        }
      }, 100);
    } catch (error) {
      console.error('usePayments - Error adding payment:', error);
    }
  };

  const updatePayment = (id: string, updatedData: Partial<Payment>) => {
    try {
      setPayments(prev => 
        prev.map(payment => 
          payment.id === id ? { ...payment, ...updatedData } : payment
        )
      );
    } catch (error) {
      console.error('usePayments - Error updating payment:', error);
    }
  };

  const deletePayment = (id: string) => {
    try {
      setPayments(prev => prev.filter(payment => payment.id !== id));
    } catch (error) {
      console.error('usePayments - Error deleting payment:', error);
    }
  };

  const markPaymentAsPaid = (id: string, cardLastFour?: string) => {
    try {
      setPayments(prev => 
        prev.map(payment => {
          if (payment.id === id) {
            const historyEntry: PaymentHistoryEntry = {
              id: crypto.randomUUID(),
              date: new Date(),
              status: 'paid',
              amount: payment.amount,
              cardLastFour: cardLastFour || payment.cardLastFour
            };

            // Calculate next due date based on frequency
            const nextDue = new Date(payment.nextDueDate);
            switch (payment.frequency) {
              case 'monthly':
                nextDue.setMonth(nextDue.getMonth() + 1);
                break;
              case 'yearly':
                nextDue.setFullYear(nextDue.getFullYear() + 1);
                break;
              case 'quarterly':
                nextDue.setMonth(nextDue.getMonth() + 3);
                break;
              case 'weekly':
                nextDue.setDate(nextDue.getDate() + 7);
                break;
              case 'one-time':
                return {
                  ...payment,
                  isActive: false,
                  paymentHistory: [...payment.paymentHistory, historyEntry],
                  cardLastFour: cardLastFour || payment.cardLastFour
                };
            }

            return {
              ...payment,
              nextDueDate: nextDue,
              paymentHistory: [...payment.paymentHistory, historyEntry],
              cardLastFour: cardLastFour || payment.cardLastFour
            };
          }
          return payment;
        })
      );
    } catch (error) {
      console.error('usePayments - Error marking payment as paid:', error);
    }
  };

  const cancelPayment = (id: string) => {
    try {
      setPayments(prev => 
        prev.map(payment => {
          if (payment.id === id) {
            const historyEntry: PaymentHistoryEntry = {
              id: crypto.randomUUID(),
              date: new Date(),
              status: 'canceled',
              amount: payment.amount
            };

            return {
              ...payment,
              isActive: false,
              paymentHistory: [...payment.paymentHistory, historyEntry]
            };
          }
          return payment;
        })
      );
    } catch (error) {
      console.error('usePayments - Error canceling payment:', error);
    }
  };

  const resumePayment = (id: string, nextDueDate: Date) => {
    try {
      setPayments(prev => 
        prev.map(payment => 
          payment.id === id ? { ...payment, isActive: true, nextDueDate } : payment
        )
      );
    } catch (error) {
      console.error('usePayments - Error resuming payment:', error);
    }
  };

  const getUpcomingPayments = (days: number = 7) => {
    try {
      const today = new Date();
      const futureDate = new Date();
      futureDate.setDate(today.getDate() + days);

      return payments.filter(payment => {
        if (!payment.nextDueDate) return false;
        
        const dueDate = new Date(payment.nextDueDate);
        return dueDate >= today && dueDate <= futureDate && payment.isActive;
      }).sort((a, b) => {
        const dateA = new Date(a.nextDueDate).getTime();
        const dateB = new Date(b.nextDueDate).getTime();
        return dateA - dateB;
      });
    } catch (error) {
      console.error('usePayments - Error getting upcoming payments:', error);
      return [];
    }
  };

  const getPaymentsByMonth = (month: number, year: number) => {
    try {
      return payments.filter(payment => {
        if (!payment.nextDueDate) return false;
        
        const dueDate = new Date(payment.nextDueDate);
        return dueDate.getMonth() === month && 
               dueDate.getFullYear() === year && 
               payment.isActive;
      });
    } catch (error) {
      console.error('usePayments - Error getting payments by month:', error);
      return [];
    }
  };

  // Función de utilidad para depurar problemas de localStorage
  const debugStorage = () => {
    try {
      const savedData = localStorage.getItem(STORAGE_KEY);
      console.log('Current localStorage data:', savedData);
      return { success: true, data: savedData };
    } catch (error) {
      console.error('Error accessing localStorage:', error);
      return { success: false, error };
    }
  };

  return {
    payments,
    addPayment,
    updatePayment,
    deletePayment,
    markPaymentAsPaid,
    cancelPayment,
    resumePayment,
    getUpcomingPayments,
    getPaymentsByMonth,
    debugStorage,
  };
};
