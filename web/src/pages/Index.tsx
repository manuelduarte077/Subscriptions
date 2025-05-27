import { useState, useEffect } from 'react';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import Dashboard from '@/components/Dashboard';
import PaymentForm from '@/components/PaymentForm';
import PaymentCalendar from '@/components/PaymentCalendar';
import PaymentList from '@/components/PaymentList';
import Navbar from '@/components/Navbar';
import { Home, Plus, Calendar, List } from 'lucide-react';
import { useAuth } from '@/contexts/AuthContext';

const Index = () => {
  const [activeTab, setActiveTab] = useState('dashboard');
  const [refreshKey, setRefreshKey] = useState(0); // Estado para forzar actualización

  const handleAddPaymentSuccess = () => {
    setActiveTab('dashboard');
    setRefreshKey(prevKey => prevKey + 1); // Incrementar la clave para forzar actualización
  };
  
  // Forzar actualización cuando se cambia de pestaña
  useEffect(() => {
    setRefreshKey(prevKey => prevKey + 1);
  }, [activeTab]);

  const { user } = useAuth();
  
  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-green-50">
      <Navbar />
      <div className="container mx-auto px-4 py-8">
        <Tabs value={activeTab} onValueChange={setActiveTab} className="w-full">
          <TabsList className="grid grid-cols-4 w-full max-w-2xl mx-auto mb-8">
            <TabsTrigger value="dashboard" className="flex items-center gap-2">
              <Home className="w-4 h-4" />
              <span className="hidden sm:inline">Inicio</span>
            </TabsTrigger>
            <TabsTrigger value="add" className="flex items-center gap-2">
              <Plus className="w-4 h-4" />
              <span className="hidden sm:inline">Agregar</span>
            </TabsTrigger>
            <TabsTrigger value="calendar" className="flex items-center gap-2">
              <Calendar className="w-4 h-4" />
              <span className="hidden sm:inline">Calendario</span>
            </TabsTrigger>
            <TabsTrigger value="list" className="flex items-center gap-2">
              <List className="w-4 h-4" />
              <span className="hidden sm:inline">Lista</span>
            </TabsTrigger>
          </TabsList>

          <TabsContent value="dashboard">
            <Dashboard key={`dashboard-${refreshKey}`} onAddPayment={() => setActiveTab('add')} />
          </TabsContent>

          <TabsContent value="add">
            <PaymentForm key={`form-${refreshKey}`} onSuccess={handleAddPaymentSuccess} />
          </TabsContent>

          <TabsContent value="calendar">
            <PaymentCalendar key={`calendar-${refreshKey}`} />
          </TabsContent>

          <TabsContent value="list">
            <PaymentList key={`list-${refreshKey}`} />
          </TabsContent>
        </Tabs>
      </div>
    </div>
  );
};

export default Index;
