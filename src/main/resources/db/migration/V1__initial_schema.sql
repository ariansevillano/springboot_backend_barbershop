-- ============================================================
-- V1 — Esquema inicial de Diamond BarberHub
-- ============================================================
-- Este script representa el estado de la BD antes de Flyway.
-- Con baseline-on-migrate=true, este script NO se ejecuta en
-- BDs existentes — solo sirve como documentación del esquema.
-- En BDs nuevas (otros entornos), sí se ejecuta para crear todo.
-- ============================================================

-- ----------------------------------------------------------
-- 1. roles
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS roles (
    rol_id  BIGINT       NOT NULL AUTO_INCREMENT,
    name    VARCHAR(255) NOT NULL,
    PRIMARY KEY (rol_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ----------------------------------------------------------
-- 2. tipo_horarios
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS tipo_horarios (
    tipo_horario_id BIGINT       NOT NULL AUTO_INCREMENT,
    nombre         VARCHAR(255) NOT NULL,
    PRIMARY KEY (tipo_horario_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ----------------------------------------------------------
-- 3. tipo_servicios
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS tipo_servicios (
    tipo_servicio_id BIGINT       NOT NULL AUTO_INCREMENT,
    nombre          VARCHAR(255) NOT NULL,
    PRIMARY KEY (tipo_servicio_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ----------------------------------------------------------
-- 4. barberos
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS barberos (
    barbero_id  BIGINT       NOT NULL AUTO_INCREMENT,
    nombre      VARCHAR(255) NOT NULL,
    estado      INT          NOT NULL,
    url_barbero  VARCHAR(255),
    PRIMARY KEY (barbero_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ----------------------------------------------------------
-- 5. horario_rangos  (depende de: tipo_horarios)
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS horario_rangos (
    horario_rango_id BIGINT       NOT NULL AUTO_INCREMENT,
    rango           VARCHAR(255) NOT NULL,
    tipo_horario_id  BIGINT       NOT NULL,
    PRIMARY KEY (horario_rango_id),
    CONSTRAINT fk_hr_tipo_horario
        FOREIGN KEY (tipo_horario_id) REFERENCES tipo_horarios (tipo_horario_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ----------------------------------------------------------
-- 6. horario_barbero_base  (depende de: barberos, tipo_horarios)
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS horario_barbero_base (
    horario_barbero_base_id BIGINT      NOT NULL AUTO_INCREMENT,
    barbero_id              BIGINT      NOT NULL,
    tipo_horario_id          BIGINT      NOT NULL,
    dia                     VARCHAR(20) NOT NULL,
    est_id                  INT,
    estado                  INT,
    PRIMARY KEY (horario_barbero_base_id),
    UNIQUE KEY uk_hbb_barbero_tipo_dia (barbero_id, tipo_horario_id, dia),
    CONSTRAINT fk_hbb_barbero
        FOREIGN KEY (barbero_id)     REFERENCES barberos     (barbero_id),
    CONSTRAINT fk_hbb_tipo_horario
        FOREIGN KEY (tipo_horario_id) REFERENCES tipo_horarios (tipo_horario_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ----------------------------------------------------------
-- 7. horario_barbero_instancias  (depende de: barberos, tipo_horarios)
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS horario_barbero_instancias (
    horario_barbero_instancia_id BIGINT      NOT NULL AUTO_INCREMENT,
    barbero_id                 BIGINT      NOT NULL,
    tipo_horario_id             BIGINT      NOT NULL,
    dia                        VARCHAR(20) NOT NULL,
    fecha                      DATE        NOT NULL,
    est_id                     INT,
    PRIMARY KEY (horario_barbero_instancia_id),
    CONSTRAINT fk_hbi_barbero
        FOREIGN KEY (barbero_id)     REFERENCES barberos      (barbero_id),
    CONSTRAINT fk_hbi_tipo_horario
        FOREIGN KEY (tipo_horario_id) REFERENCES tipo_horarios (tipo_horario_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ----------------------------------------------------------
-- 8. usuarios
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS usuarios (
    usuario_id              BIGINT       NOT NULL AUTO_INCREMENT,
    username                VARCHAR(255) NOT NULL,
    password                VARCHAR(255) NOT NULL,
    nombre                  VARCHAR(255) NOT NULL,
    apellido                VARCHAR(255) NOT NULL,
    email                   VARCHAR(255) NOT NULL,
    celular                 VARCHAR(255),
    url_usuario              VARCHAR(255),
    token_password           VARCHAR(255),
    last_token_request      DATETIME(6),
    refresh_token            VARCHAR(255),
    refresh_token_expiry_date  DATETIME(6),
    PRIMARY KEY (usuario_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ----------------------------------------------------------
-- 9. usuario_roles  (tabla de unión ManyToMany — depende de: usuarios, roles)
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS usuario_roles (
    usuario_id BIGINT NOT NULL,
    rol_id     BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, rol_id),
    CONSTRAINT fk_ur_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuarios (usuario_id),
    CONSTRAINT fk_ur_rol
        FOREIGN KEY (rol_id)     REFERENCES roles    (rol_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ----------------------------------------------------------
-- 10. servicios  (depende de: tipo_servicios)
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS servicios (
    servicio_id     BIGINT       NOT NULL AUTO_INCREMENT,
    nombre          VARCHAR(255) NOT NULL,
    precio          BIGINT       NOT NULL,
    descripcion     VARCHAR(255) NOT NULL,
    tipo_servicio_id BIGINT       NOT NULL,
    url_servicio     VARCHAR(255),
    estado          INT          NOT NULL,
    PRIMARY KEY (servicio_id),
    CONSTRAINT fk_servicio_tipo
        FOREIGN KEY (tipo_servicio_id) REFERENCES tipo_servicios (tipo_servicio_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ----------------------------------------------------------
-- 11. reservas  (depende de: barberos, usuarios, horario_rangos, servicios)
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS reservas (
    reserva_id         BIGINT       NOT NULL AUTO_INCREMENT,
    barbero_id         BIGINT       NOT NULL,
    usuario_id         BIGINT       NOT NULL,
    horario_rango_id    BIGINT       NOT NULL,
    servicio_id        BIGINT       NOT NULL,
    estado             VARCHAR(50)  NOT NULL,
    precio_servicio     BIGINT       NOT NULL,
    motivo_descripcion  VARCHAR(255),
    adicionales        VARCHAR(255),
    fecha_creacion      DATETIME(6)  NOT NULL,
    fecha_reserva       DATE         NOT NULL,
    est_recompensa      INT,
    url_pago            VARCHAR(255),
    PRIMARY KEY (reserva_id),
    CONSTRAINT fk_reserva_barbero
        FOREIGN KEY (barbero_id)      REFERENCES barberos      (barbero_id),
    CONSTRAINT fk_reserva_usuario
        FOREIGN KEY (usuario_id)      REFERENCES usuarios      (usuario_id),
    CONSTRAINT fk_reserva_horario
        FOREIGN KEY (horario_rango_id) REFERENCES horario_rangos (horario_rango_id),
    CONSTRAINT fk_reserva_servicio
        FOREIGN KEY (servicio_id)     REFERENCES servicios     (servicio_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ----------------------------------------------------------
-- 12. valoraciones  (depende de: usuarios)
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS valoraciones (
    valoracion_id BIGINT       NOT NULL AUTO_INCREMENT,
    valoracion    INT,
    util          TINYINT(1),
    mensaje       VARCHAR(255),
    usuario_id    BIGINT       NOT NULL,
    estado        INT,
    PRIMARY KEY (valoracion_id),
    CONSTRAINT fk_valoracion_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuarios (usuario_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;
