create user 'Yari04'@'localhost' identified by "0421";

create database stockcontrol_bd;

grant all privileges on stockcontrol_bd.* to 'Yari04'@'localhost';

flush privileges;

set global log_bin_trust_function_creators = 1;