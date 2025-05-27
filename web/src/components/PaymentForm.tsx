import { useState } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { Textarea } from '@/components/ui/textarea';
import { Calendar } from '@/components/ui/calendar';
import { Popover, PopoverContent, PopoverTrigger } from '@/components/ui/popover';
import { CalendarIcon, CreditCard } from 'lucide-react';
import { format } from 'date-fns';
import { es } from 'date-fns/locale';
import { cn } from '@/lib/utils';
import { usePayments } from '@/hooks/usePayments';
import { PaymentFormData, PaymentCategory, PaymentFrequency } from '@/types/payment';
import { toast } from '@/hooks/use-toast';

interface PaymentFormProps {
  onSuccess: () => void;
}

const PaymentForm = ({ onSuccess }: PaymentFormProps) => {
  const { addPayment } = usePayments();
  const [isLoading, setIsLoading] = useState(false);
  const [formData, setFormData] = useState<PaymentFormData>({
    name: '',
    amount: 0,
    provider: '',
    category: 'subscription',
    frequency: 'monthly',
    nextDueDate: new Date(),
    description: '',
    cardLastFour: '',
  });

  const categoryOptions: { value: PaymentCategory; label: string }[] = [
    { value: 'streaming', label: 'Streaming (Netflix, HBO, etc.)' },
    { value: 'subscription', label: 'Suscripciones' },
    { value: 'insurance', label: 'Seguros' },
    { value: 'utilities', label: 'Servicios públicos' },
    { value: 'transportation', label: 'Transporte' },
    { value: 'housing', label: 'Vivienda' },
    { value: 'other', label: 'Otros' },
  ];

  const frequencyOptions: { value: PaymentFrequency; label: string }[] = [
    { value: 'monthly', label: 'Mensual' },
    { value: 'yearly', label: 'Anual' },
    { value: 'quarterly', label: 'Trimestral' },
    { value: 'weekly', label: 'Semanal' },
    { value: 'one-time', label: 'Pago único' },
  ];

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!formData.name || !formData.provider || formData.amount <= 0) {
      toast({
        title: "Error",
        description: "Por favor completa todos los campos requeridos",
        variant: "destructive",
      });
      return;
    }

    setIsLoading(true);
    
    try {
      addPayment(formData);
      
      toast({
        title: "¡Pago agregado!",
        description: `${formData.name} ha sido registrado exitosamente`,
      });
      
      // Reset form
      setFormData({
        name: '',
        amount: 0,
        provider: '',
        category: 'subscription',
        frequency: 'monthly',
        nextDueDate: new Date(),
        description: '',
        cardLastFour: '',
      });
      
      onSuccess();
    } catch (error) {
      toast({
        title: "Error",
        description: "No se pudo agregar el pago. Intenta de nuevo.",
        variant: "destructive",
      });
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Card className="max-w-2xl mx-auto">
      <CardHeader>
        <CardTitle className="text-2xl font-bold text-center">
          Agregar Nuevo Pago
        </CardTitle>
      </CardHeader>
      <CardContent>
        <form onSubmit={handleSubmit} className="space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="space-y-2">
              <Label htmlFor="name">Nombre del pago *</Label>
              <Input
                id="name"
                placeholder="ej. Netflix Premium"
                value={formData.name}
                onChange={(e) => setFormData(prev => ({ ...prev, name: e.target.value }))}
                required
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="provider">Proveedor *</Label>
              <Input
                id="provider"
                placeholder="ej. Netflix"
                value={formData.provider}
                onChange={(e) => setFormData(prev => ({ ...prev, provider: e.target.value }))}
                required
              />
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="space-y-2">
              <Label htmlFor="amount">Monto *</Label>
              <div className="relative">
                <span className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground">
                  $
                </span>
                <Input
                  id="amount"
                  type="number"
                  placeholder="0.00"
                  className="pl-8"
                  value={formData.amount || ''}
                  onChange={(e) => setFormData(prev => ({ ...prev, amount: parseFloat(e.target.value) || 0 }))}
                  min="0"
                  step="0.01"
                  required
                />
              </div>
            </div>

            <div className="space-y-2">
              <Label>Frecuencia *</Label>
              <Select
                value={formData.frequency}
                onValueChange={(value: PaymentFrequency) => 
                  setFormData(prev => ({ ...prev, frequency: value }))
                }
              >
                <SelectTrigger>
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  {frequencyOptions.map((option) => (
                    <SelectItem key={option.value} value={option.value}>
                      {option.label}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="space-y-2">
              <Label>Categoría</Label>
              <Select
                value={formData.category}
                onValueChange={(value: PaymentCategory) => 
                  setFormData(prev => ({ ...prev, category: value }))
                }
              >
                <SelectTrigger>
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  {categoryOptions.map((option) => (
                    <SelectItem key={option.value} value={option.value}>
                      {option.label}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>

            <div className="space-y-2">
              <Label>Próxima fecha de pago *</Label>
              <Popover>
                <PopoverTrigger asChild>
                  <Button
                    variant="outline"
                    className={cn(
                      "w-full justify-start text-left font-normal",
                      !formData.nextDueDate && "text-muted-foreground"
                    )}
                  >
                    <CalendarIcon className="mr-2 h-4 w-4" />
                    {formData.nextDueDate ? (
                      format(formData.nextDueDate, "PPP", { locale: es })
                    ) : (
                      <span>Seleccionar fecha</span>
                    )}
                  </Button>
                </PopoverTrigger>
                <PopoverContent className="w-auto p-0" align="start">
                  <Calendar
                    mode="single"
                    selected={formData.nextDueDate}
                    onSelect={(date) => 
                      date && setFormData(prev => ({ ...prev, nextDueDate: date }))
                    }
                    initialFocus
                    className="p-3 pointer-events-auto"
                  />
                </PopoverContent>
              </Popover>
            </div>
          </div>

          <div className="space-y-2">
            <Label htmlFor="cardLastFour">Últimos 4 dígitos de la tarjeta (opcional)</Label>
            <div className="relative">
              <CreditCard className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground w-4 h-4" />
              <Input
                id="cardLastFour"
                placeholder="1234"
                className="pl-10"
                value={formData.cardLastFour}
                onChange={(e) => setFormData(prev => ({ ...prev, cardLastFour: e.target.value.slice(0, 4) }))}
                maxLength={4}
              />
            </div>
          </div>

          <div className="space-y-2">
            <Label htmlFor="description">Descripción (opcional)</Label>
            <Textarea
              id="description"
              placeholder="Notas adicionales sobre este pago..."
              value={formData.description}
              onChange={(e) => setFormData(prev => ({ ...prev, description: e.target.value }))}
              rows={3}
            />
          </div>

          <div className="flex gap-4 pt-4">
            <Button 
              type="submit" 
              className="flex-1 financial-gradient text-white"
              disabled={isLoading}
            >
              {isLoading ? 'Agregando...' : 'Agregar Pago'}
            </Button>
            <Button 
              type="button" 
              variant="outline" 
              onClick={onSuccess}
              className="flex-1"
            >
              Cancelar
            </Button>
          </div>
        </form>
      </CardContent>
    </Card>
  );
};

export default PaymentForm;
