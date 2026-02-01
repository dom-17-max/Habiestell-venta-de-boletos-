# Habiestell-venta-de-boletos-
# Sistema de Gestión de Rifa de Casa

## 📋 Tabla de Contenidos
- [Contexto del Problema](#contexto-del-problema)
- [Análisis de Requerimientos](#análisis-de-requerimientos)
- [Modelo Lógico](#modelo-lógico)
- [Descripción de Tablas Principales](#descripción-de-tablas-principales)
- [Script del Modelo Físico](#script-del-modelo-físico)
- [Instalación y Configuración](#instalación-y-configuración)
- [Funcionalidades de la Aplicación](#funcionalidades-de-la-aplicación)
- [Tecnologías Utilizadas](#tecnologías-utilizadas)
- [Autores](#autores)

---

## 🎯 Contexto del Problema

En la actualidad, muchas organizaciones, instituciones benéficas y personas particulares realizan rifas de casas, vehículos u otros bienes de alto valor como método de recaudación de fondos o venta de propiedades. Sin embargo, la gestión de estas rifas presenta diversos desafíos cuando se realiza de manera manual:

### Problemática Identificada

1. **Pérdida de Información**: Los registros en papel pueden extraviarse, deteriorarse o ser modificados sin control de versiones.

2. **Duplicidad de Números**: Al manejar múltiples puntos de venta, existe el riesgo de vender el mismo número de boleto más de una vez, generando conflictos legales.

3. **Dificultad para Consultas**: Buscar información específica requiere revisar manualmente todos los registros, lo cual es lento e ineficiente.

4. **Falta de Trazabilidad**: No existe un historial claro de las transacciones realizadas, fechas de venta, o métodos de pago.

5. **Proceso de Sorteo Ineficiente**: El proceso de selección del ganador puede verse comprometido si no se tiene un registro exacto y verificable.

### Solución Propuesta

Desarrollo de un **Sistema de Gestión de Rifa** que automatiza y optimiza todo el proceso de venta de boletos, garantizando:
- ✅ Unicidad de cada número vendido
- ✅ Registro seguro y persistente de datos
- ✅ Consultas rápidas y eficientes
- ✅ Generación automática de comprobantes
- ✅ Trazabilidad completa de transacciones
- ✅ Sorteo transparente y verificable

---

## 📊 Análisis de Requerimientos

### Requerimientos Funcionales

#### RF01: Gestión de Clientes
- **RF01.1**: El sistema debe permitir registrar nuevos clientes con los datos: nombre, cédula y teléfono.
- **RF01.2**: El sistema debe validar que no existan clientes duplicados por cédula.
- **RF01.3**: El sistema debe permitir consultar el listado completo de clientes registrados.
- **RF01.4**: El sistema debe permitir visualizar el historial de compras de cada cliente.

#### RF02: Gestión de Ventas
- **RF02.1**: El sistema debe permitir registrar ventas de boletos asociadas a un cliente.
- **RF02.2**: El sistema debe generar números de rifa únicos de 4 dígitos (0000-9999).
- **RF02.3**: El sistema debe permitir vender múltiples boletos en una sola transacción.
- **RF02.4**: El sistema debe generar recibos de venta detallados.
- **RF02.5**: El sistema debe solicitar confirmación antes de finalizar una venta.
- **RF02.6**: El sistema debe permitir cancelar una venta antes de su confirmación.

#### RF03: Gestión de Boletos
- **RF03.1**: El sistema debe garantizar que no se generen números de boleto duplicados.
- **RF03.2**: El sistema debe asociar cada boleto a una venta específica.
- **RF03.3**: El sistema debe registrar el precio de cada boleto al momento de la venta.
- **RF03.4**: El sistema debe permitir consultar todos los números vendidos.
- **RF03.5**: El sistema debe calcular automáticamente los números disponibles.

#### RF04: Consultas y Reportes
- **RF04.1**: El sistema debe mostrar el historial completo de ventas realizadas.
- **RF04.2**: El sistema debe permitir visualizar todos los números vendidos ordenados.
- **RF04.3**: El sistema debe calcular estadísticas en tiempo real (total vendido, disponible, porcentaje).
- **RF04.4**: El sistema debe generar resúmenes de recaudación total.

#### RF05: Configuración del Sistema
- **RF05.1**: El sistema debe permitir modificar el precio del boleto.
- **RF05.2**: El sistema debe validar que el precio sea mayor a 0.
- **RF05.3**: El sistema debe mostrar el precio actual en el menú principal.

#### RF06: Sorteo (Funcionalidad Futura)
- **RF06.1**: El sistema debe permitir seleccionar un ganador de forma aleatoria.
- **RF06.2**: El sistema debe marcar el boleto ganador.
- **RF06.3**: El sistema debe mostrar la información completa del ganador.

### Requerimientos No Funcionales

#### RNF01: Usabilidad
- La interfaz debe ser intuitiva y fácil de usar.
- Los mensajes de error deben ser claros y orientar al usuario.
- Las opciones del menú deben estar claramente etiquetadas.

#### RNF02: Rendimiento
- Las consultas a la base de datos deben ejecutarse en menos de 2 segundos.
- La generación de números únicos debe ser instantánea.
- El sistema debe soportar hasta 10,000 boletos simultáneos.

#### RNF03: Seguridad
- El sistema debe validar todas las entradas del usuario.
- La conexión a la base de datos debe usar credenciales seguras.
- No debe ser posible inyectar código SQL a través de formularios.

#### RNF04: Mantenibilidad
- El código debe seguir las convenciones de nomenclatura de Java.
- El código debe estar documentado con comentarios claros.
- El sistema debe usar programación orientada a objetos.

#### RNF05: Portabilidad
- El sistema debe funcionar en Windows, Linux y macOS.
- Debe requerir únicamente Java 8+ y MySQL 8.0+.

---

## 🗂️ Modelo Lógico

### Diagrama Entidad-Relación

```
┌─────────────────┐         ┌─────────────────┐         ┌─────────────────┐
│    CLIENTES     │         │     VENTAS      │         │    BOLETOS      │
├─────────────────┤         ├─────────────────┤         ├─────────────────┤
│ PK id_cliente   │────┐    │ PK id_venta     │────┐    │ PK id_boleto    │
│    nombre       │    │    │ FK id_cliente   │    │    │ FK id_venta     │
│    cedula (UK)  │    └───→│    numero_venta │    └───→│    numero_rifa  │
│    telefono     │         │    total        │         │    precio       │
│    fecha_reg    │         │    fecha_venta  │         │    fecha_gen    │
└─────────────────┘         └─────────────────┘         │    ganador      │
                                                         └─────────────────┘
                            ┌─────────────────┐
                            │  CONFIGURACION  │
                            ├─────────────────┤
                            │ PK id_config    │
                            │    nombre_param │
                            │    valor_param  │
                            │    descripcion  │
                            │    fecha_modif  │
                            └─────────────────┘
```

### Relaciones

1. **CLIENTES (1) ──→ (N) VENTAS**
   - Un cliente puede realizar múltiples ventas
   - Una venta pertenece a un único cliente
   - Relación: 1:N (Uno a Muchos)
   - Clave Foránea: `ventas.id_cliente` → `clientes.id_cliente`

2. **VENTAS (1) ──→ (N) BOLETOS**
   - Una venta puede contener múltiples boletos
   - Un boleto pertenece a una única venta
   - Relación: 1:N (Uno a Muchos)
   - Clave Foránea: `boletos.id_venta` → `ventas.id_venta`

3. **Integridad Referencial**
   - `ON DELETE CASCADE`: Al eliminar un cliente, se eliminan sus ventas y boletos
   - Garantiza la consistencia de los datos

---

## 📁 Descripción de Tablas Principales

### Tabla: `clientes`

**Propósito**: Almacenar la información de todos los clientes que participan en la rifa.

| Campo | Tipo | Restricciones | Descripción |
|-------|------|---------------|-------------|
| `id_cliente` | INT | PRIMARY KEY, AUTO_INCREMENT | Identificador único del cliente |
| `nombre` | VARCHAR(100) | NOT NULL | Nombre completo del cliente |
| `cedula` | VARCHAR(20) | UNIQUE, NOT NULL | Cédula o DNI (identificador legal) |
| `telefono` | VARCHAR(20) | NOT NULL | Número de contacto |
| `fecha_registro` | DATETIME | DEFAULT CURRENT_TIMESTAMP | Fecha de registro en el sistema |

**Índices**:
- PRIMARY KEY en `id_cliente`
- UNIQUE INDEX en `cedula` (previene duplicados)
- INDEX en `cedula` (optimiza búsquedas)

**Relevancia**: Esta tabla es fundamental ya que todos los boletos deben estar asociados a un cliente registrado. Permite mantener el control de quiénes participan en la rifa y sus datos de contacto para notificaciones.

---

### Tabla: `ventas`

**Propósito**: Registrar cada transacción de venta de boletos realizada en el sistema.

| Campo | Tipo | Restricciones | Descripción |
|-------|------|---------------|-------------|
| `id_venta` | INT | PRIMARY KEY, AUTO_INCREMENT | Identificador único de la venta |
| `id_cliente` | INT | FOREIGN KEY, NOT NULL | Cliente que realizó la compra |
| `numero_venta` | INT | UNIQUE, NOT NULL | Número consecutivo de venta |
| `total` | DECIMAL(10,2) | NOT NULL | Monto total de la transacción |
| `fecha_venta` | DATETIME | DEFAULT CURRENT_TIMESTAMP | Fecha y hora de la venta |

**Relaciones**:
- FOREIGN KEY `id_cliente` → `clientes(id_cliente)` ON DELETE CASCADE

**Índices**:
- PRIMARY KEY en `id_venta`
- UNIQUE INDEX en `numero_venta`
- INDEX en `id_cliente` (optimiza consultas por cliente)
- INDEX en `fecha_venta` (optimiza reportes por período)

**Relevancia**: Actúa como punto intermedio entre clientes y boletos. Permite agrupar múltiples boletos en una sola transacción y mantener el registro histórico de ventas para auditoría y reportes financieros.

---

### Tabla: `boletos`

**Propósito**: Almacenar cada boleto de rifa vendido con su número único.

| Campo | Tipo | Restricciones | Descripción |
|-------|------|---------------|-------------|
| `id_boleto` | INT | PRIMARY KEY, AUTO_INCREMENT | Identificador único del boleto |
| `id_venta` | INT | FOREIGN KEY, NOT NULL | Venta a la que pertenece |
| `numero_rifa` | VARCHAR(4) | UNIQUE, NOT NULL | Número del boleto (0000-9999) |
| `precio` | DECIMAL(10,2) | NOT NULL | Precio pagado (puede variar) |
| `fecha_generacion` | DATETIME | DEFAULT CURRENT_TIMESTAMP | Fecha de generación |
| `ganador` | BOOLEAN | DEFAULT FALSE | Indica si ganó el sorteo |

**Relaciones**:
- FOREIGN KEY `id_venta` → `ventas(id_venta)` ON DELETE CASCADE

**Índices**:
- PRIMARY KEY en `id_boleto`
- UNIQUE INDEX en `numero_rifa` (garantiza unicidad)
- INDEX en `id_venta` (optimiza consultas por venta)
- INDEX en `ganador` (optimiza consulta de ganadores)

**Relevancia**: Esta es la tabla central del sistema. Garantiza que cada número de rifa sea único y permite rastrear quién compró qué número, cuándo y a qué precio. Es esencial para el sorteo y la entrega de premios.

---

### Tabla: `configuracion`

**Propósito**: Almacenar parámetros configurables del sistema.

| Campo | Tipo | Restricciones | Descripción |
|-------|------|---------------|-------------|
| `id_config` | INT | PRIMARY KEY, AUTO_INCREMENT | Identificador de configuración |
| `nombre_parametro` | VARCHAR(50) | UNIQUE, NOT NULL | Nombre del parámetro |
| `valor_parametro` | VARCHAR(100) | NOT NULL | Valor actual del parámetro |
| `descripcion` | VARCHAR(255) | NULL | Descripción del parámetro |
| `fecha_modificacion` | DATETIME | DEFAULT CURRENT_TIMESTAMP ON UPDATE | Última modificación |

**Índices**:
- PRIMARY KEY en `id_config`
- UNIQUE INDEX en `nombre_parametro`

**Parámetros Almacenados**:
- `precio_boleto`: Precio actual de cada boleto

**Relevancia**: Permite modificar configuraciones del sistema sin alterar el código. Facilita el mantenimiento y la adaptación a diferentes escenarios de negocio.

---

## 💾 Script del Modelo Físico

El script completo SQL para crear la base de datos se encuentra en el archivo [`sistema_rifa.sql`](./sistema_rifa.sql).

### Creación de Base de Datos

```sql
DROP DATABASE IF EXISTS sistema_rifa;
CREATE DATABASE sistema_rifa;
USE sistema_rifa;
```

### Creación de Tablas

```sql
-- Tabla de clientes
CREATE TABLE clientes (
    id_cliente INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    cedula VARCHAR(20) NOT NULL UNIQUE,
    telefono VARCHAR(20) NOT NULL,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_cedula (cedula)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla de ventas
CREATE TABLE ventas (
    id_venta INT PRIMARY KEY AUTO_INCREMENT,
    id_cliente INT NOT NULL,
    numero_venta INT NOT NULL UNIQUE,
    total DECIMAL(10, 2) NOT NULL,
    fecha_venta DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente) ON DELETE CASCADE,
    INDEX idx_cliente (id_cliente),
    INDEX idx_fecha (fecha_venta)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla de boletos
CREATE TABLE boletos (
    id_boleto INT PRIMARY KEY AUTO_INCREMENT,
    id_venta INT NOT NULL,
    numero_rifa VARCHAR(4) NOT NULL UNIQUE,
    precio DECIMAL(10, 2) NOT NULL,
    fecha_generacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    ganador BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_venta) REFERENCES ventas(id_venta) ON DELETE CASCADE,
    INDEX idx_numero_rifa (numero_rifa),
    INDEX idx_venta (id_venta),
    INDEX idx_ganador (ganador)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla de configuración
CREATE TABLE configuracion (
    id_config INT PRIMARY KEY AUTO_INCREMENT,
    nombre_parametro VARCHAR(50) NOT NULL UNIQUE,
    valor_parametro VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    fecha_modificacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

### Vistas Útiles

```sql
-- Vista: Resumen de ventas por cliente
CREATE VIEW vista_ventas_por_cliente AS
SELECT 
    c.id_cliente,
    c.nombre,
    c.cedula,
    COUNT(DISTINCT v.id_venta) AS total_ventas,
    COUNT(b.id_boleto) AS total_boletos_comprados,
    SUM(v.total) AS total_gastado
FROM clientes c
LEFT JOIN ventas v ON c.id_cliente = v.id_cliente
LEFT JOIN boletos b ON v.id_venta = b.id_venta
GROUP BY c.id_cliente;

-- Vista: Estadísticas generales
CREATE VIEW vista_estadisticas_generales AS
SELECT 
    (SELECT COUNT(*) FROM clientes) AS total_clientes,
    (SELECT COUNT(*) FROM ventas) AS total_ventas,
    (SELECT COUNT(*) FROM boletos) AS total_boletos_vendidos,
    (10000 - (SELECT COUNT(*) FROM boletos)) AS boletos_disponibles,
    (SELECT SUM(total) FROM ventas) AS total_recaudado;
```

### Procedimientos Almacenados

El script incluye los siguientes procedimientos:
- `sp_registrar_cliente`: Registra un nuevo cliente con validaciones
- `sp_registrar_venta`: Registra una venta completa con múltiples boletos
- `sp_actualizar_precio_boleto`: Actualiza el precio del boleto
- `sp_sortear_ganador`: Selecciona un ganador aleatoriamente

---

## ⚙️ Instalación y Configuración

### Prerrequisitos

- Java JDK 8 o superior
- MySQL 8.0 o superior
- Maven 3.6 o superior
- IDE (NetBeans, IntelliJ IDEA, Eclipse)

### Pasos de Instalación

1. **Clonar el repositorio**
```bash
git clone https://github.com/[tu-usuario]/sistema-rifa-casa.git
cd sistema-rifa-casa
```

2. **Crear la base de datos**
```bash
mysql -u root -p < sistema_rifa.sql
```

3. **Configurar la conexión**
Editar el archivo de configuración con tus credenciales de MySQL.

4. **Compilar el proyecto**
```bash
mvn clean compile
```

5. **Ejecutar la aplicación**
```bash
mvn exec:java
```

---

## 🚀 Funcionalidades de la Aplicación

### 1. Gestión de Clientes
- ✅ Registro de nuevos clientes
- ✅ Validación de cédula única
- ✅ Visualización de clientes registrados

### 2. Gestión de Ventas
- ✅ Venta de boletos (1-50 por transacción)
- ✅ Generación automática de números únicos
- ✅ Confirmación de compra
- ✅ Generación de recibos

### 3. Consultas y Reportes
- ✅ Historial completo de ventas
- ✅ Números vendidos (ordenados)
- ✅ Estadísticas en tiempo real
- ✅ Resumen de recaudación

### 4. Configuración
- ✅ Modificación de precio de boleto
- ✅ Validaciones de entrada

---

## 🛠️ Tecnologías Utilizadas

- **Lenguaje**: Java 8+
- **Framework**: Maven
- **Base de Datos**: MySQL 8.0
- **Conectividad**: JDBC
- **Interfaz**: Swing (GUI) / Consola
- **Control de Versiones**: Git

---

## 👥 Autores

- **Nombre**: [Tu Nombre]
- **Curso**: Segundo de Informática
- **Institución**: Unidad Educativa Academia Naval Almirante Illingworth
- **Año**: 2025-2026

---

## 📝 Licencia

Este proyecto es de uso educativo para el ABP de Programación y Base de Datos.

---

## 📧 Contacto

Para consultas o sugerencias, contactar a: [tu-email@ejemplo.com]
