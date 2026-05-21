import { AlertCircle } from "lucide-react";

interface AlertaAccesoDenegadoProps {
  visible: boolean;
}

const AlertaAccesoDenegado = ({ visible }: AlertaAccesoDenegadoProps) => {
  if (!visible) return null;

  return (
    <div className="fixed top-4 right-4 z-50 animate-in slide-in-from-top-2 fade-in">
      <div className="bg-destructive/95 text-destructive-foreground px-6 py-4 rounded-lg shadow-lg flex items-center gap-3 border border-destructive">
        <AlertCircle className="h-5 w-5 flex-shrink-0" />
        <div>
          <p className="font-semibold">Acceso denegado</p>
          <p className="text-sm opacity-90">Requieres un rol superior para realizar esta acción</p>
        </div>
      </div>
    </div>
  );
};

export default AlertaAccesoDenegado;
