-- Note: this script will remove all information not only about all phases,
--       but about all resources as well.  You'll need to repopulate
--       the databse with that information using appropritate scripts

DELETE FROM notification;
DELETE FROM resource_info;
DELETE FROM resource;

DELETE FROM phase_criteria;
DELETE FROM phase_dependency;
DELETE FROM project_phase;
