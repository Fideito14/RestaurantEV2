-- =============================================================
-- V1__crear_tabla_usuarios.sql
--
-- Tabla base para Auth + Gestión de Usuarios
-- Roles esperados en la app:
--   CLIENTE | EMPLEADO | ADMIN
-- =============================================================

CREATE TABLE usuarios (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    email         VARCHAR(190) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    rol           VARCHAR(20)  NOT NULL,
    activo        TINYINT(1)   NOT NULL DEFAULT 1,
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT uq_usuarios_email UNIQUE (email)
);

