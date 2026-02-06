# Habiestell-venta-de-boletos-
# Sistema de Gestión de Rifa de Casa

## 📋 Tabla de Contenidos
- [Contexto del Problema](#contexto-del-problema)
- [Análisis de Requerimientos](#análisis-de-requerimientos)
- [Descripción de Tablas Principales](#descripción-de-tablas-principales)
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

- **Nombre**:Aumala Domenika
- **Curso**: 2 de Informática
- **Institución**: Unidad Educativa Academia Naval Almirante Illingworth
- **Año**: 2025-2026

---

## 📝 Licencia

Este proyecto es de uso educativo para el ABP de Programación y Base de Datos.

---

## 📧 Contacto

Para consultas o sugerencias, contactar a: 228996@estudiantes.anai.edu.ec 
