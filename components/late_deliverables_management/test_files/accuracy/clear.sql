# Database 'tcs_catalog';
DELETE FROM tcs_catalog:'informix'.late_deliverable;
DELETE FROM tcs_catalog:'informix'.late_deliverable_type_lu;
DELETE FROM tcs_catalog:'informix'.deliverable_lu;
DELETE FROM tcs_catalog:'informix'.submission_type_lu;

DELETE FROM tcs_catalog:'informix'.resource_info;
DELETE FROM tcs_catalog:'informix'.resource_info_type_lu;
DELETE FROM tcs_catalog:'informix'.resource;
DELETE FROM tcs_catalog:'informix'.resource_role_lu;

DELETE FROM tcs_catalog:'informix'.project_phase;
DELETE FROM tcs_catalog:'informix'.phase_type_lu;
DELETE FROM tcs_catalog:'informix'.phase_status_lu;

DELETE FROM tcs_catalog:'informix'.project;
DELETE FROM tcs_catalog:'informix'.project_status_lu;

DELETE FROM tcs_catalog:'informix'.project_category_lu;
DELETE FROM tcs_catalog:'informix'.project_type_lu;

# Database 'corporate_oltp';
DELETE FROM corporate_oltp:'informix'.tc_direct_project;
DELETE FROM corporate_oltp:'informix'.user_permission_grant;