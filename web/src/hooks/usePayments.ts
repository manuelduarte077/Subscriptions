import { useState, useEffect } from 'react';
import { Payment, PaymentFormData, PaymentHistoryEntry, PaymentServiceType, PaymentCategory } from '@/types/payment';
import { useAuth } from '@/contexts/AuthContext';
import { getPayments, savePayment, updatePayment as updatePaymentService, deletePayment as deletePaymentService, togglePaymentStatus } from '@/services/PaymentService';

// Clave constante para localStorage (para retrocompatibilidad)
const STORAGE_KEY = 'payment-reminders';

export const usePayments = () => {
  const [payments, setPayments] = useState<Payment[]>([]);
  const { user } = useAuth();

  // Cargar pagos del servicio
  useEffect(() => {
    const loadPayments = async () => {
      console.log('usePayments - Loading payments');
      try {
        // Si el usuario está autenticado, obtener sus pagos
        const userPayments = await getPayments(user?.id);
        console.log('usePayments - Loaded payments:', userPayments);
        
        // Convertir las fechas de string a Date
        const paymentsWithDates = userPayments.map((payment: any) => {
          try {
            return {
              ...payment,
              nextDueDate: payment.nextDueDate ? new Date(payment.nextDueDate) : new Date(),
              createdAt: payment.createdAt ? new Date(payment.createdAt) : new Date(),
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
        }).filter(Boolean); // Eliminar entradas nulas
        
        setPayments(paymentsWithDates);
        
        // Para compatibilidad con versiones anteriores, migrar datos del antiguo localStorage
        await migrateOldData();
      } catch (error) {
        console.error('usePayments - Error loading payments:', error);
      }
    };
    
    loadPayments();
  }, [user?.id]);
  
  // Función para migrar datos del antiguo formato al nuevo
  const migrateOldData = async () => {
    try {
      const oldData = localStorage.getItem(STORAGE_KEY);
      if (oldData) {
        const oldPayments = JSON.parse(oldData);
        if (Array.isArray(oldPayments) && oldPayments.length > 0) {
          console.log('usePayments - Migrating old data to new format');
          
          // Convertir y guardar cada pago antiguo en el nuevo formato
          for (const oldPayment of oldPayments) {
            const newPayment = {
              name: oldPayment.name,
              amount: oldPayment.amount,
              dueDate: oldPayment.nextDueDate,
              category: oldPayment.category || 'other',
              provider: oldPayment.provider || '', // Incluir el proveedor
              description: oldPayment.description || '',
              isPaid: !oldPayment.isActive,
              userId: user?.id
            };
            
            await savePayment(newPayment, user?.id);
          }
          
          // Eliminar los datos antiguos después de migrarlos
          localStorage.removeItem(STORAGE_KEY);
        }
      }
    } catch (error) {
      console.error('usePayments - Error migrating old data:', error);
    }
  };

  // Ya no necesitamos guardar en localStorage en cada cambio, el servicio se encarga de eso

  const addPayment = async (paymentData: PaymentFormData) => {
    try {
      console.log('usePayments - Adding new payment:', paymentData);
      
      // Convertir del formato de formulario al formato del servicio
      const newPaymentData = {
        name: paymentData.name,
        amount: paymentData.amount,
        dueDate: paymentData.nextDueDate.toISOString(),
        startDate: paymentData.startDate.toISOString(), // Fecha de inicio de la suscripción
        category: paymentData.category,
        provider: paymentData.provider, // Incluir el proveedor
        description: paymentData.description || '',
        isPaid: paymentData.isPaid || false, // Estado de pago inicial
      };
      
      // Guardar el pago usando el servicio
      const savedPayment = await savePayment(newPaymentData, user?.id);
      
      // Actualizar el estado local
      setPayments(prev => {
        const newPayment: Payment = {
          id: savedPayment.id,
          name: savedPayment.name,
          amount: savedPayment.amount,
          nextDueDate: new Date(savedPayment.dueDate || new Date().toISOString()),
          dueDate: savedPayment.dueDate,
          provider: paymentData.provider || '',
          frequency: paymentData.frequency || 'monthly',
          category: (paymentData.category || 'other') as PaymentCategory,
          createdAt: new Date(),
          paymentHistory: [],
          isActive: true,
          isPaid: savedPayment.isPaid || false
        };
        return [...prev, newPayment];
      });
    } catch (error) {
      console.error('usePayments - Error adding payment:', error);
      throw error;
    }
  };

  const updatePayment = async (id: string, updatedData: Partial<Payment>) => {
    try {
      // Encontrar el pago existente
      const existingPayment = payments.find(p => p.id === id);
      if (!existingPayment) {
        throw new Error('Pago no encontrado');
      }
      
      // Crear el pago actualizado
      const updatedPayment = {
        ...existingPayment,
        ...updatedData,
      };
      
      // Convertir a formato de servicio
      const servicePayment: PaymentServiceType = {
        ...updatedPayment,
        dueDate: updatedPayment.nextDueDate?.toISOString() || new Date().toISOString()
      };
      
      // Actualizar en el servicio
      await updatePaymentService(servicePayment as any);
      
      // Actualizar el estado local
      setPayments(prev => 
        prev.map(payment => 
          payment.id === id ? updatedPayment : payment
        )
      );
    } catch (error) {
      console.error('usePayments - Error updating payment:', error);
      throw error;
    }
  };

  const deletePayment = async (id: string) => {
    try {
      // Eliminar del servicio
      await deletePaymentService(id, user?.id);
      
      // Actualizar el estado local
      setPayments(prev => prev.filter(payment => payment.id !== id));
    } catch (error) {
      console.error('usePayments - Error deleting payment:', error);
      throw error;
    }
  };

  const markPaymentAsPaid = async (id: string, cardLastFour?: string, paymentDate?: Date) => {
    try {
      console.log('usePayments - Marking payment as paid:', id);
      
      // Buscar el pago en el estado local
      const payment = payments.find(p => p.id === id);
      if (!payment) {
        throw new Error('Pago no encontrado');
      }
      
      // Usar la fecha proporcionada o la fecha actual
      const paidDate = paymentDate || new Date();
      
      // Marcar como pagado en el servicio
      const servicePayment = await togglePaymentStatus(id, user?.id);
      
      // Crear una entrada en el historial de pagos
      const historyEntry: PaymentHistoryEntry = {
        id: crypto.randomUUID(),
        date: paidDate,
        status: 'paid',
        amount: payment.amount,
        cardLastFour
      };
      
      // Actualizar el estado local con lógica adicional para la próxima fecha
      setPayments(prev => 
        prev.map(p => {
          if (p.id === id) {

            // Calcular la próxima fecha de pago basada en la frecuencia
            const nextDue = new Date(p.nextDueDate);
            
            switch (p.frequency) {
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
                  ...p,
                  isActive: false,
                  isPaid: true,
                  paymentHistory: [...p.paymentHistory, historyEntry],
                  cardLastFour: cardLastFour || p.cardLastFour
                };
            }

            return {
              ...p,
              nextDueDate: nextDue,
              isPaid: servicePayment.isPaid,
              paymentHistory: [...p.paymentHistory, historyEntry],
              cardLastFour: cardLastFour || p.cardLastFour
            };
          }
          return p;
        })
      );
    } catch (error) {
      console.error('usePayments - Error marking payment as paid:', error);
      throw error;
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
