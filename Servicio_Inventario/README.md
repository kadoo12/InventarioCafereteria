# Backend - Servicio de Inventario

## 📝 Descripción

Backend del Sistema de Gestión de Inventario. API REST construida con Spring Boot que maneja:
- Autenticación y autorización con JWT
- Gestión de productos y categorías
- Control de roles (Admin/Vendedor)
- Validación de permisos en endpoints
- Integración con PostgreSQL

## 🚀 Tecnologías

- **Java 21**
- **Spring Boot 3.5.5**
- **Spring Security**
- **JWT (jjwt)**
- **Spring Data JPA / Hibernate**
- **PostgreSQL 16**
- **Maven**

## 📦 Estructura del Proyecto

```
Servicio_Inventario/
├── src/main/java/com/lta/inventario/ServicioInventario/
│   ├── Controller/
│   │   ├── AutentController.java      (Endpoints REST)
│   │   ├── AutentService.java         (Lógica de autenticación)
│   │   ├── AutentResponse.java        (DTO de respuesta)
│   │   └── LoginRequest.java          (DTO de entrada)
│   │
│   ├── Inventario/
│   │   ├── Producto.java              (Entidad JPA)
│   │   ├── ProductoRequest.java       (DTO)
│   │   ├── InventarioService.java     (Lógica de negocio)
│   │   └── InventarioRepository.java  (Acceso a datos)
│   │
│   ├── Categoria/
│   │   ├── Categoria.java             (Entidad JPA)
│   │   ├── CategoriaService.java      (Lógica de negocio)
│   │   ├── CategoriaController.java   (Endpoints)
│   │   └── CategoriaRepository.java   (Acceso a datos)
│   │
│   ├── Usuario/
│   │   ├── Usuario.java               (Entidad JPA con UserDetails)
│   │   └── UsuarioRepository.java     (Acceso a datos)
│   │
│   ├── Jwt/
│   │   ├── JwtService.java            (Generación/Validación de JWT)
│   │   └── JwtAuthentificationFilter.java (Filtro de autenticación)
│   │
│   ├── Config/
│   │   ├── SecurityConfig.java        (Configuración de Spring Security)
│   │   ├── JacksonConfig.java         (Serialización JSON)
│   │   └── ApplicationConfig.java     (Beans de aplicación)
│   │
│   └── ServicioInventario.java        (Clase principal)
│
├── pom.xml                             (Dependencias Maven)
├── Dockerfile                          (Imagen Docker)
└── README.md                           (Este archivo)
```

## 🔧 Dependencias Principales

### Core
- `spring-boot-starter-web` - API REST
- `spring-boot-starter-data-jpa` - ORM
- `spring-boot-starter-security` - Seguridad

### Database
- `postgresql` - Driver PostgreSQL
- `spring-boot-starter-data-jpa` - JPA

### JWT
- `jjwt-api` - JWT API
- `jjwt-impl` - JWT Implementation
- `jjwt-jackson` - JWT Jackson support

### Utilities
- `lombok` - Anotaciones para reducir boilerplate
- `org.springframework.security:spring-security-crypto` - BCrypt

## 🔐 Autenticación y Seguridad

### Flujo de Autenticación

1. Usuario envía credenciales a `POST /controller/login`
2. Se validan contra la BD (contraseña con BCrypt)
3. Se genera JWT con rol incluido (válido 1 hora)
4. Token se devuelve al cliente
5. Cliente incluye token en header `Authorization: Bearer <token>`
6. `JwtAuthentificationFilter` valida el token
7. Si es válido, se establece contexto de seguridad con rol
8. `@PreAuthorize` valida permisos en cada endpoint

### Roles y Permisos

```
ADMIN:
  - POST /inventario/agregarProducto ✅
  - PUT /inventario/sumarProducto/{codigo}/{cantidad} ✅
  - PUT /inventario/descontarCantidad/{codigo}/{cantidad} ✅
  - DELETE /inventario/eliminarProducto/{codigo} ✅
  - GET /inventario/listadoProductos ✅
  - GET /inventario/listadoProductos/categoria/{id} ✅

VENDEDOR:
  - GET /inventario/listadoProductos ✅
  - GET /inventario/listadoProductos/categoria/{id} ✅
  - PUT /inventario/descontarCantidad/{codigo}/{cantidad} ✅
  - POST /inventario/agregarProducto ❌ (403 Forbidden)
```

## 📡 Endpoints Principales

### Autenticación
```
POST /controller/login
Headers: Content-Type: application/json
Body: {
  "nomUsuario": "admin",
  "contrasena": "contrasena123"
}
Response: {
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "rol": "ADMIN",
  "nomUsuario": "admin"
}
```

### Productos
```
GET /controller/inventario/listadoProductos
Headers: Authorization: Bearer <token>
Response: [{
  "idProducto": 1,
  "codigo": "PROD001",
  "nombreProducto": "Producto 1",
  "precio": 10000,
  "cantidad": 50,
  "categoria": {
    "id": 1,
    "nombre": "Aseo",
    "descripcion": "..."
  }
}, ...]

GET /controller/inventario/listadoProductos/categoria/1
Response: [productos de categoría 1]

POST /controller/inventario/agregarProducto
Headers: Authorization: Bearer <token>, Content-Type: application/json
Body: {
  "codigo": "PROD002",
  "nombreProducto": "Nuevo Producto",
  "precio": 15000,
  "cantidad": 30,
  "categoriaId": 2
}
Response: {producto creado}

PUT /controller/inventario/sumarProducto/PROD001/10
Response: {producto actualizado con nueva cantidad}

PUT /controller/inventario/descontarCantidad/PROD001/5
Response: {producto actualizado}

DELETE /controller/inventario/eliminarProducto/PROD001
Response: 204 No Content
```

### Categorías
```
GET /controller/categorias
Response: [{
  "id": 1,
  "nombre": "Aseo",
  "descripcion": "Productos de limpieza"
}, ...]
```

## 🗄️ Base de Datos

### Entidades

#### Categoria
```java
@Entity
public class Categoria {
  @Id
  @GeneratedValue
  Integer id;
  
  String nombre;      // NOT NULL, UNIQUE
  String descripcion;
  
  @OneToMany(mappedBy = "categoria")
  List<Producto> productos;
}
```

#### Producto
```java
@Entity
public class Producto {
  @Id
  @GeneratedValue
  @Column(name = "id_producto")
  Integer idProducto;
  
  String codigo;        // NOT NULL, UNIQUE
  String nombreProducto; // NOT NULL
  Integer precio;
  Integer cantidad;
  
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "categoria_id", nullable = true)
  Categoria categoria;
}
```

#### Usuario
```java
@Entity
public class Usuario implements UserDetails {
  @Id
  @GeneratedValue
  Integer id;
  
  String nomUsuario;  // NOT NULL, UNIQUE
  String contrasena;  // BCrypt
  String rol;         // ADMIN, VENDEDOR
  
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_" + rol));
  }
}
```

## 🏃‍♂️ Ejecución Local

### Prerequisitos
- Java 21+
- Maven 3.8+
- PostgreSQL 16 en puerto 5432

### Pasos

```bash
# 1. Configurar variables de entorno
export DATABASE_URL=jdbc:postgresql://localhost:5432/inventario
export DATABASE_USER=postgres
export DATABASE_PASSWORD=postgres

# 2. Compilar
mvn clean package

# 3. Ejecutar
java -jar target/ServicioInventario-0.0.1-SNAPSHOT.jar

# 4. API disponible en http://localhost:8081
```

### Con Docker Compose

```bash
# Desde la raíz del proyecto
docker compose up backend -d

# Logs
docker logs inventario-backend
```

## 🧪 Testing

```bash
# Ejecutar tests
mvn test

# Con cobertura
mvn test jacoco:report
```

## 🔍 Debugging

### Ver logs del backend
```bash
docker logs -f inventario-backend
```

### Aumentar verbosidad de logs
Modificar `application.properties`:
```properties
logging.level.com.lta.inventario=DEBUG
logging.level.org.springframework.security=DEBUG
```

## 📋 Configuración

### application.properties
```properties
spring.datasource.url=jdbc:postgresql://db:5432/inventario
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

### SecurityConfig
- CORS: Permite localhost:3000
- CSRF: Deshabilitado (stateless)
- Session: STATELESS (JWT)
- Autenticación: Token JWT

## 🚢 Deployment

### Docker Build
```bash
docker build -t inventario-backend .

docker run -d \
  --name inventario-backend \
  -p 8081:8081 \
  -e DATABASE_URL=jdbc:postgresql://host.docker.internal:5432/inventario \
  -e DATABASE_USER=postgres \
  -e DATABASE_PASSWORD=postgres \
  inventario-backend
```

## 📊 Monitoreo

### Endpoints de salud
```
GET /controller/health
GET /actuator/health (si actuator está habilitado)
```

### Métricas
Las métricas están disponibles en `/actuator/metrics` si Spring Boot Actuator está configurado.

## 🐛 Problemas Comunes

### Error: "Cannot deserialize categoriaId"
**Solución:** Verificar que ProductoRequest tiene los campos correctos con getters/setters

### Error: "LazyInitializationException"
**Solución:** Cambiar `FetchType.LAZY` a `FetchType.EAGER` en la relación

### Error: "403 Forbidden"
**Solución:** Verificar que el token tiene el rol correcto y que `@EnableMethodSecurity` está habilitado

## 📚 Archivos Importantes

- `ServicioInventario.java` - Clase principal con `@SpringBootApplication`
- `SecurityConfig.java` - Configuración completa de seguridad
- `JwtService.java` - Generación y validación de tokens
- `AutentController.java` - Todos los endpoints REST
- `InventarioService.java` - Lógica de negocio

## 🔗 Enlaces

- [Spring Boot Docs](https://spring.io/projects/spring-boot)
- [Spring Security](https://spring.io/projects/spring-security)
- [JWT.io](https://jwt.io/)
- [PostgreSQL](https://www.postgresql.org/)

---

**Versión:** 1.0.0
**Última actualización:** 2026-05-20
**Autor:** Equipo de Desarrollo - Unimonserrate
