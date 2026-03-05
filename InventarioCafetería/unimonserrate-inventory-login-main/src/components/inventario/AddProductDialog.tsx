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
import { set } from "date-fns";
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

  const [camposVacios, setCamposVacios] = useState({
    codigo: false,
    nombreProducto: false,
    precio: false,
    cantidad: false,
});

const handleSubmit = async (e: React.FormEvent) => {
  e.preventDefault();
  setError("");

const errores = {
  codigo: !codigo.trim(),
  nombreProducto: !nombreProducto.trim(),
  precio: !precio.trim() || isNaN(Number(precio)) || Number(precio) <= 0,
  cantidad: !cantidad.trim() || isNaN(Number(cantidad)) || Number(cantidad) <= 0,
}

setCamposVacios(errores);

  if (Object.values(errores).some((v) => v)) {
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
    resetForm();

  } catch (err: any) {
    const mensajeError = err.response?.data?.message || "Error al agregar el producto";
    setError(mensajeError);

    setCamposVacios(prev =>({...prev, codigo: true, nombreProducto: true}));
  }
};

const resetForm = () => {
  setCodigo("");
  setNombreProducto("");
  setPrecio("");
  setCantidad("");
  setError("");
  setCamposVacios({
    codigo: false,
    nombreProducto: false,
    precio: false,
    cantidad: false,
}); 
} 

const inputClass = (hasError: boolean) => 
    `transition-all ${hasError ? "border-destructive focus-visible:ring-destructive" : ""}`;
  return (

    <Dialog open={open} onOpenChange={(val) => {setOpen(val); if(!val) resetForm();}}>
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
            <Label>Código <span className="text-destructive">*</span></Label>
            <Input 
            className={inputClass(camposVacios.codigo)}
            value={codigo} onChange={(e) => {
              setCodigo(e.target.value);
              setCamposVacios({...camposVacios, codigo: false});
            }} placeholder="Ej: P001" />
          </div>
          <div className="space-y-2">
            <Label>Nombre del producto</Label>
            <Input 
            className={inputClass(camposVacios.nombreProducto)}
            value={nombreProducto} 
            onChange={(e) => {
              setNombreProducto(e.target.value);
              setCamposVacios({...camposVacios, nombreProducto: false});
            }}
            placeholder="Ej: Manzana" />
          </div>

          <div className="space-y-2">
            <Label>Precio <span className="text-destructive">*</span></Label>
            <Input 
            type="number" 
            className={inputClass(camposVacios.precio)}
            value={precio} onChange={(e) => {
              setPrecio(e.target.value);
              setCamposVacios({...camposVacios, precio: false});
            }} placeholder="Ej: 15000" />
          </div>

          <div className="space-y-2">
            <Label>Cantidad <span className="text-destructive">*</span></Label>
            <Input 
            type="number" 
            className={inputClass(camposVacios.cantidad)}
            value={cantidad} onChange={(e) => {
              setCantidad(e.target.value);
              setCamposVacios({...camposVacios, cantidad: false});
            }} placeholder="Ej: 100" />
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
