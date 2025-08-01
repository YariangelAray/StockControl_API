use stockcontrol_bd;

-- REGISTROS

insert into roles (nombre, descripcion) values 
('Administrativo', 'Gestiona los inventarios a su cargo.'), 
('Corriente', 'Apoya en la gestión de los inventarios con acceso. Puede modificar cierta información de los elementos.');

insert into tipos_documento (nombre) values 
('Cédula de Ciudadanía'),
('Tarjeta de Identidad'),
('Cédula de Extranjería'),
('Pasaporte'),
('Permiso Especial de Permanencia'),
('Permiso por Protección Temporal');

insert into programas_formacion (nombre) values
('(N/A) - No Aplica'),
('(ADSO) - Análisis y Desarrollo de Software');

insert into fichas (ficha, programa_id) values
('(N/A) - No Aplica', 1),
('2894667', 2);

insert into generos (nombre) values
('Masculino'),
('Femenino'),
('Otro');

insert into usuarios (nombres, apellidos, tipo_documento_id, documento, genero_id, telefono, correo, contrasena, rol_id) value
('Enzy Zulay', 'Angarita Bermudez', 1, '1234567890', 2, '3123456789', 'ejemplo@ejemplo.com', '$2a$10$dkawaQIOhlZjU/FD2D2eN.nMhFEnZH.TxAeGuUGyYS/DBKPsRoJB6', 1); -- #Admin12345

insert into centros (nombre, direccion) value 
('(CIMI) - Centro Industrial de Mantenimiento Integral', 'Km. 7 Vía Rincón de Girón');

insert into ambientes (nombre, centro_id) values 
('Fabrica de Software', 1), 
('Dessarrollo de Software', 1);

insert into estados(nombre) values 
('Bueno'), 
('Regular'), 
('Malo');

insert into inventarios (nombre, fecha_creacion, ultima_actualizacion, usuario_admin_id) values 
('Desarrollo de Software', '2008-12-22', '2024-12-19', 1);

insert into tipos_elementos(nombre, descripcion, marca, modelo, detalles) value
('Computador Portatil', 'TIPO ELEMENTO DEVOLUTIVO UNIDAD DE MEDIDA UNIDAD CARACTERISTICA CON PUERTAS DE CERRADO FIJO DIMENSION N.A. MATERIAL N.A.', 'HP', 'RTL 8822BE', 'Portátil Ultraliviano 3 12 hp Pb445 rg6 R7-3700u con tarjeta micro Sd pad Mause Guaya Mause y morral.');

insert into elementos(placa, serial, tipo_elemento_id, fecha_adquisicion, valor_monetario, estado_id, ambiente_id, inventario_id) value
(9224102317, '3H82240000650', 1, '2024-12-13', 4732383.61, 1, 2, 1);

INSERT INTO reportes (asunto, mensaje, usuario_id, elemento_id) VALUES
('Este es el asunto del reporte 1', 'Mensaje de prueba para el reporte número 1.', 1, 1);
