import { useState } from "react";
import { Minus } from "lucide-react";
import {
  Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger,
} from "@/components/ui/dialog";
import {
  AlertDialog, AlertDialogAction, AlertDialogCancel, AlertDialogContent,
  AlertDialogDescription, AlertDialogFooter, AlertDialogHeader, AlertDialogTitle,
} from "@/components/ui/alert-dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import api from "@/services/api";
import { set } from "date-fns";
interface Producto {
  idProducto: number;
  codigo: string;
  nombreProducto: string;
  precio: number;
  cantidad: number;
}

interface Props {
  producto: Producto;
  onDescontar: (idProducto: number, codigo: string, cantidad: number) => void;
}

const DescontarDialog = ({ producto, onDescontar }: Props) => {
  const [open, setOpen] = useState(false);
  const [cantidad, setCantidad] = useState("");
  const [showConfirm, setShowConfirm] = useState(false);
  const [showError, setShowError] = useState(false);
  const [loading, setLoading] = useState(false);

  const handleDescontar = () => {
    if (!cantidad.trim() || Number(cantidad) <= 0) return;

    if (Number(cantidad) > producto.cantidad) {
      setShowError(true);
      return;
    }
    setShowConfirm(true);
  };

  const confirmar = async () => {

    try{
      setLoading(true);

      const response = await api.put(`/inventario/descontarCantidad/${producto.codigo}/${cantidad}`);
      
      onDescontar(producto.idProducto, producto.codigo, response.data.cantidad);
      setCantidad("");
      setShowConfirm(false);
      setOpen(false);
    }catch(err){
      console.error("Error al descontar el producto:", err);
      alert("Error al descontar el producto. Por favor, intenta nuevamente.");
      setShowConfirm(false);
    }finally{
      setLoading(false);
    }
  };

  return (
    <>
      <Dialog open={open} onOpenChange={setOpen}>
        <DialogTrigger asChild>
          <button className="p-2 rounded-lg text-orange-500 hover:bg-orange-500/10 transition-colors" title="Descontar stock">
            <Minus className="h-4 w-4" />
          </button>
        </DialogTrigger>
        <DialogContent className="sm:max-w-sm">
          <DialogHeader>
            <DialogTitle className="font-heading">Descontar Stock</DialogTitle>
          </DialogHeader>
          <div className="space-y-4 mt-2">
            <div className="space-y-1">
              <Label>ID del Producto</Label>
              <Input value={producto.idProducto} disabled />
            </div>
            <div></div>
            <div className="space-y-1">
              <Label>Código del Producto</Label>
              <Input value={producto.codigo} disabled />
            </div>
            <div className="space-y-1">
              <Label>Producto</Label>
              <Input value={producto.nombreProducto} disabled />
            </div>
            <div className="space-y-1">
              <Label>Stock disponible</Label>
              <Input value={producto.cantidad} disabled />
            </div>
            <div className="space-y-1">
              <Label>Cantidad a descontar</Label>
              <Input type="number" min="1" value={cantidad} onChange={(e) => setCantidad(e.target.value)} placeholder="Ej: 5" />
            </div>
            <div className="flex gap-3 justify-end pt-2">
              <Button variant="outline" onClick={() => setOpen(false)}>Cancelar</Button>
              <Button onClick={handleDescontar} variant="destructive">Descontar</Button>
            </div>
          </div>
        </DialogContent>
      </Dialog>

      <AlertDialog open={showConfirm} onOpenChange={setShowConfirm}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Confirmar descuento</AlertDialogTitle>
            <AlertDialogDescription>
              ¿Deseas descontar <strong>{cantidad}</strong> unidades de <strong>{producto.nombreProducto}</strong>?
              <br />Stock actual: {producto.cantidad} → Nuevo stock: {producto.cantidad - Number(cantidad || 0)}
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel>Cancelar</AlertDialogCancel>
            <AlertDialogAction onClick={confirmar}>Confirmar</AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>

      <AlertDialog open={showError} onOpenChange={setShowError}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Error</AlertDialogTitle>
            <AlertDialogDescription>
              No se puede descontar más del disponible. Stock actual: <strong>{producto.cantidad}</strong> unidades.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogAction onClick={() => setShowError(false)}>Entendido</AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </>
  );
};

export default DescontarDialog;
