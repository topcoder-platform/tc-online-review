INSERT INTO 'informix'.user_terms_of_use_xref (user_id, terms_of_use_id) VALUES(1, 1);
INSERT INTO 'informix'.user_terms_of_use_xref (user_id, terms_of_use_id) VALUES(1, 2);
INSERT INTO 'informix'.user_terms_of_use_xref (user_id, terms_of_use_id) VALUES(2, 1);
INSERT INTO 'informix'.user_terms_of_use_xref (user_id, terms_of_use_id) VALUES(3, 3);

INSERT INTO 'informix'.user_terms_of_use_ban_xref (user_id, terms_of_use_id) VALUES(1, 3);
INSERT INTO 'informix'.user_terms_of_use_ban_xref (user_id, terms_of_use_id) VALUES(2, 4);
INSERT INTO 'informix'.user_terms_of_use_ban_xref (user_id, terms_of_use_id) VALUES(3, 4);

INSERT INTO 'informix'.project_role_terms_of_use_xref(project_id, resource_role_id, terms_of_use_id, sort_order, group_ind) VALUES(1, 1, 3, 3, 0);
INSERT INTO 'informix'.project_role_terms_of_use_xref(project_id, resource_role_id, terms_of_use_id, sort_order, group_ind) VALUES(1, 1, 2, 2, 0);
INSERT INTO 'informix'.project_role_terms_of_use_xref(project_id, resource_role_id, terms_of_use_id, sort_order, group_ind) VALUES(1, 1, 1, 1, 0);
INSERT INTO 'informix'.project_role_terms_of_use_xref(project_id, resource_role_id, terms_of_use_id, sort_order, group_ind) VALUES(1, 2, 1, 1, 1);
INSERT INTO 'informix'.project_role_terms_of_use_xref(project_id, resource_role_id, terms_of_use_id, sort_order, group_ind) VALUES(1, 3, 2, 1, 2);
INSERT INTO 'informix'.project_role_terms_of_use_xref(project_id, resource_role_id, terms_of_use_id, sort_order, group_ind) VALUES(2, 1, 2, 1, 2);
