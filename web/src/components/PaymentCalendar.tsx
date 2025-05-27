
import { useState } from 'react';
import { Calendar } from '@/components/ui/calendar';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { usePayments } from '@/hooks/usePayments';
import { formatCurrency, getDaysUntilDue, getPaymentUrgency } from '@/utils/paymentUtils';
import { CalendarIcon, ChevronLeft, ChevronRight } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { cn } from '@/lib/utils';
import { format, startOfMonth, endOfMonth, eachDayOfInterval } from 'date-fns';
import { es } from 'date-fns/locale';

const PaymentCalendar = () => {
  const [selectedDate, setSelectedDate] = useState<Date>(new Date());
  const [currentMonth, setCurrentMonth] = useState<Date>(new Date());
  const { payments, getPaymentsByMonth } = usePayments();

  const monthPayments = getPaymentsByMonth(currentMonth.getMonth(), currentMonth.getFullYear());
  
  // Get payments for selected date
  const selectedDatePayments = selectedDate 
    ? payments.filter(payment => {
        if (!payment.nextDueDate) return false;
        
        // Asegurarse de que estamos comparando objetos Date correctamente
        const paymentDate = new Date(payment.nextDueDate);
        const selectedDateStr = selectedDate.toDateString();
        const paymentDateStr = paymentDate.toDateString();
        
        console.log('Comparing dates:', {
          paymentDate,
          selectedDate,
          paymentDateStr,
          selectedDateStr,
          match: paymentDateStr === selectedDateStr
        });
        
        return paymentDateStr === selectedDateStr && payment.isActive;
      })
    : [];

  // Create a map of dates with payments for styling
  const paymentsByDate = new Map();
  monthPayments.forEach(payment => {
    if (!payment.nextDueDate) return;
    
    // Asegurarse de que estamos trabajando con objetos Date correctos
    const paymentDate = new Date(payment.nextDueDate);
    const dateKey = paymentDate.toDateString();
    
    console.log('Adding payment to date map:', {
      payment: payment.name,
      paymentDate,
      dateKey
    });
    
    if (!paymentsByDate.has(dateKey)) {
      paymentsByDate.set(dateKey, []);
    }
    paymentsByDate.get(dateKey).push(payment);
  });

  const navigateMonth = (direction: 'prev' | 'next') => {
    const newMonth = new Date(currentMonth);
    if (direction === 'prev') {
      newMonth.setMonth(newMonth.getMonth() - 1);
    } else {
      newMonth.setMonth(newMonth.getMonth() + 1);
    }
    setCurrentMonth(newMonth);
  };

  const getDayStyle = (date: Date) => {
    const dateKey = date.toDateString();
    const dayPayments = paymentsByDate.get(dateKey) || [];
    
    if (dayPayments.length === 0) return '';
    
    // Get the most urgent payment for styling
    const urgencies = dayPayments.map(p => {
      const daysUntil = getDaysUntilDue(p.nextDueDate);
      return getPaymentUrgency(daysUntil);
    });
    
    if (urgencies.includes('urgent')) return 'urgent';
    if (urgencies.includes('upcoming')) return 'upcoming';
    return 'normal';
  };

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h2 className="text-2xl font-bold flex items-center gap-2">
          <CalendarIcon className="w-6 h-6" />
          Calendario de Pagos
        </h2>
        <div className="flex items-center gap-2">
          <Button
            variant="outline"
            size="sm"
            onClick={() => navigateMonth('prev')}
          >
            <ChevronLeft className="w-4 h-4" />
          </Button>
          <span className="font-medium min-w-[150px] text-center">
            {format(currentMonth, 'MMMM yyyy', { locale: es })}
          </span>
          <Button
            variant="outline"
            size="sm"
            onClick={() => navigateMonth('next')}
          >
            <ChevronRight className="w-4 h-4" />
          </Button>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Calendar */}
        <Card className="lg:col-span-2">
          <CardContent className="p-6">
            <Calendar
              mode="single"
              selected={selectedDate}
              onSelect={setSelectedDate}
              month={currentMonth}
              onMonthChange={setCurrentMonth}
              className="w-full"
              components={{
                Day: ({ date, displayMonth }) => {
                  const dateKey = date.toDateString();
                  const dayPayments = paymentsByDate.get(dateKey) || [];
                  const style = getDayStyle(date);
                  
                  return (
                    <button
                      className={cn(
                        'calendar-day relative h-9 w-9 p-0 font-normal hover:bg-accent hover:text-accent-foreground',
                        'inline-flex items-center justify-center whitespace-nowrap rounded-md text-sm',
                        'transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring',
                        'disabled:pointer-events-none disabled:opacity-50',
                        selectedDate && date.toDateString() === selectedDate.toDateString() && 
                        'bg-primary text-primary-foreground hover:bg-primary hover:text-primary-foreground',
                        style && `has-payment ${style}`,
                        dayPayments.length > 0 && 'font-semibold'
                      )}
                      onClick={() => setSelectedDate(date)}
                    >
                      {date.getDate()}
                      {dayPayments.length > 0 && (
                        <div className="absolute -top-1 -right-1 w-3 h-3 bg-white rounded-full flex items-center justify-center">
                          <span className="text-xs font-bold text-gray-800">
                            {dayPayments.length}
                          </span>
                        </div>
                      )}
                    </button>
                  );
                }
              }}
            />
            
            {/* Legend */}
            <div className="mt-4 flex flex-wrap gap-4 text-sm">
              <div className="flex items-center gap-2">
                <div className="w-4 h-4 rounded bg-red-500"></div>
                <span>Urgente (≤3 días)</span>
              </div>
              <div className="flex items-center gap-2">
                <div className="w-4 h-4 rounded bg-yellow-500"></div>
                <span>Próximo (4-7 días)</span>
              </div>
              <div className="flex items-center gap-2">
                <div className="w-4 h-4 rounded bg-green-500"></div>
                <span>Normal (+7 días)</span>
              </div>
            </div>
          </CardContent>
        </Card>

        {/* Selected Date Details */}
        <Card>
          <CardHeader>
            <CardTitle className="text-lg">
              {selectedDate 
                ? format(selectedDate, 'dd MMMM yyyy', { locale: es })
                : 'Selecciona una fecha'
              }
            </CardTitle>
          </CardHeader>
          <CardContent>
            {selectedDatePayments.length === 0 ? (
              <div className="text-center py-8 text-muted-foreground">
                <CalendarIcon className="w-12 h-12 mx-auto mb-4 opacity-50" />
                <p>No hay pagos programados</p>
                <p className="text-sm">para esta fecha</p>
              </div>
            ) : (
              <div className="space-y-4">
                {selectedDatePayments.map((payment) => {
                  const daysUntil = getDaysUntilDue(payment.nextDueDate);
                  const urgency = getPaymentUrgency(daysUntil);
                  
                  return (
                    <div
                      key={payment.id}
                      className="p-4 border rounded-lg space-y-2"
                    >
                      <div className="flex items-start justify-between">
                        <div className="flex-1">
                          <h4 className="font-medium">{payment.name}</h4>
                          <p className="text-sm text-muted-foreground">
                            {payment.provider}
                          </p>
                        </div>
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
                      </div>
                      
                      <div className="flex justify-between items-center">
                        <span className="font-semibold text-lg">
                          {formatCurrency(payment.amount)}
                        </span>
                        <span className="text-sm text-muted-foreground capitalize">
                          {payment.frequency}
                        </span>
                      </div>
                      
                      {payment.description && (
                        <p className="text-sm text-muted-foreground">
                          {payment.description}
                        </p>
                      )}
                    </div>
                  );
                })}
              </div>
            )}
          </CardContent>
        </Card>
      </div>
    </div>
  );
};

export default PaymentCalendar;
