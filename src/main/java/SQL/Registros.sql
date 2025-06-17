use stockcontrol_bd;

-- REGISTROS

insert into roles (nombre) values 
('Administrador'), 
('Aprendiz');

insert into tipos_documento (nombre) values 
('Cédula de Ciudadanía'),
('Tarjeta de Identidad'),
('Cédula de Extranjería'),
('Pasaporte'),
('Permiso Especial de Permanencia'),
('Permiso por Protección Temporal');

insert into programas_formacion (nombre) value
('(ADSO) - Análisis y Desarrollo de Software');

insert into fichas (ficha, programa_id) value 
('2894667', 1);

insert into generos (nombre) values
('Masculino'),
('Femenino'),
('Otro');

insert into usuarios (nombres, apellidos, tipo_documento_id, documento, genero_id, telefono, correo, contrasena, rol_id) value
("Enzy Zulay", "Angarita Bermudez", 1, "1234567890", 2, "3123456789", "ejemplo@ejemplo.com", "#Admin12345", 1);
