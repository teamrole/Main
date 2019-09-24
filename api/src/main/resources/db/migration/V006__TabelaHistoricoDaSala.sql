CREATE TABLE historico_sala_usuario(
	id bigint primary key auto_increment,
	sala_id bigint not null,
	usuario_id bigint not null,
	data_hora_entrada TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_hora_saida TIMESTAMP,	
	foreign key (sala_id) references sala(id),
	foreign key (usuario_id) references usuario(id)
)engine=InnoDB default charset=utf8;