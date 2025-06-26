use stockcontrol_bd;

drop table if exists fotos, reportes, elementos, estados, tipos_elementos, ambientes, centros, ciudades, inventarios, usuarios, generos, fichas, programas_formacion, tipos_documento, roles;

-- USUARIOS

create table roles(  
id int auto_increment primary key,  
nombre varchar(30));  

create table tipos_documento(
id int auto_increment primary key,
nombre varchar(50));

create table programas_formacion(
id int auto_increment primary key,
nombre varchar(100));

create table fichas(
id int auto_increment primary key,
ficha varchar(20),
programa_id int,
foreign key (programa_id) references programas_formacion(id));

create table generos(
id int auto_increment primary key,
nombre varchar(50));

create table usuarios(  
id int auto_increment primary key,
nombres varchar(100),
apellidos varchar(100),
tipo_documento_id int,
documento varchar(11) unique not null,  
genero_id int,
telefono varchar(15),
correo varchar(100) unique,
ficha_id int default 1, -- No Aplica
contrasena varchar(30),  
rol_id int default 2, -- Aprendices
foreign key (tipo_documento_id) references tipos_documento(id),
foreign key (genero_id) references generos(id),
foreign key (ficha_id) references fichas(id),
foreign key (rol_id) references roles(id)
);  
  
-- INVENTARIOS

create table inventarios(  
id int auto_increment primary key,  
nombre varchar(50),  
usuario_admin_id int,  
foreign key (usuario_admin_id) references usuarios(id)  
);

-- UBICACIONES

create table ciudades(  
id int auto_increment primary key,  
nombre varchar(50));  
 
create table centros(  
id int auto_increment primary key,  
nombre varchar(50),  
direccion varchar(50),  
ciudad_id int,  
foreign key (ciudad_id) references ciudades(id)
);
 
create table ambientes(  
id int auto_increment primary key,  
nombre varchar(50), 
centro_id int, 
foreign key (centro_id) references centros(id)  
);  
   
-- ELEMENTOS   

create table tipos_elementos(  
id int auto_increment primary key,  
nombre varchar(50),                 -- Ej: Portátil, Silla
descripcion varchar(250),           -- Opcional
marca varchar(50),                  -- Ej: HP, Genérica
modelo varchar(50),                 -- Ej: G5, GWC24ACEXF
observaciones text                  -- Observaciones comunes del tipo
);

  
create table estados(
id int auto_increment primary key,
nombre varchar (20));

create table elementos(  
id int auto_increment primary key,  
placa bigint unique,                 -- Código individual obligatorio
serial varchar(50),                 -- Puede ser NULL
tipo_elemento_id int,                -- FK al tipo
fecha_adquisicion date,  
valor_monetario decimal(12,2),  
estado_id int,
estado_activo boolean default true,
ambiente_id int,
inventario_id int,
foreign key (tipo_elemento_id) references tipos_elementos(id),
foreign key (estado_id) references estados(id),
foreign key (ambiente_id) references ambientes(id),
foreign key (inventario_id) references inventarios(id)
);

 
-- REPORTES 

create table reportes( 
id int auto_increment primary key, 
fecha timestamp default current_timestamp,, 
asunto varchar(100), 
mensaje text, 
usuario_id int, 
elemento_id int, 
foreign key (usuario_id) references usuarios(id), 
foreign key (elemento_id) references elementos(id)  
);

create table fotos(
id int auto_increment primary key,
url text,
reporte_id int,
foreign key (reporte_id) references reportes(id)
);