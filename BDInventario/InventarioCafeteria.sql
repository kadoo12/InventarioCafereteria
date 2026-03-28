USE InventarioCafeteria
GO

---**CREACION DE TABLAS**---
CREATE TABLE Usuario(
id INT PRIMARY KEY NOT NULL,
nom_usuario VARCHAR (30) NOT NULL,
contrasena BINARY(32) NOT NULL)

---**INGRESAR USUARIOS**---
INSERT INTO Usuario (id,nom_usuario,contrasena)
VALUES(2,'omaarg',HASHBYTES('SHA2_256','contrasena123'));

INSERT INTO Usuario (id,nom_usuario,contrasena)
VALUES(3,'SebasJ',HASHBYTES('SHA2_256','inventario2026'));


---**CONSULTAS**---
SELECT * FROM dbo.Usuario
SELECT * FROM dbo.producto

SELECT nombre_producto, cantidad
FROM dbo.producto
WHERE nombre_producto = 'mandarina'

--VALIDAR EXISTENCIA POR CONTRASEÑA--
SELECT nom_usuario
FROM Usuario
WHERE nom_usuario = 'omarg'
AND contrasena = HASHBYTES('SHA2_256', 'contrasena123')

SELECT nom_usuario
FROM Usuario
WHERE nom_usuario = 'SebasJ'
AND contrasena = HASHBYTES('SHA2_256', 'inventario2026')

---**MODIFICACIONES**---
ALTER TABLE Usuario ALTER COLUMN contrasena VARCHAR(255) NOT NULL;

UPDATE Usuario 
SET contrasena = '$2a$10$sRW.TNm2CIGXAhNBVaxXwe3NDOMlF0r3ARUc1XKPWcoQFcZL6j/Bi' 
WHERE nom_usuario = 'omaarg';

UPDATE Usuario 
SET contrasena = '$2a$10$khX24cay0U6yMeFYVCk.nes5on0UDqElBMUhb06BHR2k/7pH9m3Wa' 
WHERE nom_usuario = 'SebasJ';

