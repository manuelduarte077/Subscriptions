
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { usePayments } from '@/hooks/usePayments';
import { formatCurrency, getDaysUntilDue, getPaymentUrgency } from '@/utils/paymentUtils';
import { CalendarIcon, Plus } from 'lucide-react';
import { Button } from '@/components/ui/button';

interface DashboardProps {
  onAddPayment: () => void;
}

const Dashboard = ({ onAddPayment }: DashboardProps) => {
  const { payments, getUpcomingPayments } = usePayments();
  const upcomingPayments = getUpcomingPayments(30);
  
  const totalMonthlyAmount = payments
    .filter(p => p.frequency === 'monthly' && p.isActive)
    .reduce((sum, p) => sum + p.amount, 0);

  const urgentPayments = upcomingPayments.filter(p => getDaysUntilDue(p.nextDueDate) <= 3);

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
        <div>
          <h1 className="text-3xl font-bold bg-gradient-to-r from-blue-600 to-green-600 bg-clip-text text-transparent">
            Recordatorios de Pagos
          </h1>
          <p className="text-muted-foreground mt-1">
            Mantén el control de todas tus suscripciones y pagos recurrentes
          </p>
        </div>
        <Button onClick={onAddPayment} className="financial-gradient text-white">
          <Plus className="w-4 h-4 mr-2" />
          Agregar Pago
        </Button>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <Card className="payment-card">
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">
              Total Mensual
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-blue-600">
              {formatCurrency(totalMonthlyAmount)}
            </div>
            <p className="text-xs text-muted-foreground mt-1">
              Pagos mensuales activos
            </p>
          </CardContent>
        </Card>

        <Card className="payment-card">
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">
              Próximos Pagos
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-green-600">
              {upcomingPayments.length}
            </div>
            <p className="text-xs text-muted-foreground mt-1">
              En los próximos 30 días
            </p>
          </CardContent>
        </Card>

        <Card className="payment-card">
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">
              Pagos Urgentes
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-red-600">
              {urgentPayments.length}
            </div>
            <p className="text-xs text-muted-foreground mt-1">
              Vencen en 3 días o menos
            </p>
          </CardContent>
        </Card>
      </div>

      {/* Upcoming Payments */}
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <CalendarIcon className="w-5 h-5" />
            Próximos Pagos
          </CardTitle>
        </CardHeader>
        <CardContent>
          {upcomingPayments.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              <CalendarIcon className="w-12 h-12 mx-auto mb-4 opacity-50" />
              <p>No tienes pagos próximos</p>
              <p className="text-sm">¡Perfecto! Todos tus pagos están al día</p>
            </div>
          ) : (
            <div className="space-y-3">
              {upcomingPayments.slice(0, 5).map((payment) => {
                const daysUntil = getDaysUntilDue(payment.nextDueDate);
                const urgency = getPaymentUrgency(daysUntil);
                
                return (
                  <div
                    key={payment.id}
                    className="flex items-center justify-between p-4 border rounded-lg hover:bg-gray-50 transition-colors"
                  >
                    <div className="flex-1">
                      <div className="flex items-center gap-3">
                        <div className="font-medium">{payment.name}</div>
                        <Badge variant={urgency === 'urgent' ? 'destructive' : urgency === 'upcoming' ? 'default' : 'secondary'}>
                          {urgency === 'urgent' ? 'Urgente' : urgency === 'upcoming' ? 'Próximo' : 'Normal'}
                        </Badge>
                      </div>
                      <div className="text-sm text-muted-foreground">
                        {payment.provider} • {daysUntil === 0 ? 'Hoy' : daysUntil === 1 ? 'Mañana' : `En ${daysUntil} días`}
                      </div>
                    </div>
                    <div className="text-right">
                      <div className="font-semibold">{formatCurrency(payment.amount)}</div>
                      <div className="text-sm text-muted-foreground">
                        {payment.nextDueDate.toLocaleDateString('es-ES')}
                      </div>
                    </div>
                  </div>
                );
              })}
              {upcomingPayments.length > 5 && (
                <div className="text-center pt-4">
                  <Button variant="outline" size="sm">
                    Ver todos los pagos
                  </Button>
                </div>
              )}
            </div>
          )}
        </CardContent>
      </Card>
    </div>
  );
};

export default Dashboard;
