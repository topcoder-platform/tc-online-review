INSERT INTO 'informix'.id_sequences(name, next_block_start, block_size, exhausted) VALUES ('idGenerator', 100, 10, 0);

INSERT INTO 'informix'.user(user_id, handle, status) VALUES(1, 'handle1', 'A');
INSERT INTO 'informix'.user(user_id, handle, status) VALUES(2, 'handle2', 'A');
INSERT INTO 'informix'.user(user_id, handle, status) VALUES(3, 'handle3', 'A');

INSERT INTO 'informix'.terms_of_use_type(terms_of_use_type_id, terms_of_use_type_desc) VALUES(1, 'type1');
INSERT INTO 'informix'.terms_of_use_type(terms_of_use_type_id, terms_of_use_type_desc) VALUES(2, 'type2');

INSERT INTO 'informix'.terms_of_use_agreeability_type_lu (terms_of_use_agreeability_type_id, name, description) VALUES(1, 'Non-agreeable', 'Non-agreeable');
INSERT INTO 'informix'.terms_of_use_agreeability_type_lu (terms_of_use_agreeability_type_id, name, description) VALUES(2, 'Electronically-agreeable', 'Electronically-agreeable');