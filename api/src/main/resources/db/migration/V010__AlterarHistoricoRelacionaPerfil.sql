alter table historico_sala_usuario DROP FOREIGN KEY historico_sala_usuario_ibfk_2;

alter table historico_sala_usuario drop usuario_id;

alter table historico_sala_usuario add column perfil_id bigint;

alter table historico_sala_usuario add foreign key (perfil_id) references perfil(id);