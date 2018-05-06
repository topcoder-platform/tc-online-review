database tcs_catalog;

DELETE FROM resource_info WHERE resource_id >= 125200;
DELETE FROM resource WHERE resource_id >= 125200;
DELETE FROM review_application WHERE review_application_id > 29951;
UPDATE review_application SET review_application_status_id = 1 WHERE review_application_id BETWEEN 29901 AND 29951;
UPDATE project_phase SET scheduled_end_time = scheduled_start_time + 172800 UNITS SECOND, duration = 172800000 WHERE phase_type_id = 4 AND phase_status_id = 2;
