import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { LogOut, Package, Search } from "lucide-react";
import AddProductDialog from "@/components/inventario/AddProductDialog";
import SumarDialog from "@/components/inventario/SumarDialog";
import DescontarDialog from "@/components/inventario/DescontarDialog";
import DeleteProductDialog from "@/components/inventario/DeleteProductDialog";
import api from "@/services/api";
interface Producto {
  idProducto: number;
  codigo: string;
  nombreProducto: string;
  precio: number;
  cantidad: number;
}



const Inventario = () => {
  const navigate = useNavigate();
  const [nomUsuario, setUser] = useState<{ nomUsuario: string } | null>(null);
  const [productos, setProductos] = useState<Producto[]>([]);
  const [busqueda, setBusqueda] = useState("");

  useEffect(() => {
    const stored = localStorage.getItem("user");
    if (!stored) {
      navigate("/");
      return;
    }
    setUser(JSON.parse(stored));

    const cargarProductos = async () => {
      try {
        const response = await api.get("/inventario/listadoProductos");
        setProductos(response.data);
    }catch (err) {
        console.error("Error al cargar productos:", err);
      }
    };

    cargarProductos();
  }, [navigate]);

  const handleLogout = () => {
    localStorage.removeItem("user");
    navigate("/");
  };

  const filteredProducts = productos.filter((p) =>
    p.nombreProducto.toLowerCase().includes(busqueda.toLowerCase()) ||
    p.idProducto.toString().includes(busqueda) ||
    p.codigo.toLowerCase().includes(busqueda.toLowerCase())
  );

  const handleAdd = (ProductoDesdeServer: Producto) => {
    setProductos((prev) => {
      const indiceExiste = prev.findIndex((p) => p.codigo === ProductoDesdeServer.codigo);
      if (indiceExiste !== -1) {
        const actualizado = [...prev];
        actualizado[indiceExiste] = ProductoDesdeServer;
        return actualizado;
      }else {
        return [...prev, ProductoDesdeServer];
      }        
    });
    
  };

  const handleSumar = (idProducto: number, codigo: string, nuevaCantidadTotal: number) => {
    setProductos(prevProductos => prevProductos.map((p) => p.idProducto === idProducto || p.codigo === codigo ? { ...p, cantidad: Number(nuevaCantidadTotal) } : p));
  };          

  const handleDescontar = (id: number, codigo: string, cantidad: number) => {
    setProductos(productos.map((p) => p.idProducto === id || p.codigo === codigo ? { ...p, cantidad: p.cantidad - cantidad } : p));
  };

  const handleDelete = (id: number) => {
    setProductos(productos.filter((p) => p.idProducto !== id));
  };

  const formatCurrency = (val: number) =>
    new Intl.NumberFormat("es-CO", { style: "currency", currency: "COP", minimumFractionDigits: 0 }).format(val);

  if (!nomUsuario) return null;

  return (
    <div className="min-h-screen bg-background">
      {/* Header */}
      <header className="bg-primary shadow-lg">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 py-4 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-xl bg-secondary flex items-center justify-center shadow shadow-secondary/30">
              <span className="text-lg font-heading font-extrabold text-secondary-foreground">U</span>
            </div>
            <div>
              <h1 className="text-lg font-heading font-bold text-primary-foreground">Inventario</h1>
              <p className="text-xs text-primary-foreground/50">Unimonserrate</p>
            </div>
          </div>
          <div className="flex items-center gap-4">
            <span className="text-sm text-primary-foreground/70 hidden sm:block">
              Hola, <strong className="text-primary-foreground">{nomUsuario?.nomUsuario}</strong>
            </span>
            <button
              onClick={handleLogout}
              className="flex items-center gap-2 px-4 py-2 rounded-xl bg-primary-foreground/10 text-primary-foreground text-sm hover:bg-primary-foreground/20 transition-colors"
            >
              <LogOut className="h-4 w-4" />
              Salir
            </button>
          </div>
        </div>
      </header>

      {/* Content */}
      <main className="max-w-7xl mx-auto px-4 sm:px-6 py-8">
        {/* Stats */}
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 mb-8">
          <div className="bg-card rounded-2xl p-5 shadow-sm border border-border">
            <p className="text-sm text-muted-foreground">Total Productos</p>
            <p className="text-2xl font-heading font-bold text-foreground">{productos.length}</p>
          </div>
          <div className="bg-card rounded-2xl p-5 shadow-sm border border-border">
            <p className="text-sm text-muted-foreground">Unidades Totales</p>
            <p className="text-2xl font-heading font-bold text-foreground">
              {productos.reduce((a, p) => a + p.cantidad, 0).toLocaleString()}
            </p>
          </div>
          <div className="bg-card rounded-2xl p-5 shadow-sm border border-border">
            <p className="text-sm text-muted-foreground">Valor Total</p>
            <p className="text-2xl font-heading font-bold text-secondary">
              {formatCurrency(productos.reduce((a, p) => a + p.precio * p.cantidad, 0))}
            </p>
          </div>
        </div>

        {/* Toolbar */}
        <div className="flex flex-col sm:flex-row gap-3 mb-6">
          <div className="relative flex-1">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
            <input
              type="text"
              placeholder="Buscar por nombre o ID..."
              value={busqueda}
              onChange={(e) => setBusqueda(e.target.value)}
              className="w-full pl-10 pr-4 py-2.5 rounded-xl border border-input bg-card text-foreground placeholder:text-muted-foreground/50 focus:outline-none focus:ring-2 focus:ring-ring transition-all"
            />
          </div>
          <AddProductDialog onAdd={handleAdd} />
        </div>

        {/* Table */}
        <div className="bg-card rounded-2xl shadow-sm border border-border overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead>
                <tr className="bg-primary">
                  <th className="text-left py-3.5 px-5 text-xs font-heading font-semibold text-primary-foreground uppercase tracking-wider">ID</th>
                  <th className="text-left py-3.5 px-5 text-xs font-heading font-semibold text-primary-foreground uppercase tracking-wider">Nombre Producto</th>
                  <th className="text-left py-3.5 px-5 text-xs font-heading font-semibold text-primary-foreground uppercase tracking-wider">Código</th>
                  <th className="text-left py-3.5 px-5 text-xs font-heading font-semibold text-primary-foreground uppercase tracking-wider">Precio</th>
                  <th className="text-left py-3.5 px-5 text-xs font-heading font-semibold text-primary-foreground uppercase tracking-wider">Cantidad</th>
                  <th className="text-right py-3.5 px-5 text-xs font-heading font-semibold text-primary-foreground uppercase tracking-wider">Acciones</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-border">
                {filteredProducts.map((p) => (
                  <tr key={p.idProducto} className="hover:bg-muted/50 transition-colors">
                    <td className="py-3.5 px-5 text-sm text-muted-foreground font-mono">{p.idProducto}</td>
                    <td className="py-3.5 px-5 text-sm font-medium text-foreground flex items-center gap-2">
                      <Package className="h-4 w-4 text-secondary" />
                      {p.nombreProducto}
                    </td>
                    <td className="py-3.5 px-5 text-sm text-foreground">{p.codigo}</td>
                    <td className="py-3.5 px-5 text-sm text-foreground">{formatCurrency(p.precio)}</td>
                    <td className="py-3.5 px-5">
                      <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-semibold ${p.cantidad < 20 ? 'bg-destructive/10 text-destructive' : p.cantidad < 100 ? 'bg-accent/20 text-accent-foreground' : 'bg-secondary/10 text-secondary'}`}>
                        {p.cantidad}
                      </span>
                    </td>
                    <td className="py-3.5 px-5 text-right">
                      <div className="flex items-center justify-end gap-1">
                        <SumarDialog producto={p} onSumar={handleSumar} />
                        <DescontarDialog producto={p} onDescontar={handleDescontar} />
                        <DeleteProductDialog productoNombre={p.nombreProducto} onDelete={() => handleDelete(p.idProducto)} />
                      </div>
                    </td>
                  </tr>
                ))}
                {filteredProducts.length === 0 && (
                  <tr>
                    <td colSpan={5} className="py-12 text-center text-muted-foreground">
                      No se encontraron productos
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      </main>
    </div>
  );
};

export default Inventario;
