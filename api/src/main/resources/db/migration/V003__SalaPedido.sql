create table item (id bigint not null auto_increment, nome varchar(255), valor decimal(19,2), primary key (id))
 engine=InnoDB default charset=utf8;
 
create table pedido (id bigint not null auto_increment, quantidade int not null, item_id bigint, primary key (id),
 foreign key (item_id) references item (id)) engine=InnoDB default charset=utf8;
 
create table pedido_perfil (pedido_cliente_id bigint not null, perfil_id bigint unique not null,
 foreign key (perfil_id) references perfil(id), foreign key (pedido_cliente_id) references pedido(id))
  engine=InnoDB default charset=utf8;
  
create table sala (id bigint not null auto_increment, primary key (id)) engine=InnoDB default charset=utf8;

create table pedido_sala (sala_id bigint not null, pedido_id bigint unique not null, primary key (sala_id, pedido_id), 
foreign key (pedido_id) references pedido (id), foreign key (sala_id) references sala (id)) engine=InnoDB default charset=utf8;