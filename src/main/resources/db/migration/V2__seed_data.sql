-- V2 — Datos iniciales (seed) de Diamond BarberHub
-- Datos de catálogo que el sistema necesita para funcionar.
-- INSERT IGNORE: no falla si los datos ya existen en la BD.

INSERT IGNORE INTO roles (name) VALUES
    ('USER'),
    ('ADMIN');

INSERT IGNORE INTO tipo_horarios (nombre) VALUES
    ('Mañana'),
    ('Tarde'),
    ('Noche');

INSERT IGNORE INTO tipo_servicios (nombre) VALUES
    ('Cortes'),
    ('Skincare'),
    ('Afeitado de Barba'),
    ('Coloración');

INSERT IGNORE INTO horario_rangos (rango, tipo_horario_id) VALUES
    ('08:00 - 09:00', 1),
    ('09:00 - 10:00', 1),
    ('10:00 - 11:00', 1),
    ('11:00 - 12:00', 1),
    ('12:00 - 13:00', 1);

INSERT IGNORE INTO horario_rangos (rango, tipo_horario_id) VALUES
    ('13:00 - 14:00', 1),
    ('14:00 - 15:00', 2),
    ('15:00 - 16:00', 2),
    ('16:00 - 17:00', 2),
    ('17:00 - 18:00', 2);

INSERT IGNORE INTO horario_rangos (rango, tipo_horario_id) VALUES
    ('18:00 - 19:00', 3),
    ('19:00 - 20:00', 3),
    ('20:00 - 21:00', 3),
    ('21:00 - 22:00', 3);