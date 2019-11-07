create table item_tipo (id bigint primary key auto_increment, tipo varchar(255) not null);
insert into item_tipo (tipo) values ("COMIDA");
insert into item_tipo (tipo) values ("BEBIDA");
insert into item_tipo (tipo) values ("OUTRO");

alter table item modify valor decimal(19,2) not null;
alter table item change nome descricao varchar(255) null;
alter table item add column item_tipo_id bigint not null;
alter table item add foreign key (item_tipo_id) references item_tipo(id);

alter table sala add column nome varchar(255) null;

alter table perfil modify id_usuario_fk bigint(20) not null unique;
