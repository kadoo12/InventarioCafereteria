import { useState } from "react";
import { Plus } from "lucide-react";
import {
  Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import api from "@/services/api";
import { on } from "events";
interface Props {
  onAdd: (producto: { codigo: string; nombreProducto: string; precio: number; cantidad: number }) => void;
}

const AddProductDialog = ({ onAdd }: Props) => {
  const [open, setOpen] = useState(false);
  const [codigo, setCodigo] = useState("");
  const [nombreProducto, setNombreProducto] = useState("");
  const [precio, setPrecio] = useState("");
  const [cantidad, setCantidad] = useState("");
  const [error, setError] = useState("");

const handleSubmit = async (e: React.FormEvent) => {
  e.preventDefault();
  if (!codigo.trim() || !nombreProducto.trim() || !precio.trim() || !cantidad.trim()) {
    setError("Todos los campos son obligatorios");
    return;
  }
  try {
    const nuevoProducto = {
      codigo,
      nombreProducto,
      precio: Number(precio),
      cantidad: Number(cantidad),
    };

    const response = await api.post("/inventario/agregarProducto", nuevoProducto);

    onAdd(response.data);

    setOpen(false);
    setCodigo("");
    setNombreProducto("");
    setPrecio("");
    setCantidad("");
    setError("");
  } catch (err) {
    setError("Error al agregar el producto");
  }
};

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <button className="flex items-center gap-2 px-5 py-2.5 rounded-xl bg-secondary text-secondary-foreground font-semibold text-sm hover:brightness-110 active:scale-[0.98] transition-all shadow-md shadow-secondary/20">
          <Plus className="h-4 w-4" />
          Agregar Producto
        </button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-md">
        <DialogHeader>
          <DialogTitle className="font-heading">Nuevo Producto</DialogTitle>
        </DialogHeader>
        <form onSubmit={handleSubmit} className="space-y-4 mt-2">
          <div className="space-y-2">
            <Label>Código</Label>
            <Input value={codigo} onChange={(e) => setCodigo(e.target.value)} placeholder="Ej: P001" />
          </div>
          <div className="space-y-2">
            <Label>Nombre del producto</Label>
            <Input value={nombreProducto} onChange={(e) => setNombreProducto(e.target.value)} placeholder="Ej: Manzana" />
          </div>
          <div className="space-y-2">
            <Label>Precio</Label>
            <Input type="number" value={precio} onChange={(e) => setPrecio(e.target.value)} placeholder="Ej: 15000" />
          </div>
          <div className="space-y-2">
            <Label>Cantidad</Label>
            <Input type="number" value={cantidad} onChange={(e) => setCantidad(e.target.value)} placeholder="Ej: 100" />
          </div>
          <div className="flex gap-3 justify-end pt-2">
            <Button type="button" variant="outline" onClick={() => setOpen(false)}>Cancelar</Button>
            <Button type="submit" className="bg-secondary text-secondary-foreground hover:bg-secondary/80">Guardar</Button>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  );
};

export default AddProductDialog;
