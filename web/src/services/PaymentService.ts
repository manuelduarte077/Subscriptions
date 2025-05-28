import { toast } from 'sonner';
import { db } from '@/lib/firebase';
import { 
  collection, 
  doc, 
  addDoc, 
  updateDoc, 
  deleteDoc, 
  getDocs, 
  query, 
  where,
  serverTimestamp,
  Timestamp,
  setDoc
} from 'firebase/firestore';

// Definir el tipo de pago para el servicio
export interface Payment {
  id: string;
  name: string;
  amount: number;
  dueDate: string;
  startDate?: string; // Fecha de inicio de la suscripción
  category: string;
  description?: string;
  isPaid: boolean;
  userId?: string;
  // Campos adicionales para compatibilidad con la aplicación
  provider?: string;
  frequency?: string;
  nextDueDate?: Date;
  isActive?: boolean;
  createdAt?: Date;
  paymentHistory?: any[];
  cardLastFour?: string;
}

// Referencia a la colección de pagos en Firestore
const PAYMENTS_COLLECTION = 'payments';

// Clave para almacenar los pagos en localStorage (para compatibilidad con versiones anteriores)
const PAYMENTS_STORAGE_KEY = 'payments';

// Obtener todos los pagos
export const getPayments = async (userId?: string): Promise<Payment[]> => {
  try {
    // Si no hay userId, intentar obtener pagos del localStorage (para compatibilidad)
    if (!userId) {
      const storedPayments = localStorage.getItem(PAYMENTS_STORAGE_KEY);
      if (!storedPayments) return [];
      
      try {
        const payments: Payment[] = JSON.parse(storedPayments);
        return payments.filter(payment => !payment.userId);
      } catch (e) {
        console.error('Error al parsear pagos del localStorage:', e);
        return [];
      }
    }
    
    // Obtener pagos de Firestore
    const paymentsRef = collection(db, PAYMENTS_COLLECTION);
    const q = query(paymentsRef, where("userId", "==", userId));
    const querySnapshot = await getDocs(q);
    
    const payments: Payment[] = [];
    querySnapshot.forEach((doc) => {
      const data = doc.data();
      // Asegurarse de que todos los campos requeridos estén presentes
      const payment: Payment = {
        ...data,
        id: doc.id,
        name: data.name || '',
        amount: data.amount || 0,
        category: data.category || 'other',
        isPaid: data.isPaid || false,
        dueDate: data.dueDate,
        startDate: data.startDate, // Fecha de inicio de la suscripción
        // Campos necesarios para la compatibilidad con la aplicación
        provider: data.provider || '',
        frequency: data.frequency || 'monthly',
        nextDueDate: data.nextDueDate ? new Date(data.nextDueDate) : new Date(),
        isActive: data.isActive !== undefined ? data.isActive : !data.isPaid,
        // Convertir Timestamp a string si existe
        createdAt: data.createdAt instanceof Timestamp ? data.createdAt.toDate() : new Date(),
        paymentHistory: data.paymentHistory || []
      };
      payments.push(payment);
    });
    
    return payments;
  } catch (error) {
    console.error('Error al obtener pagos:', error);
    return [];
  }
};

// Guardar un nuevo pago
export const savePayment = async (payment: Omit<Payment, 'id'>, userId?: string): Promise<Payment> => {
  try {
    // Si no hay userId, guardar en localStorage (para compatibilidad)
    if (!userId) {
      const payments = await getPayments();
      
      const newPayment: Payment = {
        ...payment,
        id: Date.now().toString(),
        userId: undefined,
      };
      
      const updatedPayments = [...payments, newPayment];
      localStorage.setItem(PAYMENTS_STORAGE_KEY, JSON.stringify(updatedPayments));
      
      toast.success('Pago guardado correctamente');
      return newPayment;
    }
    
    // Guardar en Firestore
    const paymentsRef = collection(db, PAYMENTS_COLLECTION);
    const paymentData = {
      ...payment,
      userId,
      createdAt: serverTimestamp(),
      // Asegurar que isActive sea siempre true para nuevos pagos
      isActive: true
    };
    
    const docRef = await addDoc(paymentsRef, paymentData);
    
    const newPayment: Payment = {
      ...paymentData,
      id: docRef.id,
      createdAt: new Date(),
      // Asegurar que las fechas sean del tipo correcto
      startDate: payment.startDate,
      dueDate: payment.dueDate,
      nextDueDate: payment.nextDueDate,
      // Asegurar que isActive sea true para nuevos pagos
      isActive: true,
      isPaid: payment.isPaid || false
    };
    
    toast.success('Pago guardado correctamente');
    return newPayment;
  } catch (error) {
    console.error('Error al guardar pago:', error);
    toast.error('Error al guardar pago');
    throw error;
  }
};

// Actualizar un pago existente
export const updatePayment = async (payment: Payment): Promise<Payment> => {
  try {
    // Si no hay userId, actualizar en localStorage (para compatibilidad)
    if (!payment.userId) {
      const payments = await getPayments();
      const paymentIndex = payments.findIndex(p => p.id === payment.id);
      
      if (paymentIndex === -1) {
        throw new Error('Pago no encontrado');
      }
      
      payments[paymentIndex] = payment;
      localStorage.setItem(PAYMENTS_STORAGE_KEY, JSON.stringify(payments));
      
      toast.success('Pago actualizado correctamente');
      return payment;
    }
    
    // Actualizar en Firestore
    const paymentRef = doc(db, PAYMENTS_COLLECTION, payment.id);
    
    // Eliminar el id del objeto antes de actualizar
    const { id, ...paymentData } = payment;
    
    await updateDoc(paymentRef, {
      ...paymentData,
      updatedAt: serverTimestamp(),
    });
    
    toast.success('Pago actualizado correctamente');
    return payment;
  } catch (error) {
    console.error('Error al actualizar pago:', error);
    toast.error('Error al actualizar pago');
    throw error;
  }
};

// Eliminar un pago
export const deletePayment = async (paymentId: string, userId?: string): Promise<void> => {
  try {
    // Si no hay userId, eliminar de localStorage (para compatibilidad)
    if (!userId) {
      const payments = await getPayments();
      const updatedPayments = payments.filter(payment => payment.id !== paymentId);
      
      localStorage.setItem(PAYMENTS_STORAGE_KEY, JSON.stringify(updatedPayments));
      
      toast.success('Pago eliminado correctamente');
      return;
    }
    
    // Eliminar de Firestore
    const paymentRef = doc(db, PAYMENTS_COLLECTION, paymentId);
    await deleteDoc(paymentRef);
    
    toast.success('Pago eliminado correctamente');
  } catch (error) {
    console.error('Error al eliminar pago:', error);
    toast.error('Error al eliminar pago');
    throw error;
  }
};

// Marcar un pago como pagado o no pagado
export const togglePaymentStatus = async (paymentId: string, userId?: string): Promise<Payment> => {
  try {
    // Si no hay userId, actualizar en localStorage (para compatibilidad)
    if (!userId) {
      const payments = await getPayments();
      const paymentIndex = payments.findIndex(p => p.id === paymentId);
      
      if (paymentIndex === -1) {
        throw new Error('Pago no encontrado');
      }
      
      payments[paymentIndex].isPaid = !payments[paymentIndex].isPaid;
      localStorage.setItem(PAYMENTS_STORAGE_KEY, JSON.stringify(payments));
      
      toast.success(`Pago marcado como ${payments[paymentIndex].isPaid ? 'pagado' : 'no pagado'}`);
      return payments[paymentIndex];
    }
    
    // Obtener todos los pagos para encontrar el que necesitamos
    const allPayments = await getPayments(userId);
    const payment = allPayments.find(p => p.id === paymentId);
    
    if (!payment) {
      throw new Error('Pago no encontrado');
    }
    
    // Actualizar en Firestore
    const paymentRef = doc(db, PAYMENTS_COLLECTION, paymentId);
    const newStatus = !payment.isPaid;
    
    await updateDoc(paymentRef, {
      isPaid: newStatus,
      updatedAt: serverTimestamp(),
    });
    
    const updatedPayment = {
      ...payment,
      isPaid: newStatus,
    };
    
    toast.success(`Pago marcado como ${newStatus ? 'pagado' : 'no pagado'}`);
    return updatedPayment;
  } catch (error) {
    console.error('Error al cambiar estado del pago:', error);
    toast.error('Error al cambiar estado del pago');
    throw error;
  }
};

// Sincronizar pagos con la cuenta de usuario
export const syncPaymentsWithUser = async (userId: string): Promise<void> => {
  try {
    // Obtener pagos del localStorage
    const storedPayments = localStorage.getItem(PAYMENTS_STORAGE_KEY);
    if (!storedPayments) return;
    
    let localPayments: Payment[] = [];
    try {
      localPayments = JSON.parse(storedPayments);
    } catch (e) {
      console.error('Error al parsear pagos del localStorage:', e);
      return;
    }
    
    // Filtrar pagos sin userId
    const paymentsToSync = localPayments.filter(payment => !payment.userId);
    
    if (paymentsToSync.length === 0) {
      console.log('No hay pagos para sincronizar');
      return;
    }
    
    // Subir cada pago a Firestore
    const batch = [];
    for (const payment of paymentsToSync) {
      const { id, ...paymentData } = payment;
      
      // Crear un nuevo documento en Firestore
      const paymentWithUserId = {
        ...paymentData,
        userId,
        createdAt: serverTimestamp(),
      };
      
      batch.push(addDoc(collection(db, PAYMENTS_COLLECTION), paymentWithUserId));
    }
    
    // Esperar a que se completen todas las operaciones
    await Promise.all(batch);
    
    // Actualizar localStorage para marcar estos pagos como sincronizados
    const remainingPayments = localPayments.filter(payment => payment.userId);
    localStorage.setItem(PAYMENTS_STORAGE_KEY, JSON.stringify(remainingPayments));
    
    toast.success(`${paymentsToSync.length} pagos sincronizados con tu cuenta`);
  } catch (error) {
    console.error('Error al sincronizar pagos:', error);
    toast.error('Error al sincronizar pagos');
    throw error;
  }
};
