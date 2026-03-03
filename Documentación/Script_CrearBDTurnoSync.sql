CREATE DATABASE IF NOT EXISTS turnosync;

USE turnosync;

CREATE TABLE usuarios (
  id_usuario INT AUTO_INCREMENT PRIMARY KEY,

  username VARCHAR(150) NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  salt VARCHAR(255) NOT NULL,

  rol VARCHAR(20) NOT NULL,
  estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',

  id_cliente INT NULL,
  id_empleado INT NULL,

  -- CAMPOS DE AUDITORÍA
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_by VARCHAR(100) NULL,

  updated_at DATETIME NULL DEFAULT NULL
    ON UPDATE CURRENT_TIMESTAMP,
  updated_by VARCHAR(100) NULL,

  deleted_at DATETIME NULL,
  deleted_by VARCHAR(100) NULL,
  is_deleted TINYINT(1) NOT NULL DEFAULT 0,

  -- RESTRICCIONES
  CONSTRAINT uq_usuarios_username UNIQUE (username),

  CONSTRAINT ck_usuarios_rol
    CHECK (rol IN ('ADMIN','CLIENTE','EMPLEADO')),

  CONSTRAINT ck_usuarios_estado
    CHECK (estado IN ('ACTIVO','INACTIVO','BLOQUEADO'))
);

INSERT INTO usuarios
(username, password_hash, salt, rol, estado, created_by)
VALUES
('admin', 'HASH_EJEMPLO', 'SALT_EJEMPLO', 'ADMIN', 'ACTIVO', 'SYSTEM');


CREATE TABLE clientes (
  id_cliente INT AUTO_INCREMENT PRIMARY KEY,

  nombre VARCHAR(120) NOT NULL,
  telefono VARCHAR(30) NULL,
  email VARCHAR(150) NOT NULL,

  -- CAMPOS DE AUDITORÍA
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_by VARCHAR(100) NULL,

  updated_at DATETIME NULL DEFAULT NULL
    ON UPDATE CURRENT_TIMESTAMP,
  updated_by VARCHAR(100) NULL,

  deleted_at DATETIME NULL,
  deleted_by VARCHAR(100) NULL,
  is_deleted TINYINT(1) NOT NULL DEFAULT 0,

  -- RESTRICCIONES
  CONSTRAINT uq_clientes_email UNIQUE (email)
);

CREATE TABLE empleados (
  id_empleado INT AUTO_INCREMENT PRIMARY KEY,

  nombre VARCHAR(120) NOT NULL,
  especialidad VARCHAR(120) NULL,

  -- CAMPOS DE AUDITORÍA
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_by VARCHAR(100) NULL,

  updated_at DATETIME NULL DEFAULT NULL
    ON UPDATE CURRENT_TIMESTAMP,
  updated_by VARCHAR(100) NULL,

  deleted_at DATETIME NULL,
  deleted_by VARCHAR(100) NULL,
  is_deleted TINYINT(1) NOT NULL DEFAULT 0
);

ALTER TABLE usuarios
ADD CONSTRAINT fk_usuario_cliente
FOREIGN KEY (id_cliente)
REFERENCES clientes(id_cliente)
ON UPDATE CASCADE
ON DELETE SET NULL;

ALTER TABLE usuarios
ADD CONSTRAINT fk_usuario_empleado
FOREIGN KEY (id_empleado)
REFERENCES empleados(id_empleado)
ON UPDATE CASCADE
ON DELETE SET NULL;

CREATE TABLE turnos (
  id_turno INT AUTO_INCREMENT PRIMARY KEY,

  fecha DATE NOT NULL,
  hora TIME NOT NULL,
  estado VARCHAR(20) NOT NULL DEFAULT 'DISPONIBLE',

  id_cliente INT NULL,
  id_empleado INT NULL,

  -- CAMPOS DE AUDITORÍA
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_by VARCHAR(100) NULL,

  updated_at DATETIME NULL DEFAULT NULL
    ON UPDATE CURRENT_TIMESTAMP,
  updated_by VARCHAR(100) NULL,

  deleted_at DATETIME NULL,
  deleted_by VARCHAR(100) NULL,
  is_deleted TINYINT(1) NOT NULL DEFAULT 0,

  -- FOREIGN KEYS
  CONSTRAINT fk_turno_cliente
    FOREIGN KEY (id_cliente)
    REFERENCES clientes(id_cliente)
    ON UPDATE CASCADE
    ON DELETE SET NULL,

  CONSTRAINT fk_turno_empleado
    FOREIGN KEY (id_empleado)
    REFERENCES empleados(id_empleado)
    ON UPDATE CASCADE
    ON DELETE SET NULL,

  -- RESTRICCIONES
  CONSTRAINT ck_turno_estado
    CHECK (estado IN (
      'DISPONIBLE',
      'RESERVADO',
      'CANCELADO',
      'ATENDIDO'
    )),

  -- Evita duplicar el mismo horario para un empleado
  CONSTRAINT uq_turno_slot UNIQUE (fecha, hora, id_empleado)
);

CREATE TABLE peticiones (
  id_peticion INT AUTO_INCREMENT PRIMARY KEY,

  tipo VARCHAR(60) NOT NULL,
  estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',

  creada_en DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  procesada_en DATETIME NULL,
  finalizada_en DATETIME NULL,

  id_usuario INT NULL,
  id_turno INT NULL,

  detalle VARCHAR(500) NULL,

  -- CAMPOS DE AUDITORÍA
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_by VARCHAR(100) NULL,

  updated_at DATETIME NULL DEFAULT NULL
    ON UPDATE CURRENT_TIMESTAMP,
  updated_by VARCHAR(100) NULL,

  deleted_at DATETIME NULL,
  deleted_by VARCHAR(100) NULL,
  is_deleted TINYINT(1) NOT NULL DEFAULT 0,

  -- FOREIGN KEYS
  CONSTRAINT fk_peticion_usuario
    FOREIGN KEY (id_usuario)
    REFERENCES usuarios(id_usuario)
    ON UPDATE CASCADE
    ON DELETE SET NULL,

  CONSTRAINT fk_peticion_turno
    FOREIGN KEY (id_turno)
    REFERENCES turnos(id_turno)
    ON UPDATE CASCADE
    ON DELETE SET NULL,

  -- RESTRICCIONES
  CONSTRAINT ck_peticion_estado
    CHECK (estado IN (
      'PENDIENTE',
      'EN_PROCESO',
      'COMPLETADA',
      'FALLIDA'
    ))
);

CREATE INDEX idx_peticiones_estado ON peticiones(estado);
CREATE INDEX idx_peticiones_tipo ON peticiones(tipo);
CREATE INDEX idx_peticiones_usuario ON peticiones(id_usuario);
CREATE INDEX idx_peticiones_turno ON peticiones(id_turno);
CREATE INDEX idx_peticiones_creada_en ON peticiones(creada_en);