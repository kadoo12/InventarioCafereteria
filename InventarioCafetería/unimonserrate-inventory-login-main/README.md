# Frontend - Sistema de Gestión de Inventario

## 📝 Descripción

Frontend del Sistema de Gestión de Inventario. Aplicación web interactiva construida con React que permite:
- Autenticación de usuarios con JWT
- Visualización de productos organizados por categoría
- Gestión de inventario según rol del usuario
- Búsqueda y filtrado de productos
- Interfaz responsive y moderna

## 🚀 Tecnologías

- **React 18** - Librería de UI
- **TypeScript** - Tipado estático
- **Vite** - Bundler ultra rápido
- **Tailwind CSS** - Framework de utilidades
- **Axios** - Cliente HTTP
- **React Router** - Enrutamiento
- **Lucide React** - Iconos
- **shadcn/ui** - Componentes UI

## 📦 Estructura del Proyecto

```
InventarioCafetería/unimonserrate-inventory-login-main/
├── src/
│   ├── pages/
│   │   ├── Login.tsx              (Página de autenticación)
│   │   └── Inventario.tsx         (Dashboard principal)
│   │
│   ├── components/
│   │   ├── inventario/
│   │   │   ├── AddProductDialog.tsx       (Modal agregar producto)
│   │   │   ├── DescontarDialog.tsx        (Modal descontar cantidad)
│   │   │   ├── SumarDialog.tsx            (Modal aumentar cantidad)
│   │   │   └── DeleteProductDialog.tsx    (Modal eliminar producto)
│   │   ├── ui/
│   │   │   ├── dialog.tsx         (Componente Dialog)
│   │   │   ├── button.tsx         (Componente Button)
│   │   │   ├── input.tsx          (Componente Input)
│   │   │   └── ...                (Otros componentes)
│   │   └── AlertaAccesoDenegado.tsx      (Alerta de permiso denegado)
│   │
│   ├── services/
│   │   └── api.ts                 (Cliente HTTP con Axios)
│   │
│   ├── hooks/
│   │   └── useRoleValidation.ts   (Hook para validación de roles)
│   │
│   ├── App.tsx                    (Componente raíz)
│   ├── main.tsx                   (Punto de entrada)
│   └── index.css                  (Estilos globales)
│
├── public/
│   └── favicon.ico
│
├── package.json                   (Dependencias npm)
├── vite.config.ts                 (Configuración Vite)
├── tsconfig.json                  (Configuración TypeScript)
├── tailwind.config.ts             (Configuración Tailwind)
├── postcss.config.js              (Configuración PostCSS)
├── Dockerfile                     (Imagen Docker)
├── nginx.conf                     (Configuración Nginx)
└── README.md                      (Este archivo)
```

## 🎨 Componentes Principales

### Páginas

#### Login.tsx
- Autenticación de usuarios
- Validación de credenciales
- Almacenamiento de token JWT en localStorage
- Almacenamiento del rol para mostrar/ocultar funcionalidades
- Redirección automática al dashboard si ya está autenticado

#### Inventario.tsx
- Dashboard principal
- Listado de productos en tabla
- Estadísticas (total productos, unidades, valor)
- Dropdown para filtrar por categoría
- Buscador en tiempo real
- Botones de acción por rol
- Estados de carga y errores

### Componentes de Diálogos

#### AddProductDialog.tsx
- Formulario para agregar nuevos productos
- Categoría es campo obligatorio
- Validación de campos en frontend
- Validación de código único
- Precio y cantidad > 0
- Error 403 si vendedor intenta agregar
- Alerta visual si acceso es denegado

#### DescontarDialog.tsx
- Disminuir cantidad de stock
- Validación de cantidad disponible
- Disponible para Admin y Vendedor
- Confirmación antes de ejecutar

#### SumarDialog.tsx
- Aumentar cantidad de stock
- Solo disponible para Admin
- Validación de cantidad válida

#### DeleteProductDialog.tsx
- Eliminar producto del inventario
- Solo disponible para Admin
- Confirmación antes de eliminar

### Componentes de UI

#### AlertaAccesoDenegado.tsx
- Notificación visual cuando se deniega acceso
- Aparece 3 segundos automáticamente
- Se dispara desde AddProductDialog y DescontarDialog

## 🔌 Servicios

### api.ts
```typescript
// Cliente Axios con configuración global
const api = axios.create({
  baseURL: 'http://localhost:8081/controller',
  timeout: 5000,
});

// Interceptor que agrega token automáticamente
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});
```

Funciones disponibles:
- `api.get()` - Peticiones GET
- `api.post()` - Peticiones POST
- `api.put()` - Peticiones PUT
- `api.delete()` - Peticiones DELETE

## 🪝 Custom Hooks

### useRoleValidation.ts
```typescript
// Hook para validar permisos por rol
const { 
  canPerformAction,    // (role: string) => boolean
  handleUnauthorized,  // () => void (muestra alerta)
  showAlert            // boolean (estado de alerta)
} = useRoleValidation();
```

## 🔐 Autenticación y Seguridad

### Flujo de Autenticación

1. Usuario ingresa credenciales en Login.tsx
2. Se envía POST a `/controller/login`
3. Si es válido, se recibe token + rol
4. Se almacenan en localStorage:
   - `token` - JWT para autorización
   - `rol` - Rol del usuario (ADMIN/VENDEDOR)
   - `user` - Datos del usuario
5. Se redirige a /inventario
6. En cada petición, interceptor agrega `Authorization: Bearer <token>`
7. Si token es inválido, usuario es redirigido a login

### Tokens

- **Generación:** Backend genera JWT con rol
- **Expiración:** 1 hora
- **Almacenamiento:** localStorage (en sesión)
- **Envío:** Header `Authorization: Bearer <token>`

## 📡 Flujos API

### Login
```typescript
POST /controller/login
{
  nomUsuario: "admin",
  contrasena: "contrasena123"
}

Response:
{
  token: "eyJhbGciOiJIUzI1NiJ9...",
  rol: "ADMIN",
  nomUsuario: "admin"
}
```

### Listar Productos
```typescript
GET /controller/inventario/listadoProductos
Headers: Authorization: Bearer <token>

Response: Array de productos con categoría
```

### Filtrar por Categoría
```typescript
GET /controller/inventario/listadoProductos/categoria/1
Headers: Authorization: Bearer <token>

Response: Productos de categoría 1
```

### Agregar Producto
```typescript
POST /controller/inventario/agregarProducto
Headers: 
  - Authorization: Bearer <token>
  - Content-Type: application/json
Body: {
  codigo: "PROD001",
  nombreProducto: "Producto",
  precio: 10000,
  cantidad: 50,
  categoriaId: 1  // OBLIGATORIO en frontend
}
```

## 🏃‍♂️ Ejecución Local

### Prerequisitos
- Node.js 18+
- npm o yarn

### Pasos

```bash
# 1. Instalar dependencias
npm install

# 2. Desarrollo con hot reload
npm run dev

# 3. Build para producción
npm run build

# 4. Previsualizar build
npm run preview
```

### Con Docker

```bash
# Build
docker build -t inventario-frontend .

# Run
docker run -d -p 3000:3000 inventario-frontend
```

## 🎨 Estilos y Tema

### Tailwind CSS
- Configuración en `tailwind.config.ts`
- Variables CSS personalizadas
- Colores, espaciado, tipografía

### Tema Personalizado
```typescript
// Colors
primary: Azul principal
secondary: Verde/Naranja
destructive: Rojo para errores
muted: Gris para textos secundarios
```

### Tipografía
```typescript
font-heading: 'Geist' (títulos)
font-sans: 'Geist' (cuerpo)
```

## 📊 Estados de la Aplicación

### Inventario.tsx
```typescript
const [productos, setProductos] = useState<Producto[]>([]); // Lista de productos
const [categorias, setCategorias] = useState<Categoria[]>([]); // Categorías disponibles
const [categoriaSeleccionada, setCategoriaSeleccionada] = useState<number | null>(null); // Filtro activo
const [busqueda, setBusqueda] = useState(""); // Término de búsqueda
const [nomUsuario, setUser] = useState<{ nomUsuario: string } | null>(null); // Usuario actual
const [rol, setRol] = useState<string | null>(null); // Rol del usuario
```

## 🔄 Ciclo de Vida

### Al cargar Inventario.tsx
1. Se verifica si hay usuario loguado (localStorage)
2. Se cargan todas las categorías
3. Se cargan todos los productos
4. Se muestran en la tabla principal

### Al agregar producto
1. Se abre modal AddProductDialog
2. Se cargan categorías (si no están cargadas)
3. Usuario llena formulario con categoría obligatoria
4. Se valida en frontend
5. Se envía POST al backend
6. Si es exitoso, se actualiza lista local
7. Se cierra modal y se resetea formulario

### Al filtrar por categoría
1. Usuario selecciona categoría del dropdown
2. Se filtra lista local por categoría
3. Se actualiza tabla automáticamente
4. El filtro se combina con la búsqueda

## 🌐 Flujo de Navegación

```
/ (Login)
  ↓
/inventario (Dashboard)
  ├── Ver productos
  ├── Filtrar por categoría
  ├── Buscar
  ├── [Admin] Agregar producto
  ├── [Admin] Aumentar cantidad
  ├── [Admin] Eliminar producto
  ├── [Admin/Vendedor] Descontar cantidad
  └── [Cualquiera] Logout → /
```

## 📱 Responsive

El frontend es totalmente responsive:
- Desktop (1024px+)
- Tablet (768px - 1023px)
- Mobile (< 768px)

## ♿ Accesibilidad

- Semántica HTML correcta
- Labels asociados a inputs
- Contraste de colores WCAG
- Navegación por teclado
- ARIA attributes donde es necesario

## 🧪 Testing

```bash
# Tests unitarios
npm run test

# Coverage
npm run test:coverage

# E2E (si está configurado)
npm run test:e2e
```

## 🚢 Build y Deployment

### Build para producción
```bash
npm run build

# Genera carpeta dist/ lista para servir
```

### Nginx (Docker)
```conf
# nginx.conf
server {
  listen 80;
  root /usr/share/nginx/html;
  
  location / {
    try_files $uri /index.html;
  }
  
  location /api {
    proxy_pass http://backend:8081;
  }
}
```

## 🐛 Debugging

### Console logs
```typescript
// Habilitar logs de desarrollo
console.log('Debug:', data);
```

### React DevTools
Instalar extensión de React DevTools en navegador

### Network tab
Verificar peticiones HTTP en Developer Tools

## 📋 Checklist de Desarrollo

- ✅ Inputs validados
- ✅ Mensajes de error claros
- ✅ Loading states
- ✅ Manejo de errores 403
- ✅ Interceptor de token
- ✅ Logout funcional
- ✅ Tabla responsive
- ✅ Filtros funcionando
- ✅ Modal focus trap
- ✅ Confirmaciones antes de acciones destructivas

## 🔗 Enlaces

- [React Docs](https://react.dev)
- [TypeScript](https://www.typescriptlang.org/)
- [Vite](https://vitejs.dev/)
- [Tailwind CSS](https://tailwindcss.com/)
- [Axios](https://axios-http.com/)
- [shadcn/ui](https://ui.shadcn.com/)

## 📚 Recursos

- Guía de componentes: `/src/components/ui/`
- Ejemplos de uso: `/src/pages/`
- Configuración: `vite.config.ts`, `tailwind.config.ts`

---

**Versión:** 1.0.0
**Última actualización:** 2026-05-20
**Autor:** Equipo de Desarrollo - Unimonserrate
