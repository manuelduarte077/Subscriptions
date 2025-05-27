import { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'sonner';
import { syncPaymentsWithUser } from '@/services/PaymentService';

// Definir el tipo de usuario
export interface User {
  id: string;
  email: string;
  name: string;
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

  // Verificar si hay un usuario almacenado al cargar
  useEffect(() => {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      try {
        setUser(JSON.parse(storedUser));
      } catch (error) {
        console.error('Error al parsear el usuario almacenado:', error);
        localStorage.removeItem('user');
      }
    }
    setLoading(false);
  }, []);

  // Función para iniciar sesión
  const login = async (email: string, password: string): Promise<boolean> => {
    try {
      setLoading(true);
      
      // Simulación de llamada a API - En un entorno real, esto sería una llamada a un backend
      // Esperar 1 segundo para simular la llamada
      await new Promise(resolve => setTimeout(resolve, 1000));
      
      // Verificar credenciales (simulado)
      if (email === 'demo@example.com' && password === 'password') {
        const userData: User = {
          id: '1',
          email: 'demo@example.com',
          name: 'Usuario Demo',
        };
        
        // Guardar usuario en localStorage
        localStorage.setItem('user', JSON.stringify(userData));
        setUser(userData);
        
        // Sincronizar pagos con el usuario
        syncPaymentsWithUser(userData.id);
        
        toast.success('Inicio de sesión exitoso');
        return true;
      } else {
        toast.error('Credenciales incorrectas');
        return false;
      }
    } catch (error) {
      console.error('Error al iniciar sesión:', error);
      toast.error('Error al iniciar sesión');
      return false;
    } finally {
      setLoading(false);
    }
  };

  // Función para registrar un nuevo usuario
  const register = async (name: string, email: string, password: string): Promise<boolean> => {
    try {
      setLoading(true);
      
      // Simulación de llamada a API
      await new Promise(resolve => setTimeout(resolve, 1000));
      
      // En un entorno real, aquí se enviarían los datos al backend
      const userData: User = {
        id: Date.now().toString(), // Generar un ID único
        email,
        name,
      };
      
      // Guardar usuario en localStorage
      localStorage.setItem('user', JSON.stringify(userData));
      setUser(userData);
      
      // Sincronizar pagos con el usuario
      syncPaymentsWithUser(userData.id);
      
      toast.success('Registro exitoso');
      return true;
    } catch (error) {
      console.error('Error al registrar:', error);
      toast.error('Error al registrar usuario');
      return false;
    } finally {
      setLoading(false);
    }
  };

  // Función para cerrar sesión
  const logout = () => {
    localStorage.removeItem('user');
    setUser(null);
    navigate('/login');
    toast.success('Sesión cerrada');
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
