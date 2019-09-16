ALTER TABLE pedido_perfil CHANGE `pedido_cliente_id` `pedido_id` bigint not null;

ALTER TABLE pedido_perfil DROP FOREIGN KEY pedido_perfil_ibfk_1;

ALTER TABLE pedido_perfil DROP INDEX perfil_id;
  
 alter table pedido_perfil add column id bigint primary key auto_increment;

