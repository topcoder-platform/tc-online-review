create table 'informix'.user_rating (
    user_id DECIMAL(10) not null,
    rating DECIMAL(10) default 0 not null,
    phase_id DECIMAL(3) not null,
    vol DECIMAL(10) default 0 not null,
    rating_no_vol DECIMAL(10) default 0 not null,
    num_ratings DECIMAL(5) default 0 not null,
    mod_date_time DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION not null,
    create_date_time DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION not null,
    last_rated_project_id DECIMAL(12,0)
)
extent size 250 next size 124
lock mode row;

alter table 'informix'.user_rating add constraint primary key 
	(user_id, phase_id)
	constraint pk_user_rating;

create table 'informix'.user_reliability (
    user_id DECIMAL(10,0),
    rating DECIMAL(5,4),
    modify_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION,
    create_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION,
    phase_id DECIMAL(12,0)
)
extent size 32 next size 32
lock mode row;

alter table 'informix'.user_reliability add constraint primary key 
	(user_id, phase_id)
	constraint user_reliability_pkey;

alter table 'informix'.user_reliability add constraint foreign key 
	(phase_id)
	references 'informix'.phase
	(phase_id) 
	constraint userreliability_phase_fk;

