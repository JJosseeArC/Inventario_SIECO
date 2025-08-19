# Inventario SIECO — Versión 1

Aplicación de inventario de **Materiales** y **Herramientas/Equipos** con **Swing (Java)** y **MySQL**.  
Incluye: gestión básica, solicitudes con aprobación/entrega, devoluciones parciales y cambio de contraseña de administrador.

> Nota: Proyecto personal basado en procesos reales. No constituye software oficial de SIECO.

---

## Tecnologías

- Java 17
- Swing (UI de escritorio)
- MySQL 8.x
- Maven

---

## Estructura (módulos principales)

- 'com.sieco.inventario.ui'
  - 'MainFrame' — Ventana principal con pestañas
  - 'MaterialPanel' — CRUD visual de materiales
  - 'HerramientaPanel' — CRUD visual de herramientas/equipos
  - 'SolicitudPanel' — Alta de solicitudes, aprobar/rechazar, entregar, devolver, ver detalles
  - 'AdministracionPanel' — Cambio de contraseña admin
- 'com.sieco.inventario.dao'
  - 'MaterialDAO', 'HerramientaDAO', 'SolicitudDAO', 'SolicitudDetalleDAO'
- 'com.sieco.inventario.modelo'
  - Modelos POJO: 'Material', 'Herramienta', 'Solicitud', 'SolicitudDetalle'
- 'com.sieco.inventario.util'
  - 'ConexionBD' (MySQL)
  - 'AdminAuth' (gestiona 'admin_pswd.txt')

---

## Funcionalidades (V1)

### Materiales
- Alta/edición/listado.
- Stock en 'cantidad_disponible'.
- Descuento de stock al entregar una solicitud.
- Devolución parcial: permite sumar al stock.

### Herramientas / Equipos
- Alta/edición/listado.
- Sin cantidad; cada ítem es único.
- Estados: 'DISPONIBLE' / 'NO_DISPONIBLE'.
- Al entregar: se marca 'NO_DISPONIBLE'.
- Al devolver: se marca 'DISPONIBLE'.

### Solicitudes
- Alta con texto libre en V1: 'cuadrilla', 'supervisor', 'obra' (campos de texto).
- Agregar materiales (cantidad variable) y herramientas (1 por fila).
- Aprobar / Rechazar:
  - Valida stock (material) y disponibilidad (herramienta).
- Entregar:
  - Material: resta stock.
  - Herramienta: cambia a 'NO_DISPONIBLE'.
  - Si incluye herramientas → estado: 'pendiente_de_devolucion'; si no → 'entregada'.
- Ver detalles: tabla con ítems antes de aprobar/entregar.
- Devolución:
  - Material: elegir cantidad a devolver.
  - Herramienta: marcar si se devuelve (vuelve a 'DISPONIBLE').
  - Si quedan herramientas sin devolver → 'pendiente_de_devolucion'; si no, 'devuelta'.

### Administración
- Cambiar contraseña de administrador (se guarda en 'admin_pswd.txt').
- Si no existe el archivo 'admin_pswd.txt', se crea el archivo con clave por defecto: 'admin123'.


---

## Ejecución

- Generado archivo ejecutable 'Inventario_SIECO_v1.jar'.
- Requiere Java 17 y base de datos MySQL configurada.
- La primera vez que se ejecute, se generará el archivo 'admin_pswd.txt' con la contraseña por defecto 'admin123'.

---

## Estado actual

- CRUD funcional para materiales y herramientas.
- Flujo de solicitudes completo: aprobar, entregar, devolver.
- Control de stock y disponibilidad implementado.
- GUI operativa y estable en Swing.
- Ejecutable JAR incluido ('Inventario_SIECO_v1.jar').

---

## Limitaciones

- En V1 los campos de 'obra', 'cuadrilla' y 'supervisor' son texto libre, no catálogos.
- No existe control de usuarios (solo administrador con clave).
- Base de datos pensada para entorno local (no red/multiusuario).

---

## Próximos pasos (V2 y posteriores)

- Implementar catálogos para obras, cuadrillas y supervisores.
- Rediseño de la interfaz con mejoras visuales y usabilidad.
- Migración a base de datos en red para uso multiusuario.


---

## Base de Datos

### Opción A (V1)
Usa el script `SQL/sieco_estructura.sql` pero aplícale estos cambios para **texto libre** y **opcionalidad** en V1:

```sql
USE sieco;

ALTER TABLE solicitud
  ADD COLUMN obra_text VARCHAR(120) NULL AFTER supervisor_id,
  MODIFY COLUMN cuadrilla_id INT NULL,
  MODIFY COLUMN supervisor_id INT NULL;

---
