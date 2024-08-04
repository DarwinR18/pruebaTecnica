drop DATABASE dro_prueba_tecnica;

CREATE DATABASE IF NOT EXISTS dro_prueba_tecnica;

USE dro_prueba_tecnica;

CREATE TABLE cliente (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    genero VARCHAR(10),
    edad INT NOT NULL,
    identificacion VARCHAR(50) UNIQUE NOT NULL,
    direccion VARCHAR(255),
    telefono VARCHAR(50),
    contrasenia VARCHAR(255) NOT NULL,
    estado VARCHAR(50) NOT NULL
);

CREATE TABLE cuenta (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_cuenta VARCHAR(50) UNIQUE NOT NULL,
    tipo_cuenta VARCHAR(50) NOT NULL,
    saldo_inicial DECIMAL(15, 2) NOT NULL,
    estado VARCHAR(50) NOT NULL,
    cliente_id BIGINT NOT NULL
);

CREATE TABLE movimiento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE NOT NULL,
    tipo_movimiento VARCHAR(50) NOT NULL,
    valor DECIMAL(15, 2) NOT NULL,
    saldo DECIMAL(15, 2) NOT NULL,
    cuenta_id BIGINT NOT NULL
);
