CREATE TABLE perfil(
	id bigint primary key auto_increment,
    foto varchar(255),
    nome varchar(100),
    id_usuario_fk bigint,
	foreign key (id_usuario_fk) references usuario(id)
)engine=InnoDB default charset=utf8;