ALTER TABLE item DROP FOREIGN KEY item_ibfk_1;

alter table item drop column item_tipo_id;

drop table item_tipo;

alter table item add column tipo varchar(20) not null;