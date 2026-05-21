import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { LogOut, Package, Search, Menu } from "lucide-react";
import AddProductDialog from "@/components/inventario/AddProductDialog";
import SumarDialog from "@/components/inventario/SumarDialog";
import DescontarDialog from "@/components/inventario/DescontarDialog";
import DeleteProductDialog from "@/components/inventario/DeleteProductDialog";
import AlertaAccesoDenegado from "@/components/AlertaAccesoDenegado";
import { useRoleValidation } from "@/hooks/useRoleValidation";
import api from "@/services/api";

interface Producto {
  idProducto: number;
  codigo: string;
  nombreProducto: string;
  precio: number;
  cantidad: number;
  categoria?: Categoria;
}

interface Categoria {
  id: number;
  nombre: string;
  descripcion: string;
}

const Inventario = () => {
  const navigate = useNavigate();
  const { canPerformAction, handleUnauthorized, showAlert } = useRoleValidation();
  
  const [nomUsuario, setUser] = useState<{ nomUsuario: string } | null>(null);
  const [rol, setRol] = useState<string | null>(null);
  const [productos, setProductos] = useState<Producto[]>([]);
  const [categorias, setCategorias] = useState<Categoria[]>([]);
  const [categoriaSeleccionada, setCategoriaSeleccionada] = useState<number | null>(null);
  const [busqueda, setBusqueda] = useState("");

  useEffect(() => {
    const stored = localStorage.getItem("user");
    const rolGuardado = localStorage.getItem("rol");
    
    if (!stored) {
      navigate("/", { replace: true });
      return;
    }
    
    setUser(JSON.parse(stored));
    setRol(rolGuardado);

    const cargarDatos = async () => {
      try {
        // Cargar categorías
        const respCategorias = await api.get("/categorias");
        setCategorias(respCategorias.data);

        // Cargar todos los productos
        const respProductos = await api.get("/inventario/listadoProductos");
        setProductos(respProductos.data);
      } catch (err) {
        console.error("Error al cargar datos:", err);
      }
    };

    cargarDatos();
  }, [navigate]);

  const handleLogout = () => {
    localStorage.removeItem("user");
    localStorage.removeItem("rol");
    localStorage.removeItem("token");
    navigate("/", { replace: true });
  };

  // Filtrar productos por categoría seleccionada y búsqueda
  const filteredProducts = productos.filter((p) => {
    const coincideBusqueda =
      p.nombreProducto.toLowerCase().includes(busqueda.toLowerCase()) ||
      p.idProducto.toString().includes(busqueda) ||
      p.codigo.toLowerCase().includes(busqueda.toLowerCase());

    const coincideCategoria = categoriaSeleccionada === null || p.categoria?.id === categoriaSeleccionada;

    return coincideBusqueda && coincideCategoria;
  });

  const handleAdd = (ProductoDesdeServer: Producto) => {
    setProductos((prev) => {
      const indiceExiste = prev.findIndex((p) => p.codigo === ProductoDesdeServer.codigo);
      if (indiceExiste !== -1) {
        const actualizado = [...prev];
        actualizado[indiceExiste] = ProductoDesdeServer;
        return actualizado;
      } else {
        return [...prev, ProductoDesdeServer];
      }
    });
  };

  const handleSumar = (idProducto: number, codigo: string, nuevaCantidadTotal: number) => {
    setProductos((prevProductos) =>
      prevProductos.map((p) =>
        p.idProducto === idProducto || p.codigo === codigo
          ? { ...p, cantidad: Number(nuevaCantidadTotal) }
          : p
      )
    );
  };

  const handleDescontar = (id: number, codigo: string, cantidad: number) => {
    setProductos((prevProductos) =>
      prevProductos.map((p) =>
        p.idProducto === id || p.codigo === codigo
          ? { ...p, cantidad: Number(cantidad) }
          : p
      )
    );
  };

  const handleDelete = async (codigo: string, id: number) => {
    if (!canPerformAction("ADMIN")) {
      handleUnauthorized();
      return;
    }

    try {
      await api.delete(`/inventario/eliminarProducto/${codigo}`);
      setProductos((prev) => prev.filter((p) => p.idProducto !== id));
    } catch (err) {
      alert("Error al eliminar producto: " + err);
    }
  };

  const formatCurrency = (val: number) =>
    new Intl.NumberFormat("es-CO", {
      style: "currency",
      currency: "COP",
      minimumFractionDigits: 0,
    }).format(val);

  if (!nomUsuario) return null;

  return (
    <div className="min-h-screen bg-background">
      <AlertaAccesoDenegado visible={showAlert} />

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
            <div className="text-sm text-primary-foreground/70 hidden sm:block">
              <p>
                Hola, <strong className="text-primary-foreground">{nomUsuario?.nomUsuario}</strong>
              </p>
              <p className="text-xs opacity-75">Rol: {rol}</p>
            </div>
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
            <p className="text-2xl font-heading font-bold text-foreground">{filteredProducts.length}</p>
          </div>
          <div className="bg-card rounded-2xl p-5 shadow-sm border border-border">
            <p className="text-sm text-muted-foreground">Unidades Totales</p>
            <p className="text-2xl font-heading font-bold text-foreground">
              {filteredProducts.reduce((a, p) => a + p.cantidad, 0).toLocaleString()}
            </p>
          </div>
          <div className="bg-card rounded-2xl p-5 shadow-sm border border-border">
            <p className="text-sm text-muted-foreground">Valor Total</p>
            <p className="text-2xl font-heading font-bold text-secondary">
              {formatCurrency(filteredProducts.reduce((a, p) => a + p.precio * p.cantidad, 0))}
            </p>
          </div>
        </div>

        {/* Toolbar */}
        <div className="flex flex-col sm:flex-row gap-3 mb-6 items-start sm:items-center">
          {/* Dropdown Categorías - Menú hamburguesa a la izquierda */}
          <div className="flex items-center gap-2 bg-card rounded-xl border border-input px-3 py-2.5 hover:border-primary/50 transition-colors cursor-pointer">
            <Menu className="h-5 w-5 text-primary font-bold" />
            <select
              value={categoriaSeleccionada ?? ""}
              onChange={(e) => setCategoriaSeleccionada(e.target.value ? Number(e.target.value) : null)}
              className="bg-card text-foreground focus:outline-none text-sm font-medium min-w-[180px] appearance-none cursor-pointer"
            >
              <option value="">Todas las categorías</option>
              {categorias.map((cat) => (
                <option key={cat.id} value={cat.id}>
                  {cat.nombre}
                </option>
              ))}
            </select>
          </div>

          {/* Buscador */}
          <div className="relative flex-1 w-full sm:w-auto">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
            <input
              type="text"
              placeholder="Buscar por nombre, código o id..."
              value={busqueda}
              onChange={(e) => setBusqueda(e.target.value)}
              className="w-full pl-10 pr-4 py-2.5 rounded-xl border border-input bg-card text-foreground placeholder:text-muted-foreground/50 focus:outline-none focus:ring-2 focus:ring-ring transition-all"
            />
          </div>

          {canPerformAction("ADMIN") && <AddProductDialog onAdd={handleAdd} productos={productos} onUnauthorized={handleUnauthorized} />}
        </div>

        {/* Table */}
        <div className="bg-card rounded-2xl shadow-sm border border-border overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead>
                <tr className="bg-primary">
                  <th className="text-left py-3.5 px-5 text-xs font-heading font-semibold text-primary-foreground uppercase tracking-wider">
                    ID
                  </th>
                  <th className="text-left py-3.5 px-5 text-xs font-heading font-semibold text-primary-foreground uppercase tracking-wider">
                    Nombre Producto
                  </th>
                  <th className="text-left py-3.5 px-5 text-xs font-heading font-semibold text-primary-foreground uppercase tracking-wider">
                    Categoría
                  </th>
                  <th className="text-left py-3.5 px-5 text-xs font-heading font-semibold text-primary-foreground uppercase tracking-wider">
                    Código
                  </th>
                  <th className="text-left py-3.5 px-5 text-xs font-heading font-semibold text-primary-foreground uppercase tracking-wider">
                    Precio
                  </th>
                  <th className="text-left py-3.5 px-5 text-xs font-heading font-semibold text-primary-foreground uppercase tracking-wider">
                    Cantidad
                  </th>
                  <th className="text-right py-3.5 px-5 text-xs font-heading font-semibold text-primary-foreground uppercase tracking-wider">
                    Acciones
                  </th>
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
                    <td className="py-3.5 px-5 text-sm text-foreground">
                      <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-semibold bg-primary/10 text-primary">
                        {p.categoria?.nombre || "Sin categoría"}
                      </span>
                    </td>
                    <td className="py-3.5 px-5 text-sm text-foreground">{p.codigo}</td>
                    <td className="py-3.5 px-5 text-sm text-foreground">{formatCurrency(p.precio)}</td>
                    <td className="py-3.5 px-5">
                      <span
                        className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-semibold ${
                          p.cantidad < 20
                            ? "bg-destructive/10 text-destructive"
                            : p.cantidad < 100
                              ? "bg-accent/20 text-accent-foreground"
                              : "bg-secondary/10 text-secondary"
                        }`}
                      >
                        {p.cantidad}
                      </span>
                    </td>
                    <td className="py-3.5 px-5 text-right">
                      <div className="flex items-center justify-end gap-1">
                        {canPerformAction("ADMIN") && (
                          <>
                            <SumarDialog producto={p} onSumar={handleSumar} />
                            <DeleteProductDialog
                              productoNombre={p.nombreProducto}
                              onDelete={() => handleDelete(p.codigo, p.idProducto)}
                            />
                          </>
                        )}
                        <DescontarDialog
                          producto={p}
                          onDescontar={handleDescontar}
                          onUnauthorized={handleUnauthorized}
                        />
                      </div>
                    </td>
                  </tr>
                ))}
                {filteredProducts.length === 0 && (
                  <tr>
                    <td colSpan={7} className="py-12 text-center text-muted-foreground">
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
