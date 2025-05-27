import { useState } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { usePayments } from '@/hooks/usePayments';
import { formatCurrency, getDaysUntilDue, getPaymentUrgency } from '@/utils/paymentUtils';
import { Search, Edit, CreditCard } from 'lucide-react';
import { PaymentCategory, Payment } from '@/types/payment';
import PaymentEditDialog from './PaymentEditDialog';

const PaymentList = () => {
  const { payments } = usePayments();
  const [searchTerm, setSearchTerm] = useState('');
  const [categoryFilter, setCategoryFilter] = useState<PaymentCategory | 'all'>('all');
  const [sortBy, setSortBy] = useState<'dueDate' | 'amount' | 'name'>('dueDate');
  const [showInactive, setShowInactive] = useState(false);
  const [selectedPayment, setSelectedPayment] = useState<Payment | null>(null);
  const [editDialogOpen, setEditDialogOpen] = useState(false);

  console.log('PaymentList - Total payments:', payments.length);
  console.log('PaymentList - Payments data:', payments);

  // Filter and sort payments
  const filteredPayments = payments
    .filter(payment => {
      const matchesSearch = payment.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
                           payment.provider.toLowerCase().includes(searchTerm.toLowerCase());
      const matchesCategory = categoryFilter === 'all' || payment.category === categoryFilter;
      const matchesStatus = showInactive || payment.isActive;
      return matchesSearch && matchesCategory && matchesStatus;
    })
    .sort((a, b) => {
      switch (sortBy) {
        case 'dueDate':
          return new Date(a.nextDueDate).getTime() - new Date(b.nextDueDate).getTime();
        case 'amount':
          return b.amount - a.amount;
        case 'name':
          return a.name.localeCompare(b.name);
        default:
          return 0;
      }
    });

  const handleEdit = (payment: Payment) => {
    console.log('Editing payment:', payment);
    setSelectedPayment(payment);
    setEditDialogOpen(true);
  };

  const getCategoryLabel = (category: PaymentCategory) => {
    const labels = {
      streaming: 'Streaming',
      subscription: 'Suscripción',
      insurance: 'Seguro',
      utilities: 'Servicios',
      transportation: 'Transporte',
      housing: 'Vivienda',
      other: 'Otros',
    };
    return labels[category];
  };

  const getFrequencyLabel = (frequency: string) => {
    const labels = {
      monthly: 'Mensual',
      yearly: 'Anual',
      quarterly: 'Trimestral',
      weekly: 'Semanal',
      'one-time': 'Único',
    };
    return labels[frequency as keyof typeof labels] || frequency;
  };

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row gap-4">
        <div className="flex-1 relative">
          <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground w-4 h-4" />
          <Input
            placeholder="Buscar pagos..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="pl-10"
          />
        </div>
        
        <Select value={categoryFilter} onValueChange={(value: PaymentCategory | 'all') => setCategoryFilter(value)}>
          <SelectTrigger className="w-[180px]">
            <SelectValue placeholder="Categoría" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="all">Todas las categorías</SelectItem>
            <SelectItem value="streaming">Streaming</SelectItem>
            <SelectItem value="subscription">Suscripciones</SelectItem>
            <SelectItem value="insurance">Seguros</SelectItem>
            <SelectItem value="utilities">Servicios</SelectItem>
            <SelectItem value="transportation">Transporte</SelectItem>
            <SelectItem value="housing">Vivienda</SelectItem>
            <SelectItem value="other">Otros</SelectItem>
          </SelectContent>
        </Select>

        <Select value={sortBy} onValueChange={(value: 'dueDate' | 'amount' | 'name') => setSortBy(value)}>
          <SelectTrigger className="w-[150px]">
            <SelectValue placeholder="Ordenar por" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="dueDate">Fecha de pago</SelectItem>
            <SelectItem value="amount">Monto</SelectItem>
            <SelectItem value="name">Nombre</SelectItem>
          </SelectContent>
        </Select>

        <Button
          variant={showInactive ? "default" : "outline"}
          onClick={() => setShowInactive(!showInactive)}
          className="whitespace-nowrap"
        >
          {showInactive ? "Mostrar solo activos" : "Mostrar inactivos"}
        </Button>
      </div>

      <div className="grid gap-4">
        {filteredPayments.length === 0 ? (
          <Card>
            <CardContent className="text-center py-12">
              <div className="text-muted-foreground">
                <Search className="w-12 h-12 mx-auto mb-4 opacity-50" />
                {payments.length === 0 ? (
                  <>
                    <p>No tienes pagos registrados</p>
                    <p className="text-sm">Agrega tu primer pago usando el botón "Agregar"</p>
                  </>
                ) : (
                  <>
                    <p>No se encontraron pagos</p>
                    <p className="text-sm">Intenta ajustar los filtros de búsqueda</p>
                  </>
                )}
              </div>
            </CardContent>
          </Card>
        ) : (
          filteredPayments.map((payment) => {
            const daysUntil = getDaysUntilDue(payment.nextDueDate);
            const urgency = getPaymentUrgency(daysUntil);
            
            return (
              <Card key={payment.id} className={`payment-card ${!payment.isActive ? 'opacity-60' : ''}`}>
                <CardContent className="p-6">
                  <div className="flex items-start justify-between">
                    <div className="flex-1">
                      <div className="flex items-center gap-3 mb-2">
                        <h3 className="font-semibold text-lg">{payment.name}</h3>
                        <Badge variant="outline">
                          {getCategoryLabel(payment.category)}
                        </Badge>
                        {payment.isActive ? (
                          <Badge 
                            variant={
                              urgency === 'urgent' ? 'destructive' : 
                              urgency === 'upcoming' ? 'default' : 
                              'secondary'
                            }
                          >
                            {urgency === 'urgent' ? 'Urgente' : 
                             urgency === 'upcoming' ? 'Próximo' : 
                             'Normal'}
                          </Badge>
                        ) : (
                          <Badge variant="secondary">Cancelado</Badge>
                        )}
                      </div>
                      
                      <div className="grid grid-cols-1 md:grid-cols-3 gap-4 text-sm">
                        <div>
                          <span className="text-muted-foreground">Proveedor:</span>
                          <p className="font-medium">{payment.provider}</p>
                        </div>
                        <div>
                          <span className="text-muted-foreground">Frecuencia:</span>
                          <p className="font-medium">{getFrequencyLabel(payment.frequency)}</p>
                        </div>
                        <div>
                          <span className="text-muted-foreground">
                            {payment.isActive ? 'Próximo pago:' : 'Último pago:'}
                          </span>
                          <p className="font-medium">
                            {payment.nextDueDate.toLocaleDateString('es-ES')}
                            {payment.isActive && daysUntil === 0 && ' (Hoy)'}
                            {payment.isActive && daysUntil === 1 && ' (Mañana)'}
                            {payment.isActive && daysUntil > 1 && ` (${daysUntil} días)`}
                          </p>
                        </div>
                      </div>

                      {payment.cardLastFour && (
                        <div className="flex items-center gap-2 mt-3">
                          <CreditCard className="w-4 h-4 text-muted-foreground" />
                          <span className="text-sm text-muted-foreground">
                            •••• {payment.cardLastFour}
                          </span>
                        </div>
                      )}

                      {payment.paymentHistory.length > 0 && (
                        <div className="mt-3">
                          <span className="text-sm text-muted-foreground">
                            {payment.paymentHistory.filter(h => h.status === 'paid').length} pagos realizados
                          </span>
                        </div>
                      )}
                      
                      {payment.description && (
                        <p className="text-sm text-muted-foreground mt-3">
                          {payment.description}
                        </p>
                      )}
                    </div>
                    
                    <div className="flex items-center gap-4 ml-6">
                      <div className="text-right">
                        <div className="text-2xl font-bold">
                          {formatCurrency(payment.amount)}
                        </div>
                      </div>
                      
                      <div className="flex flex-col gap-2">
                        <Button 
                          variant="outline" 
                          size="sm"
                          onClick={() => handleEdit(payment)}
                        >
                          <Edit className="w-4 h-4" />
                        </Button>
                      </div>
                    </div>
                  </div>
                </CardContent>
              </Card>
            );
          })
        )}
      </div>

      <PaymentEditDialog
        payment={selectedPayment}
        open={editDialogOpen}
        onOpenChange={setEditDialogOpen}
      />
    </div>
  );
};

export default PaymentList;
