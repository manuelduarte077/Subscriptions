
import { useState, useEffect } from 'react';
import { Dialog, DialogContent, DialogHeader, DialogTitle } from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Checkbox } from '@/components/ui/checkbox';
import { Badge } from '@/components/ui/badge';
import { Calendar } from '@/components/ui/calendar';
import { Popover, PopoverContent, PopoverTrigger } from '@/components/ui/popover';
import { CalendarIcon, CreditCard, Check, X, Play } from 'lucide-react';
import { format } from 'date-fns';
import { es } from 'date-fns/locale';
import { cn } from '@/lib/utils';
import { Payment } from '@/types/payment';
import { usePayments } from '@/hooks/usePayments';
import { formatCurrency } from '@/utils/paymentUtils';
import { toast } from '@/hooks/use-toast';

interface PaymentEditDialogProps {
  payment: Payment | null;
  open: boolean;
  onOpenChange: (open: boolean) => void;
}

const PaymentEditDialog = ({ payment, open, onOpenChange }: PaymentEditDialogProps) => {
  const { markPaymentAsPaid, cancelPayment, resumePayment, updatePayment, refreshPayments } = usePayments();
  const [cardLastFour, setCardLastFour] = useState('');
  const [paymentDate, setPaymentDate] = useState<Date>(new Date());
  const [resumeDate, setResumeDate] = useState<Date>(new Date());
  const [showPaymentDatePicker, setShowPaymentDatePicker] = useState(false);
  
  const [isPaymentDue, setIsPaymentDue] = useState(false);

  useEffect(() => {
    if (payment) {
      const today = new Date();
      const dueDate = new Date(payment.nextDueDate);
    
      setIsPaymentDue(today >= dueDate);
    }
  }, [payment]);

  if (!payment) return null;

  const handleMarkAsPaid = () => {
    markPaymentAsPaid(payment.id, cardLastFour, showPaymentDatePicker ? paymentDate : undefined);
    toast({
      title: "Pago registrado",
      description: `${payment.name} ha sido marcado como pagado`,
    });
    onOpenChange(false);
  };

  const handleCancel = () => {
    cancelPayment(payment.id);
    toast({
      title: "Suscripción cancelada",
      description: `${payment.name} ha sido cancelado`,
      variant: "destructive",
    });
    onOpenChange(false);
  };

  const handleResume = () => {
    resumePayment(payment.id, resumeDate);
    toast({
      title: "Suscripción reanudada",
      description: `${payment.name} ha sido reactivado`,
    });
    onOpenChange(false);
  };

  const handleUpdateCard = () => {
    if (cardLastFour.length === 4) {
      updatePayment(payment.id, { cardLastFour });
      toast({
        title: "Tarjeta actualizada",
        description: "Los datos de la tarjeta han sido actualizados",
      });
    }
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-2xl">
        <DialogHeader>
          <DialogTitle className="flex items-center gap-2">
            <CreditCard className="w-5 h-5" />
            Gestionar Pago: {payment.name}
          </DialogTitle>
        </DialogHeader>

        <div className="space-y-6">
          {/* Payment Info */}
          <div className="grid grid-cols-2 gap-4 p-4 bg-muted rounded-lg">
            <div>
              <Label className="text-sm text-muted-foreground">Proveedor</Label>
              <p className="font-medium">{payment.provider || 'No especificado'}</p>
            </div>
            <div>
              <Label className="text-sm text-muted-foreground">Monto</Label>
              <p className="font-medium">{formatCurrency(payment.amount)}</p>
            </div>
            <div>
              <Label className="text-sm text-muted-foreground">Próximo pago</Label>
              <p className="font-medium">
                {format(payment.nextDueDate, 'dd MMM yyyy', { locale: es })}
              </p>
            </div>
            <div>
              <Label className="text-sm text-muted-foreground">Estado</Label>
              <Badge variant={payment.isActive ? 'default' : 'secondary'}>
                {payment.isActive ? 'Activo' : 'Cancelado'}
              </Badge>
            </div>
          </div>

          {/* Card Information */}
          <div className="space-y-3">
            <Label>Últimos 4 dígitos de la tarjeta</Label>
            <div className="flex gap-2">
              <Input
                placeholder="1234"
                value={cardLastFour}
                onChange={(e) => setCardLastFour(e.target.value.slice(0, 4))}
                maxLength={4}
                className="w-24"
              />
              {payment.cardLastFour && (
                <Badge variant="outline">
                  Actual: •••• {payment.cardLastFour}
                </Badge>
              )}
              <Button 
                variant="outline" 
                size="sm" 
                onClick={handleUpdateCard}
                disabled={cardLastFour.length !== 4}
              >
                Actualizar
              </Button>
            </div>
          </div>

          {/* Fecha de pago */}
          <div className="space-y-2">
            <div className="flex items-center space-x-2">
              <Checkbox 
                id="showPaymentDatePicker" 
                checked={showPaymentDatePicker}
                onCheckedChange={(checked) => 
                  setShowPaymentDatePicker(checked === true)
                }
              />
              <Label htmlFor="showPaymentDatePicker" className="cursor-pointer">
                Especificar fecha de pago diferente
              </Label>
            </div>
            
            {showPaymentDatePicker && (
              <div className="mt-2">
                <Label>Fecha de pago</Label>
                <Popover>
                  <PopoverTrigger asChild>
                    <Button
                      variant="outline"
                      className={cn(
                        "w-full justify-start text-left font-normal mt-1",
                        !paymentDate && "text-muted-foreground"
                      )}
                    >
                      <CalendarIcon className="mr-2 h-4 w-4" />
                      {paymentDate ? (
                        format(paymentDate, "PPP", { locale: es })
                      ) : (
                        <span>Seleccionar fecha</span>
                      )}
                    </Button>
                  </PopoverTrigger>
                  <PopoverContent className="w-auto p-0" align="start">
                    <Calendar
                      mode="single"
                      selected={paymentDate}
                      onSelect={(date) => date && setPaymentDate(date)}
                      initialFocus
                    />
                  </PopoverContent>
                </Popover>
                <p className="text-xs text-muted-foreground mt-1">
                  Útil para registrar pagos de meses anteriores
                </p>
              </div>
            )}
          </div>

          {/* Actions */}
          {payment.isActive ? (
            <div className="flex gap-3 flex-col">
              {isPaymentDue ? (
                <Button 
                  onClick={handleMarkAsPaid} 
                  className="w-full"
                  variant="default"
                >
                  <Check className="w-4 h-4 mr-2" />
                  Marcar como Pagado
                </Button>
              ) : (
                <div className="bg-muted p-3 rounded-md mb-2">
                  <p className="text-sm text-center text-muted-foreground">
                    El pago estará disponible a partir del {format(payment.nextDueDate, "d 'de' MMMM", { locale: es })}
                  </p>
                </div>
              )}
              <Button 
                onClick={handleCancel} 
                className="w-full"
                variant="destructive"
              >
                <X className="w-4 h-4 mr-2" />
                Cancelar Suscripción
              </Button>
            </div>
          ) : (
            <div className="space-y-3">
              <Label>Fecha para reanudar</Label>
              <Popover>
                <PopoverTrigger asChild>
                  <Button
                    variant="outline"
                    className={cn(
                      "w-full justify-start text-left font-normal",
                      !resumeDate && "text-muted-foreground"
                    )}
                  >
                    <CalendarIcon className="mr-2 h-4 w-4" />
                    {resumeDate ? (
                      format(resumeDate, "PPP", { locale: es })
                    ) : (
                      <span>Seleccionar fecha</span>
                    )}
                  </Button>
                </PopoverTrigger>
                <PopoverContent className="w-auto p-0" align="start">
                  <Calendar
                    mode="single"
                    selected={resumeDate}
                    onSelect={(date) => date && setResumeDate(date)}
                    initialFocus
                  />
                </PopoverContent>
              </Popover>
              <Button onClick={handleResume} className="w-full">
                <Play className="w-4 h-4 mr-2" />
                Reanudar Suscripción
              </Button>
            </div>
          )}

          {/* Payment History */}
          {payment.paymentHistory.length > 0 && (
            <div className="space-y-3">
              <Label className="text-lg font-semibold">Historial de Pagos</Label>
              <div className="max-h-40 overflow-y-auto space-y-2">
                {payment.paymentHistory
                  .sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime())
                  .map((entry) => (
                  <div 
                    key={entry.id} 
                    className="flex items-center justify-between p-3 border rounded-lg"
                  >
                    <div className="flex items-center gap-3">
                      <Badge 
                        variant={entry.status === 'paid' ? 'default' : 'destructive'}
                      >
                        {entry.status === 'paid' ? 'Pagado' : 'Cancelado'}
                      </Badge>
                      <span className="text-sm">
                        {format(entry.date, 'dd MMM yyyy', { locale: es })}
                      </span>
                      {entry.cardLastFour && (
                        <span className="text-sm text-muted-foreground">
                          •••• {entry.cardLastFour}
                        </span>
                      )}
                    </div>
                    <span className="font-medium">
                      {formatCurrency(entry.amount)}
                    </span>
                  </div>
                ))}
              </div>
            </div>
          )}
        </div>
      </DialogContent>
    </Dialog>
  );
};

export default PaymentEditDialog;
