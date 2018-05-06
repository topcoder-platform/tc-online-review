INSERT INTO 'informix'.id_sequences (name, next_block_start, block_size, exhausted) VALUES ('idGenerator', 1, 10000, 0);

INSERT INTO 'informix'.user (user_id, handle, status) VALUES(1, 'tc1', '');
INSERT INTO 'informix'.user (user_id, handle, status) VALUES(2, 'tc2', '');
INSERT INTO 'informix'.user (user_id, handle, status) VALUES(3, 'tc3', '');
INSERT INTO 'informix'.user (user_id, handle, status) VALUES(4, 'tc4', '');
INSERT INTO 'informix'.user (user_id, handle, status) VALUES(5, 'tc5', '');
INSERT INTO 'informix'.user (user_id, handle, status) VALUES(6, 'tc6', '');
INSERT INTO 'informix'.user (user_id, handle, status) VALUES(7, 'tc7', '');
INSERT INTO 'informix'.user (user_id, handle, status) VALUES(8, 'tc8', '');
INSERT INTO 'informix'.user (user_id, handle, status) VALUES(9, 'tc9', '');
INSERT INTO 'informix'.user (user_id, handle, status) VALUES(10, 'tc10', '');

INSERT INTO 'informix'.terms_of_use_type (terms_of_use_type_id, terms_of_use_type_desc) VALUES(1, 'type1');
INSERT INTO 'informix'.terms_of_use_type (terms_of_use_type_id, terms_of_use_type_desc) VALUES(2, 'type2');
INSERT INTO 'informix'.terms_of_use_type (terms_of_use_type_id, terms_of_use_type_desc) VALUES(3, 'type3');
INSERT INTO 'informix'.terms_of_use_type (terms_of_use_type_id, terms_of_use_type_desc) VALUES(4, 'type4');
INSERT INTO 'informix'.terms_of_use_type (terms_of_use_type_id, terms_of_use_type_desc) VALUES(5, 'type5');



INSERT INTO 'informix'.terms_of_use_agreeability_type_lu (terms_of_use_agreeability_type_id, name, description) VALUES(0, 'Non-agreeable', 'Non-agreeable');
INSERT INTO 'informix'.terms_of_use_agreeability_type_lu (terms_of_use_agreeability_type_id, name, description) VALUES(1, 'Non-electronically-agreeable', 'Non-electronically-agreeable');

