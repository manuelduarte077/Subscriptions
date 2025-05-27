import { toast } from 'sonner';

// Definir el tipo de pago
export interface Payment {
  id: string;
  name: string;
  amount: number;
  dueDate: string;
  category: string;
  description?: string;
  isPaid: boolean;
  userId?: string;
}

// Clave para almacenar los pagos en localStorage
const PAYMENTS_STORAGE_KEY = 'payments';

// Obtener todos los pagos
export const getPayments = (userId?: string): Payment[] => {
  try {
    const storedPayments = localStorage.getItem(PAYMENTS_STORAGE_KEY);
    if (!storedPayments) return [];
    
    const payments: Payment[] = JSON.parse(storedPayments);
    
    // Si hay un userId, filtrar solo los pagos de ese usuario
    if (userId) {
      return payments.filter(payment => payment.userId === userId);
    }
    
    // Si no hay userId, devolver pagos sin userId (para compatibilidad con versiones anteriores)
    return payments.filter(payment => !payment.userId);
  } catch (error) {
    console.error('Error al obtener pagos:', error);
    return [];
  }
};

// Guardar un nuevo pago
export const savePayment = (payment: Omit<Payment, 'id'>, userId?: string): Payment => {
  try {
    const payments = getPayments();
    
    const newPayment: Payment = {
      ...payment,
      id: Date.now().toString(),
      userId: userId,
    };
    
    const updatedPayments = [...payments, newPayment];
    localStorage.setItem(PAYMENTS_STORAGE_KEY, JSON.stringify(updatedPayments));
    
    toast.success('Pago guardado correctamente');
    return newPayment;
  } catch (error) {
    console.error('Error al guardar pago:', error);
    toast.error('Error al guardar pago');
    throw error;
  }
};

// Actualizar un pago existente
export const updatePayment = (payment: Payment): Payment => {
  try {
    const payments = getPayments();
    const paymentIndex = payments.findIndex(p => p.id === payment.id);
    
    if (paymentIndex === -1) {
      throw new Error('Pago no encontrado');
    }
    
    payments[paymentIndex] = payment;
    localStorage.setItem(PAYMENTS_STORAGE_KEY, JSON.stringify(payments));
    
    toast.success('Pago actualizado correctamente');
    return payment;
  } catch (error) {
    console.error('Error al actualizar pago:', error);
    toast.error('Error al actualizar pago');
    throw error;
  }
};

// Eliminar un pago
export const deletePayment = (paymentId: string): void => {
  try {
    const payments = getPayments();
    const updatedPayments = payments.filter(payment => payment.id !== paymentId);
    
    localStorage.setItem(PAYMENTS_STORAGE_KEY, JSON.stringify(updatedPayments));
    
    toast.success('Pago eliminado correctamente');
  } catch (error) {
    console.error('Error al eliminar pago:', error);
    toast.error('Error al eliminar pago');
    throw error;
  }
};

// Marcar un pago como pagado o no pagado
export const togglePaymentStatus = (paymentId: string): Payment => {
  try {
    const payments = getPayments();
    const paymentIndex = payments.findIndex(p => p.id === paymentId);
    
    if (paymentIndex === -1) {
      throw new Error('Pago no encontrado');
    }
    
    payments[paymentIndex].isPaid = !payments[paymentIndex].isPaid;
    localStorage.setItem(PAYMENTS_STORAGE_KEY, JSON.stringify(payments));
    
    toast.success(`Pago marcado como ${payments[paymentIndex].isPaid ? 'pagado' : 'no pagado'}`);
    return payments[paymentIndex];
  } catch (error) {
    console.error('Error al cambiar estado del pago:', error);
    toast.error('Error al cambiar estado del pago');
    throw error;
  }
};

// Sincronizar pagos con la cuenta de usuario
export const syncPaymentsWithUser = (userId: string): void => {
  try {
    const payments = getPayments();
    
    // Asignar el userId a todos los pagos que no tienen userId
    const updatedPayments = payments.map(payment => {
      if (!payment.userId) {
        return { ...payment, userId };
      }
      return payment;
    });
    
    localStorage.setItem(PAYMENTS_STORAGE_KEY, JSON.stringify(updatedPayments));
    
    toast.success('Pagos sincronizados con tu cuenta');
  } catch (error) {
    console.error('Error al sincronizar pagos:', error);
    toast.error('Error al sincronizar pagos');
    throw error;
  }
};
