create table usuario(
	id bigint primary key auto_increment,
    celular varchar(11) not null,
    ativo boolean default true
)engine=InnoDB default charset=utf8;