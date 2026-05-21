# Backup de Base de Datos - Sistema de Inventario

## 📅 Fecha de Backup
2026-05-20 23:30

## 📂 Archivos de Backup

### Archivos principales:

1. **inventario_backup_completo_*.backup** 
   - Formato: PostgreSQL Custom Format (-Fc)
   - Contiene: Estructura + Datos completos
   - Uso: Para restaurar con `pg_restore` (recomendado)
   - Ventaja: Más comprimido y seguro

2. **inventario_backup_final_*.sql**
   - Formato: SQL plano
   - Contiene: Estructura + Datos
   - Uso: Para restaurar con `psql`
   - Ventaja: Legible en cualquier editor de texto

3. **tabla_usuario_backup.txt**
   - Datos de la tabla usuario
   - Usuarios de prueba guardados

### Archivos adicionales (legacy):
- insert_usuario.sql
- insert_vendedor.sql
- update_password.sql
- InventarioCafeteria.bak
- InventarioCafeteria.sql

## 🔄 Cómo restaurar la base de datos

### Opción 1: Usando el formato custom (.backup) - RECOMENDADO

```bash
# Dentro del contenedor PostgreSQL
docker exec -it practical_keller pg_restore -U postgres -d inventario /ruta/al/inventario_backup_completo_*.backup
```

### Opción 2: Usando el archivo SQL

```bash
# Dentro del contenedor PostgreSQL
docker exec -i practical_keller psql -U postgres -d inventario < C:\Users\omarg\ProyectoInventario\BDInventario\inventario_backup_final_*.sql
```

O desde dentro del contenedor:
```bash
psql -U postgres -d inventario < /ruta/al/backup.sql
```

## 📊 Contenido de la base de datos

La base de datos contiene 3 tablas principales:

### 1. **categoria**
- id (PK)
- nombre
- descripcion
- Categorías: Aseo, Comestibles, Electrodomésticos

### 2. **producto**
- id_producto (PK)
- codigo (UNIQUE)
- nombre_producto
- precio
- cantidad
- categoria_id (FK → categoria)

### 3. **usuario**
- id (PK)
- nom_usuario (UNIQUE)
- contrasena (BCrypt)
- rol (ADMIN, VENDEDOR)

## 👥 Usuarios incluidos en el backup

| Usuario | Contraseña | Rol |
|---------|-----------|-----|
| admin | contrasena123 | ADMIN |
| vendedor | contrasena123 | VENDEDOR |

## 🛡️ Información de seguridad

- Las contraseñas están encriptadas con BCrypt
- Los backups NO contienen credenciales de base de datos en texto plano
- Se puede compartir estos backups de forma segura

## 📋 Versión de PostgreSQL

- Versión dumped: 16.13
- Versión pg_dump: 16.13

## ✅ Verificación

Los backups fueron creados el 2026-05-20 con todos los productos y categorías 
agregadas durante el desarrollo del sistema.

---

**Última actualización:** 2026-05-20 23:30 UTC
