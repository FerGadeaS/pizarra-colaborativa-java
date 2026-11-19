
-- =====================================================
-- BASE DE DATOS: PIZARRA
-- Proyecto: Pizarra colaborativa Java
-- =====================================================

CREATE DATABASE IF NOT EXISTS pizarra;
USE pizarra;

-- Eliminar objetos anteriores para permitir la recreación
DROP PROCEDURE IF EXISTS SP_InicioSesion;
DROP PROCEDURE IF EXISTS SP_CrearCuenta;
DROP PROCEDURE IF EXISTS SP_GuardarDatos;

DROP TABLE IF EXISTS conexion;
DROP TABLE IF EXISTS usuario;
DROP TABLE IF EXISTS Datos;

-- =====================================================
-- TABLA: usuario
-- =====================================================

CREATE TABLE usuario (
    id_cuenta INT AUTO_INCREMENT NOT NULL,
    matricula CHAR(10) NOT NULL,
    contrasenna CHAR(20) NOT NULL,
    conectado INT DEFAULT 0,
    PRIMARY KEY (id_cuenta)
);

-- =====================================================
-- TABLA: conexion
-- =====================================================

CREATE TABLE conexion (
    id_conexion INT AUTO_INCREMENT NOT NULL,
    id_user INT NOT NULL,
    fecha_ingreso CHAR(20) NOT NULL,
    hora_entrada CHAR(20) NOT NULL,
    duracion_en_pizarra CHAR(20) NOT NULL,
    fecha_salida CHAR(20) NOT NULL,
    hora_salida CHAR(20) NOT NULL,
    PRIMARY KEY (id_conexion),
    CONSTRAINT FK_id_user
        FOREIGN KEY (id_user) REFERENCES usuario(id_cuenta)
);

-- =====================================================
-- TABLA: Datos
-- =====================================================

CREATE TABLE Datos (
    id INT,
    fecha CHAR(20),
    hora CHAR(20)
);

-- =====================================================
-- PROCEDIMIENTOS ALMACENADOS
-- =====================================================

DELIMITER //

-- Inicio de sesión
CREATE PROCEDURE SP_InicioSesion(
    IN p_matricula CHAR(10),
    IN p_password CHAR(20)
)
BEGIN
    IF EXISTS (
        SELECT 1 FROM usuario
        WHERE matricula = p_matricula
        AND contrasenna = p_password
    ) THEN
        SELECT id_cuenta FROM usuario
        WHERE matricula = p_matricula
        AND contrasenna = p_password;
    ELSE
        SELECT '0';
    END IF;
END //

-- Crear cuenta
CREATE PROCEDURE SP_CrearCuenta(
    IN p_matricula CHAR(10),
    IN p_password CHAR(20)
)
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM usuario
        WHERE matricula = p_matricula
    ) THEN
        INSERT INTO usuario (matricula, contrasenna)
        VALUES (p_matricula, p_password);

        SELECT '1' AS resultado;
    ELSE
        SELECT '0' AS resultado;
    END IF;
END //

-- Guardar historial de conexión
CREATE PROCEDURE SP_GuardarDatos(
    IN p_id_user INT,
    IN p_fecha_ingreso CHAR(20),
    IN p_horain CHAR(20),
    IN p_duracion CHAR(20),
    IN p_fechaout CHAR(20),
    IN p_horaout CHAR(20)
)
BEGIN
    INSERT INTO conexion (
        id_user,
        fecha_ingreso,
        hora_entrada,
        duracion_en_pizarra,
        fecha_salida,
        hora_salida
    )
    VALUES (
        p_id_user,
        p_fecha_ingreso,
        p_horain,
        p_duracion,
        p_fechaout,
        p_horaout
    );
END //

DELIMITER ;
