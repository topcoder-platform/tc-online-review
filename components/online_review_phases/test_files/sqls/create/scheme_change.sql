alter table review add initial_score  FLOAT;

create table project_phase_audit (
project_phase_id int not null,
scheduled_start_time datetime year to fraction,
scheduled_end_time datetime year to fraction,
audit_action_type_id int not null,
action_date datetime year to fraction not null,
action_user_id decimal(12,0) not null
);