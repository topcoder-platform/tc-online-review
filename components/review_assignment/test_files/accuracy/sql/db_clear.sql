database tcs_catalog;

DELETE FROM resource_info WHERE resource_id >= 125200;
DELETE FROM resource WHERE resource_id >= 125200;
DELETE FROM review_application WHERE review_application_id > 29951;
UPDATE review_application SET review_application_status_id = 1 WHERE review_application_id BETWEEN 29901 AND 29951;
