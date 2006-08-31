-- Note: this script will remove all information not only about all projects,
--       but about all phases and resources as well.  You'll need to repopulate
--       the databse with that information using appropritate scripts

DELETE FROM notification;
DELETE FROM resource_info;
DELETE FROM resource;

DELETE FROM phase_criteria;
DELETE FROM phase_dependency;
DELETE FROM project_phase;

DELETE FROM project_info;
DELETE FROM project;
