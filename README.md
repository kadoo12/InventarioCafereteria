# Sistema de Gestión de Inventario - Unimonserrate

## 📋 Descripción General

Sistema web completo de gestión de inventario con control de acceso por roles y categorización de productos. Diseñado para cafeterías y pequeños comercios que necesitan organizar su stock de manera eficiente.

## ✨ Características Principales

✅ **Autenticación con JWT** - Tokens seguros con roles incluidos
✅ **Control de Acceso por Roles** - Admin y Vendedor con permisos diferenciados
✅ **Categorización de Productos** - Aseo, Comestibles, Electrodomésticos
✅ **Gestión Completa de Inventario** - Agregar, editar, buscar y filtrar productos
✅ **Filtrado por Categoría** - Interfaz intuitiva con dropdown
✅ **Base de Datos Relacional** - PostgreSQL con relaciones Foreign Key
✅ **API REST** - Endpoints seguros con validaciones
✅ **Frontend Responsive** - React + TypeScript + Tailwind CSS

## 🏗️ Arquitectura

```
ProyectoInventario/
│
├── Servicio_Inventario/          (Backend - Java Spring Boot)
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
│
├── InventarioCafetería/          (Frontend - React TypeScript)
│   ├── src/
│   ├── package.json
│   └── Dockerfile
│
└── BDInventario/                 (Backups de Base de Datos)
    └── *.sql, *.backup
```

## 🚀 Inicio Rápido

### Requisitos
- Docker y Docker Compose
- Git
- Navegador web moderno

### Instalación

```bash
# Clonar el repositorio
git clone <url-del-repo>
cd ProyectoInventario

# Iniciar todos los servicios
docker compose up -d

# Esperar 60 segundos a que se inicialicen todos los contenedores
```

### Acceso

- **Frontend:** http://localhost:3000
- **Backend API:** http://localhost:8081/controller
- **Base de Datos:** localhost:5432

### Credenciales de Prueba

| Usuario | Contraseña | Rol |
|---------|-----------|-----|
| admin | contrasena123 | ADMIN |
| vendedor | contrasena123 | VENDEDOR |

## 👥 Permisos por Rol

### Admin
- ✅ Agregar productos
- ✅ Editar cantidad de productos
- ✅ Eliminar productos
- ✅ Asignar categorías
- ✅ Ver todos los productos

### Vendedor
- ✅ Ver todos los productos
- ✅ Filtrar por categoría
- ✅ Descontar cantidad de stock
- ❌ Agregar productos (403 Forbidden)
- ❌ Eliminar productos
- ❌ Editar información de productos

## 📁 Estructura del Repositorio

```
├── README.md                     (Este archivo)
├── docker-compose.yml            (Configuración de servicios)
├── Servicio_Inventario/
│   ├── README.md                 (Documentación del Backend)
│   ├── src/
│   │   └── main/java/com/lta/inventario/ServicioInventario/
│   │       ├── Controller/       (Endpoints REST)
│   │       ├── Inventario/       (Lógica de productos)
│   │       ├── Categoria/        (Gestión de categorías)
│   │       ├── Usuario/          (Gestión de usuarios)
│   │       ├── Jwt/              (Autenticación JWT)
│   │       └── Config/           (Configuración Spring)
│   ├── pom.xml                   (Dependencias Maven)
│   └── Dockerfile                (Imagen Docker)
│
├── InventarioCafetería/
│   ├── README.md                 (Documentación del Frontend)
│   ├── src/
│   │   ├── pages/                (Páginas React)
│   │   ├── components/           (Componentes reutilizables)
│   │   ├── services/             (Servicios API)
│   │   ├── hooks/                (Custom hooks)
│   │   └── App.tsx               (Componente raíz)
│   ├── package.json              (Dependencias npm)
│   ├── vite.config.ts            (Configuración Vite)
│   ├── tailwind.config.ts        (Temas Tailwind)
│   ├── Dockerfile                (Imagen Docker)
│   └── nginx.conf                (Configuración Nginx)
│
└── BDInventario/                 (Backups de BD)
    ├── README.md                 (Cómo restaurar backups)
    ├── inventario_backup_completo_*.backup
    └── inventario_backup_final_*.sql
```

## 🔧 Tecnologías Utilizadas

### Backend
- **Java 21** - Lenguaje principal
- **Spring Boot 3.5.5** - Framework web
- **Spring Security** - Autenticación y autorización
- **JWT (jjwt)** - Tokens seguros
- **PostgreSQL 16** - Base de datos
- **Hibernate/JPA** - ORM
- **Maven** - Gestor de dependencias

### Frontend
- **React 18** - Librería UI
- **TypeScript** - Tipado estático
- **Vite** - Bundler rápido
- **Tailwind CSS** - Estilos
- **Axios** - Cliente HTTP
- **React Router** - Navegación
- **Lucide React** - Iconos

### DevOps
- **Docker** - Containerización
- **Docker Compose** - Orquestación local
- **Nginx** - Servidor web (Frontend)
- **Tomcat** - Servidor web (Backend)

## 🗄️ Base de Datos

### Tablas principales

#### categoria
```sql
id (PK, Integer)
nombre (String, NOT NULL, UNIQUE)
descripcion (String)
```

#### producto
```sql
id_producto (PK, Integer)
codigo (String, NOT NULL, UNIQUE)
nombre_producto (String, NOT NULL)
precio (Integer)
cantidad (Integer)
categoria_id (FK → categoria, Nullable)
```

#### usuario
```sql
id (PK, Integer)
nom_usuario (String, NOT NULL, UNIQUE)
contrasena (String - BCrypt)
rol (String: ADMIN, VENDEDOR)
```

## 🔐 Seguridad

- ✅ Contraseñas encriptadas con BCrypt
- ✅ JWT con expiración de 1 hora
- ✅ CORS configurado para localhost:3000
- ✅ @PreAuthorize para validación de roles
- ✅ Validaciones en frontend y backend
- ✅ SQL Injection prevention con ORM

## 📊 Categorías Disponibles

1. **Aseo** - Productos de limpieza y aseo personal
2. **Comestibles** - Alimentos y bebidas
3. **Electrodomésticos** - Aparatos electrónicos

## 🔄 Flujo de Uso Típico

### Como Admin
1. Inicia sesión
2. Ve el dashboard con todos los productos
3. Selecciona una categoría del dropdown (opcional)
4. Busca productos específicos
5. Puede agregar nuevos productos con categoría obligatoria
6. Puede aumentar o disminuir cantidad
7. Puede eliminar productos

### Como Vendedor
1. Inicia sesión
2. Ve el dashboard con todos los productos
3. Selecciona una categoría del dropdown
4. Ve solo los productos de esa categoría
5. Puede descontar cantidad de stock
6. NO puede agregar ni eliminar productos

## 🧪 Testing

### Tests Backend
```bash
# Dentro del contenedor o máquina host
cd Servicio_Inventario
mvn test
```

### Tests Frontend
```bash
# Dentro del contenedor o máquina host
cd InventarioCafetería
npm test
```

## 📝 Endpoints Principales

### Autenticación
- `POST /controller/login` - Obtener token JWT

### Productos
- `GET /controller/inventario/listadoProductos` - Listar todos
- `GET /controller/inventario/listadoProductos/categoria/{id}` - Filtrar por categoría
- `POST /controller/inventario/agregarProducto` - Agregar (ADMIN)
- `PUT /controller/inventario/sumarProducto/{codigo}/{cantidad}` - Aumentar (ADMIN)
- `PUT /controller/inventario/descontarCantidad/{codigo}/{cantidad}` - Descontar (ADMIN, VENDEDOR)
- `DELETE /controller/inventario/eliminarProducto/{codigo}` - Eliminar (ADMIN)

### Categorías
- `GET /controller/categorias` - Listar categorías

## 🐛 Troubleshooting

### Error de conexión a la BD
```bash
# Reiniciar todos los servicios
docker compose down
docker compose up -d
```

### Frontend no se conecta al backend
- Verificar que el backend esté en http://localhost:8081
- Verificar CORS en SecurityConfig.java

### Productos no aparecen
- Verificar que la categoría tenga productos asignados
- Revisar logs: `docker logs inventario-backend`

## 📚 Documentación Adicional

- [README Backend](./Servicio_Inventario/README.md)
- [README Frontend](./InventarioCafetería/README.md)
- [README Base de Datos](./BDInventario/README.md)

## 👨‍💻 Autor

Sistema desarrollado como proyecto de gestión de inventario para Unimonserrate.

## 📄 Licencia

Uso interno - Unimonserrate

## 🔗 Enlaces Útiles

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [React Documentation](https://react.dev)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Docker Documentation](https://docs.docker.com/)

## 📞 Contacto y Soporte

Para preguntas o problemas, contactar al equipo de desarrollo.

---

**Última actualización:** 2026-05-20
**Versión:** 1.0.0
