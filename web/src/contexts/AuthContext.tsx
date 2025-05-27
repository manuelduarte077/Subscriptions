import { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'sonner';
import { syncPaymentsWithUser } from '@/services/PaymentService';
import { 
  createUserWithEmailAndPassword, 
  signInWithEmailAndPassword, 
  signOut, 
  onAuthStateChanged,
  updateProfile,
  User as FirebaseUser
} from 'firebase/auth';
import { auth } from '@/lib/firebase';

// Definir el tipo de usuario
export interface User {
  id: string;
  email: string | null;
  name: string | null;
  photoURL?: string | null;
}

// Definir la interfaz del contexto
interface AuthContextType {
  user: User | null;
  loading: boolean;
  login: (email: string, password: string) => Promise<boolean>;
  register: (name: string, email: string, password: string) => Promise<boolean>;
  logout: () => void;
  isAuthenticated: boolean;
}

// Crear el contexto
const AuthContext = createContext<AuthContextType | undefined>(undefined);

// Hook personalizado para usar el contexto
export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth debe ser usado dentro de un AuthProvider');
  }
  return context;
};

// Proveedor del contexto
interface AuthProviderProps {
  children: ReactNode;
}

export const AuthProvider = ({ children }: AuthProviderProps) => {
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  // Escuchar cambios en el estado de autenticación de Firebase
  useEffect(() => {
    const unsubscribe = onAuthStateChanged(auth, (firebaseUser) => {
      if (firebaseUser) {
        // Usuario autenticado
        const userData: User = {
          id: firebaseUser.uid,
          email: firebaseUser.email,
          name: firebaseUser.displayName,
          photoURL: firebaseUser.photoURL
        };
        setUser(userData);
      } else {
        // Usuario no autenticado
        setUser(null);
      }
      setLoading(false);
    });

    // Limpiar el listener cuando el componente se desmonte
    return () => unsubscribe();
  }, []);

  // Función para iniciar sesión con Firebase
  const login = async (email: string, password: string): Promise<boolean> => {
    try {
      setLoading(true);
      
      // Iniciar sesión con Firebase Authentication
      const userCredential = await signInWithEmailAndPassword(auth, email, password);
      const firebaseUser = userCredential.user;
      
      // Sincronizar pagos con el usuario
      syncPaymentsWithUser(firebaseUser.uid);
      
      toast.success('Inicio de sesión exitoso');
      return true;
    } catch (error: any) {
      console.error('Error al iniciar sesión:', error);
      
      // Mostrar mensajes de error específicos
      if (error.code === 'auth/user-not-found' || error.code === 'auth/wrong-password') {
        toast.error('Credenciales incorrectas');
      } else if (error.code === 'auth/too-many-requests') {
        toast.error('Demasiados intentos fallidos. Intenta más tarde');
      } else {
        toast.error('Error al iniciar sesión');
      }
      
      return false;
    } finally {
      setLoading(false);
    }
  };

  // Función para registrar un nuevo usuario con Firebase
  const register = async (name: string, email: string, password: string): Promise<boolean> => {
    try {
      setLoading(true);
      
      // Crear usuario en Firebase Authentication
      const userCredential = await createUserWithEmailAndPassword(auth, email, password);
      const firebaseUser = userCredential.user;
      
      // Actualizar el perfil del usuario con su nombre
      await updateProfile(firebaseUser, {
        displayName: name
      });
      
      // Sincronizar pagos con el usuario
      syncPaymentsWithUser(firebaseUser.uid);
      
      toast.success('Registro exitoso');
      return true;
    } catch (error: any) {
      console.error('Error al registrar:', error);
      
      // Mostrar mensajes de error específicos
      if (error.code === 'auth/email-already-in-use') {
        toast.error('El correo electrónico ya está en uso');
      } else if (error.code === 'auth/weak-password') {
        toast.error('La contraseña es demasiado débil');
      } else if (error.code === 'auth/invalid-email') {
        toast.error('Correo electrónico inválido');
      } else {
        toast.error('Error al registrar usuario');
      }
      
      return false;
    } finally {
      setLoading(false);
    }
  };

  // Función para cerrar sesión con Firebase
  const logout = async () => {
    try {
      await signOut(auth);
      navigate('/login');
      toast.success('Sesión cerrada');
    } catch (error) {
      console.error('Error al cerrar sesión:', error);
      toast.error('Error al cerrar sesión');
    }
  };

  const value = {
    user,
    loading,
    login,
    register,
    logout,
    isAuthenticated: !!user,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
