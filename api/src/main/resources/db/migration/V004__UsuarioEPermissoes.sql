ALTER TABLE usuario
ADD senha VARCHAR(150) NOT NULL;

CREATE TABLE permissao (
	id BIGINT(20) PRIMARY KEY,
	descricao VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE usuario_permissao (
	usuario_id BIGINT(20) NOT NULL,
	permissao_id BIGINT(20) NOT NULL,
	PRIMARY KEY (usuario_id, permissao_id),
	FOREIGN KEY (usuario_id) REFERENCES usuario(id),
	FOREIGN KEY (permissao_id) REFERENCES permissao(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO usuario (id, celular, senha) values (1, '43999032081', '$2a$10$COodp//W7Y0.qs5skWuvjeVl3H.JlYQMWyQx8iJmZKHzq.WUgi5vO');

INSERT INTO permissao (id, descricao) values (1, 'ROLE_CADASTRAR_USUARIO');
INSERT INTO permissao (id, descricao) values (2, 'ROLE_PESQUISAR_USUARIO');
INSERT INTO permissao (id, descricao) values (3, 'ROLE_LISTAR_USUARIO');
INSERT INTO permissao (id, descricao) values (4, 'ROLE_EDITAR_USUARIO');
INSERT INTO permissao (id, descricao) values (5, 'ROLE_DESATIVAR_USUARIO');

-- admin
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (1, 1);
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (1, 2);
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (1, 3);
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (1, 4);
INSERT INTO usuario_permissao (usuario_id, permissao_id) values (1, 5);
