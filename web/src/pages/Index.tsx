
import { useState } from 'react';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import Dashboard from '@/components/Dashboard';
import PaymentForm from '@/components/PaymentForm';
import PaymentCalendar from '@/components/PaymentCalendar';
import PaymentList from '@/components/PaymentList';
import { Home, Plus, Calendar, List } from 'lucide-react';

const Index = () => {
  const [activeTab, setActiveTab] = useState('dashboard');

  const handleAddPaymentSuccess = () => {
    setActiveTab('dashboard');
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-green-50">
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
            <Dashboard onAddPayment={() => setActiveTab('add')} />
          </TabsContent>

          <TabsContent value="add">
            <PaymentForm onSuccess={handleAddPaymentSuccess} />
          </TabsContent>

          <TabsContent value="calendar">
            <PaymentCalendar />
          </TabsContent>

          <TabsContent value="list">
            <PaymentList />
          </TabsContent>
        </Tabs>
      </div>
    </div>
  );
};

export default Index;
