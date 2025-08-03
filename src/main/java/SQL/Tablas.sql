use stockcontrol_bd;

drop table if exists fotos, reportes, elementos, estados, tipos_elementos, ambientes, centros, ciudades, inventarios, codigos_acceso, usuarios, accesos_temporales, generos, fichas, programas_formacion, tipos_documento, roles;

-- USUARIOS

create table roles(  
id int auto_increment primary key,  
nombre varchar(30),
descripcion text);  

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
correo varchar(100) unique not null,
ficha_id int default 1, -- No Aplica
contrasena varchar(100),  
rol_id int default 3, -- Aprendices
activo boolean default true,
foreign key (tipo_documento_id) references tipos_documento(id),
foreign key (genero_id) references generos(id),
foreign key (ficha_id) references fichas(id),
foreign key (rol_id) references roles(id)
);  
  
-- INVENTARIOS

create table inventarios(  
id int auto_increment primary key,  
nombre varchar(50),
fecha_creacion date,
ultima_actualizacion date,
usuario_admin_id int,  
foreign key (usuario_admin_id) references usuarios(id)  
);

DELIMITER //
create trigger establecer_ultima_actualizacion
before insert on inventarios
for each row
begin
  if new.ultima_actualizacion is null then
    set new.ultima_actualizacion = new.fecha_creacion;
  end if;
end;
//
DELIMITER ;


-- UBICACIONES
create table centros(  
id int auto_increment primary key,  
nombre varchar(100),  
direccion varchar(50)
);
 
create table ambientes(  
id int auto_increment primary key,  
nombre varchar(50), 
centro_id int, 
foreign key (centro_id) references centros(id)  
);  
   
-- ELEMENTOS   

create table estados(
id int auto_increment primary key,
nombre varchar (20));

create table tipos_elementos(  
id int auto_increment primary key,  
nombre varchar(50),  
consecutivo int unique not null,              
descripcion varchar(250),           
marca varchar(50),                 
modelo varchar(50),              
atributos text                  
);

create table elementos(  
id int auto_increment primary key,  
placa bigint unique,                 -- Código individual obligatorio
serial varchar(50),                 -- Puede ser NULL
tipo_elemento_id int,                -- FK al tipo
fecha_adquisicion date,  
valor_monetario decimal(12,2),  
estado_id int,
observaciones text,
estado_activo boolean default true,
ambiente_id int null,
inventario_id int,
foreign key (tipo_elemento_id) references tipos_elementos(id),
foreign key (estado_id) references estados(id),
foreign key (ambiente_id) references ambientes(id),
foreign key (inventario_id) references inventarios(id)
);

DELIMITER //
create trigger actualizar_inventario_elemento
after insert on elementos
for each row
begin
    update inventarios
    set ultima_actualizacion = now()
    where id = new.inventario_id;
end;
//
create trigger actualizar_inventario_modificacion
after update on elementos
for each row
begin
    update inventarios
    set ultima_actualizacion = now()
    where id = new.inventario_id;
end;
//

DELIMITER ;
 
-- REPORTES 

create table reportes( 
id int auto_increment primary key, 
fecha date default (curdate()),
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

-- GESTIÓN DE ACCESO A UN INVENTARIO

create table codigos_acceso (
  id int primary key auto_increment,
  codigo varchar(10) not null unique,
  inventario_id int not null,
  fecha_creacion datetime default current_timestamp,
  fecha_expiracion datetime not null,
  foreign key (inventario_id) references inventarios(id)
);

create table accesos_temporales (
  id int primary key auto_increment,
  usuario_id int not null,
  inventario_id int not null,  
  foreign key (usuario_id) references usuarios(id),
  foreign key (inventario_id) references inventarios(id)
);