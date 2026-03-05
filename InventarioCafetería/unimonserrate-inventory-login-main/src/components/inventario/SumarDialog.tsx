import { useState } from "react";
import { Plus } from "lucide-react";
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

interface Producto {
  idProducto: number;
  codigo: string;
  nombreProducto: string;
  precio: number;
  cantidad: number;
}

interface Props {
  producto: Producto;
  onSumar: (idProducto: number, codigo: string, cantidad: number) => void;
}

const SumarDialog = ({ producto, onSumar }: Props) => {
  const [open, setOpen] = useState(false);
  const [cantidad, setCantidad] = useState("");
  const [showConfirm, setShowConfirm] = useState(false);

  const handleSumar = () => {
    if (!cantidad || Number(cantidad) <= 0) return;
    setShowConfirm(true);
  };

  const confirmar = () => {
    onSumar(producto.idProducto, producto.codigo, Number(cantidad));
    setCantidad("");
    setShowConfirm(false);
    setOpen(false);
  };

  return (
    <>
      <Dialog open={open} onOpenChange={setOpen}>
        <DialogTrigger asChild>
          <button className="p-2 rounded-lg text-secondary hover:bg-secondary/10 transition-colors" title="Sumar stock">
            <Plus className="h-4 w-4" />
          </button>
        </DialogTrigger>
        <DialogContent className="sm:max-w-sm">
          <DialogHeader>
            <DialogTitle className="font-heading">Sumar Stock</DialogTitle>
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
              <Label>Cantidad a sumar</Label>
              <Input type="number" min="1" value={cantidad} onChange={(e) => setCantidad(e.target.value)} placeholder="Ej: 10" />
            </div>
            <div className="flex gap-3 justify-end pt-2">
              <Button variant="outline" onClick={() => setOpen(false)}>Cancelar</Button>
              <Button onClick={handleSumar} className="bg-secondary text-secondary-foreground hover:bg-secondary/80">Sumar</Button>
            </div>
          </div>
        </DialogContent>
      </Dialog>

      <AlertDialog open={showConfirm} onOpenChange={setShowConfirm}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Confirmar suma</AlertDialogTitle>
            <AlertDialogDescription>
              ¿Deseas sumar <strong>{cantidad}</strong> unidades a <strong>{producto.nombreProducto}</strong>?
              <br />Stock actual: {producto.cantidad} → Nuevo stock: {producto.cantidad + Number(cantidad || 0)}
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel>Cancelar</AlertDialogCancel>
            <AlertDialogAction onClick={confirmar}>Confirmar</AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </>
  );
};

export default SumarDialog;
