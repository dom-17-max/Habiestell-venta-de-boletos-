-- ============================================
-- SISTEMA DE RIFA HABISTELL
-- Base de Datos MySQL
-- ============================================

DROP DATABASE IF EXISTS habistell_rifa;
CREATE DATABASE habistell_rifa CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE habistell_rifa;

-- Tabla de Rifas
CREATE TABLE rifas (
    id_rifa INT AUTO_INCREMENT PRIMARY KEY,
    nombre_rifa VARCHAR(200) NOT NULL,
    descripcion_casa TEXT,
    direccion_casa VARCHAR(300),
    precio_boleto DECIMAL(10,2) NOT NULL,
    total_boletos INT NOT NULL,
    boletos_vendidos INT DEFAULT 0,
    fecha_inicio DATE NOT NULL,
    fecha_sorteo DATE NOT NULL,
    estado ENUM('activa', 'finalizada', 'cancelada') DEFAULT 'activa',
    imagen_casa VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_estado (estado),
    INDEX idx_fecha_sorteo (fecha_sorteo)
) ENGINE=InnoDB;

-- Tabla de Usuarios/Participantes
CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre_completo VARCHAR(150) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    direccion VARCHAR(300),
    ciudad VARCHAR(100),
    estado VARCHAR(100),
    codigo_postal VARCHAR(10),
    password_hash VARCHAR(255) NOT NULL,
    rol ENUM('participante', 'administrador') DEFAULT 'participante',
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ultimo_acceso TIMESTAMP NULL,
    activo BOOLEAN DEFAULT TRUE,
    INDEX idx_email (email),
    INDEX idx_telefono (telefono)
) ENGINE=InnoDB;

-- Tabla de Boletos
CREATE TABLE boletos (
    id_boleto INT AUTO_INCREMENT PRIMARY KEY,
    id_rifa INT NOT NULL,
    numero_boleto INT NOT NULL,
    id_usuario INT NULL,
    estado ENUM('disponible', 'reservado', 'vendido', 'cancelado') DEFAULT 'disponible',
    fecha_reserva TIMESTAMP NULL,
    fecha_compra TIMESTAMP NULL,
    monto_pagado DECIMAL(10,2),
    codigo_verificacion VARCHAR(50) UNIQUE,
    FOREIGN KEY (id_rifa) REFERENCES rifas(id_rifa) ON DELETE CASCADE,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE SET NULL,
    UNIQUE KEY unique_boleto_rifa (id_rifa, numero_boleto),
    INDEX idx_estado (estado),
    INDEX idx_usuario (id_usuario),
    INDEX idx_numero (numero_boleto)
) ENGINE=InnoDB;

-- Tabla de Pagos
CREATE TABLE pagos (
    id_pago INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_boleto INT NOT NULL,
    metodo_pago ENUM('efectivo', 'transferencia', 'tarjeta', 'paypal', 'stripe') NOT NULL,
    monto DECIMAL(10,2) NOT NULL,
    estado_pago ENUM('pendiente', 'confirmado', 'rechazado', 'reembolsado') DEFAULT 'pendiente',
    referencia_pago VARCHAR(100),
    comprobante_url VARCHAR(500),
    fecha_pago TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_confirmacion TIMESTAMP NULL,
    notas TEXT,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_boleto) REFERENCES boletos(id_boleto) ON DELETE CASCADE,
    INDEX idx_estado_pago (estado_pago),
    INDEX idx_fecha_pago (fecha_pago)
) ENGINE=InnoDB;

-- Tabla de Sorteos
CREATE TABLE sorteos (
    id_sorteo INT AUTO_INCREMENT PRIMARY KEY,
    id_rifa INT NOT NULL,
    numero_ganador INT NOT NULL,
    id_boleto_ganador INT NOT NULL,
    id_usuario_ganador INT NOT NULL,
    fecha_sorteo TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    metodo_sorteo VARCHAR(200),
    testigos TEXT,
    video_sorteo_url VARCHAR(500),
    acta_sorteo_url VARCHAR(500),
    notificado BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_rifa) REFERENCES rifas(id_rifa) ON DELETE CASCADE,
    FOREIGN KEY (id_boleto_ganador) REFERENCES boletos(id_boleto),
    FOREIGN KEY (id_usuario_ganador) REFERENCES usuarios(id_usuario),
    INDEX idx_rifa (id_rifa)
) ENGINE=InnoDB;

-- Tabla de Transacciones (log de actividades)
CREATE TABLE transacciones_log (
    id_log INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT,
    tipo_accion ENUM('registro', 'compra', 'pago', 'cancelacion', 'sorteo', 'login') NOT NULL,
    descripcion TEXT,
    ip_address VARCHAR(45),
    user_agent TEXT,
    fecha_accion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE SET NULL,
    INDEX idx_fecha (fecha_accion),
    INDEX idx_tipo (tipo_accion)
) ENGINE=InnoDB;

-- ============================================
-- STORED PROCEDURES
-- ============================================

-- Procedimiento para reservar boletos
DELIMITER //
CREATE PROCEDURE reservar_boletos(
    IN p_id_rifa INT,
    IN p_id_usuario INT,
    IN p_cantidad INT
)
BEGIN
    DECLARE v_count INT;
    DECLARE v_precio DECIMAL(10,2);
    
    -- Verificar boletos disponibles
    SELECT COUNT(*) INTO v_count 
    FROM boletos 
    WHERE id_rifa = p_id_rifa AND estado = 'disponible';
    
    IF v_count >= p_cantidad THEN
        -- Obtener precio del boleto
        SELECT precio_boleto INTO v_precio FROM rifas WHERE id_rifa = p_id_rifa;
        
        -- Reservar boletos
        UPDATE boletos 
        SET estado = 'reservado', 
            id_usuario = p_id_usuario,
            fecha_reserva = NOW()
        WHERE id_rifa = p_id_rifa 
        AND estado = 'disponible'
        LIMIT p_cantidad;
        
        SELECT 'Boletos reservados exitosamente' AS mensaje, p_cantidad AS cantidad, v_precio * p_cantidad AS total;
    ELSE
        SELECT 'No hay suficientes boletos disponibles' AS mensaje, v_count AS disponibles;
    END IF;
END //
DELIMITER ;

-- Procedimiento para confirmar compra
DELIMITER //
CREATE PROCEDURE confirmar_compra(
    IN p_id_boleto INT,
    IN p_id_pago INT
)
BEGIN
    UPDATE boletos 
    SET estado = 'vendido', 
        fecha_compra = NOW()
    WHERE id_boleto = p_id_boleto;
    
    UPDATE pagos 
    SET estado_pago = 'confirmado',
        fecha_confirmacion = NOW()
    WHERE id_pago = p_id_pago;
    
    -- Actualizar contador de boletos vendidos
    UPDATE rifas r
    INNER JOIN boletos b ON r.id_rifa = b.id_rifa
    SET r.boletos_vendidos = (
        SELECT COUNT(*) FROM boletos 
        WHERE id_rifa = b.id_rifa AND estado = 'vendido'
    )
    WHERE b.id_boleto = p_id_boleto;
    
    SELECT 'Compra confirmada exitosamente' AS mensaje;
END //
DELIMITER ;

-- Procedimiento para realizar sorteo
DELIMITER //
CREATE PROCEDURE realizar_sorteo(
    IN p_id_rifa INT
)
BEGIN
    DECLARE v_numero_ganador INT;
    DECLARE v_id_boleto_ganador INT;
    DECLARE v_id_usuario_ganador INT;
    
    -- Seleccionar un boleto ganador al azar de los vendidos
    SELECT id_boleto, numero_boleto, id_usuario
    INTO v_id_boleto_ganador, v_numero_ganador, v_id_usuario_ganador
    FROM boletos
    WHERE id_rifa = p_id_rifa AND estado = 'vendido'
    ORDER BY RAND()
    LIMIT 1;
    
    -- Insertar resultado del sorteo
    INSERT INTO sorteos (id_rifa, numero_ganador, id_boleto_ganador, id_usuario_ganador, metodo_sorteo)
    VALUES (p_id_rifa, v_numero_ganador, v_id_boleto_ganador, v_id_usuario_ganador, 'Aleatorio MySQL RAND()');
    
    -- Actualizar estado de la rifa
    UPDATE rifas SET estado = 'finalizada' WHERE id_rifa = p_id_rifa;
    
    SELECT v_numero_ganador AS numero_ganador, v_id_usuario_ganador AS id_ganador;
END //
DELIMITER ;

-- ============================================
-- VISTAS ÚTILES
-- ============================================

-- Vista de estadísticas de rifa
CREATE VIEW vista_estadisticas_rifa AS
SELECT 
    r.id_rifa,
    r.nombre_rifa,
    r.total_boletos,
    r.boletos_vendidos,
    r.precio_boleto,
    (r.boletos_vendidos * r.precio_boleto) AS ingresos_totales,
    ROUND((r.boletos_vendidos / r.total_boletos) * 100, 2) AS porcentaje_vendido,
    r.fecha_sorteo,
    r.estado,
    COUNT(DISTINCT b.id_usuario) AS total_participantes
FROM rifas r
LEFT JOIN boletos b ON r.id_rifa = b.id_rifa AND b.estado = 'vendido'
GROUP BY r.id_rifa;

-- Vista de boletos por usuario
CREATE VIEW vista_boletos_usuario AS
SELECT 
    u.id_usuario,
    u.nombre_completo,
    u.email,
    u.telefono,
    b.id_boleto,
    b.numero_boleto,
    b.estado,
    b.fecha_compra,
    r.nombre_rifa,
    r.fecha_sorteo,
    p.estado_pago
FROM usuarios u
INNER JOIN boletos b ON u.id_usuario = b.id_usuario
INNER JOIN rifas r ON b.id_rifa = r.id_rifa
LEFT JOIN pagos p ON b.id_boleto = p.id_boleto;

-- ============================================
-- DATOS DE EJEMPLO
-- ============================================

-- Insertar rifa de ejemplo
INSERT INTO rifas (nombre_rifa, descripcion_casa, direccion_casa, precio_boleto, total_boletos, fecha_inicio, fecha_sorteo)
VALUES (
    'Rifa Casa en Zona Residencial',
    'Casa de 3 recámaras, 2 baños, cochera para 2 autos, jardín amplio',
    'Av. Principal #123, Col. Jardines, Ciudad',
    500.00,
    1000,
    CURDATE(),
    DATE_ADD(CURDATE(), INTERVAL 90 DAY)
);

-- Crear boletos para la rifa
DELIMITER //
CREATE PROCEDURE generar_boletos_rifa(IN p_id_rifa INT, IN p_cantidad INT)
BEGIN
    DECLARE i INT DEFAULT 1;
    WHILE i <= p_cantidad DO
        INSERT INTO boletos (id_rifa, numero_boleto, codigo_verificacion)
        VALUES (p_id_rifa, i, MD5(CONCAT(p_id_rifa, '-', i, '-', NOW())));
        SET i = i + 1;
    END WHILE;
END //
DELIMITER ;

-- Generar boletos para la rifa de ejemplo
CALL generar_boletos_rifa(1, 1000);

-- Crear usuario administrador
INSERT INTO usuarios (nombre_completo, email, telefono, direccion, password_hash, rol)
VALUES (
    'Administrador Sistema',
    'admin@habistell.com',
    '5551234567',
    'Oficina Central',
    SHA2('admin123', 256),
    'administrador'
);

-- ============================================
-- TRIGGERS
-- ============================================

-- Trigger para registrar actividades en el log
DELIMITER //
CREATE TRIGGER after_usuario_insert
AFTER INSERT ON usuarios
FOR EACH ROW
BEGIN
    INSERT INTO transacciones_log (id_usuario, tipo_accion, descripcion)
    VALUES (NEW.id_usuario, 'registro', CONCAT('Nuevo usuario registrado: ', NEW.nombre_completo));
END //
DELIMITER ;

DELIMITER //
CREATE TRIGGER after_pago_update
AFTER UPDATE ON pagos
FOR EACH ROW
BEGIN
    IF NEW.estado_pago = 'confirmado' AND OLD.estado_pago != 'confirmado' THEN
        INSERT INTO transacciones_log (id_usuario, tipo_accion, descripcion)
        VALUES (NEW.id_usuario, 'pago', CONCAT('Pago confirmado por $', NEW.monto));
    END IF;
END //
DELIMITER ;

-- ============================================
-- QUERIES ÚTILES PARA REPORTES
-- ============================================

-- Consulta: Boletos disponibles por rifa
-- SELECT r.nombre_rifa, COUNT(*) as disponibles 
-- FROM rifas r 
-- INNER JOIN boletos b ON r.id_rifa = b.id_rifa 
-- WHERE b.estado = 'disponible' 
-- GROUP BY r.id_rifa;

-- Consulta: Top compradores
-- SELECT u.nombre_completo, u.email, COUNT(b.id_boleto) as total_boletos, SUM(b.monto_pagado) as total_gastado
-- FROM usuarios u
-- INNER JOIN boletos b ON u.id_usuario = b.id_usuario
-- WHERE b.estado = 'vendido'
-- GROUP BY u.id_usuario
-- ORDER BY total_boletos DESC
-- LIMIT 10;

-- Consulta: Ingresos diarios
-- SELECT DATE(fecha_pago) as fecha, COUNT(*) as pagos, SUM(monto) as total
-- FROM pagos
-- WHERE estado_pago = 'confirmado'
-- GROUP BY DATE(fecha_pago)
-- ORDER BY fecha DESC;