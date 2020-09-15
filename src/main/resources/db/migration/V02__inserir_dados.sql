insert into permissao (id, nome) values 
	(1, 'ROLE_ADMIN'), (2, 'ROLE_USER');
	
insert into usuario (id,codigo, nome,sobrenome, username, password, enabled, data_cadastro) values 
	(1,'001', 'ACQUA','Default', 'acqua', '$2a$10$6seaJ.I/0iVkGaA5rl1FmuQCRRKx/UuFLEOsxVPMeKv2q3gXapufm', true, '2020-08-24')
;
	
insert into usuario_permissoes (usuario_id, permissao_id) values (1, 1), (1, 2);

