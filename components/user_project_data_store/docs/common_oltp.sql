create table 'informix'.email (
    user_id DECIMAL(10,0),
    email_id DECIMAL(10,0),
    email_type_id DECIMAL(5,0),
    address VARCHAR(100),
    create_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION,
    modify_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION,
    primary_ind DECIMAL(1,0),
    status_id DECIMAL(3,0)
)
extent size 7500 next size 2750
lock mode row;

create index 'informix'.email_user_id_idx on 'informix'.email
	(
	user_id, 
	primary_ind
	);

alter table 'informix'.email add constraint primary key 
	(email_id)
	constraint u110_23;

alter table 'informix'.email add constraint foreign key 
	(email_type_id)
	references 'informix'.email_type_lu
	(email_type_id) 
	constraint email_emailtypelu_fk;

alter table 'informix'.email add constraint foreign key 
	(status_id)
	references 'informix'.email_status_lu
	(status_id) 
	constraint email_emailstatuslu_fk;



create table 'informix'.user (
    user_id DECIMAL(10,0) not null,
    first_name VARCHAR(64),
    last_name VARCHAR(64),
    create_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION,
    modify_date DATETIME YEAR TO FRACTION default CURRENT YEAR TO FRACTION,
    handle VARCHAR(50) not null,
    last_login DATETIME YEAR TO FRACTION,
    status VARCHAR(3) not null,
    password VARCHAR(15),
    activation_code VARCHAR(32),
    middle_name VARCHAR(64),
    handle_lower VARCHAR(50),
    timezone_id DECIMAL(5,0)
)
extent size 10000 next size 5000
lock mode row;

create index 'informix'.user_handle_idx on 'informix'.user
	(
	handle
	);

create index 'informix'.user_lower_handle_idx on 'informix'.user
	(
	handle_lower
	);

alter table 'informix'.user add constraint primary key 
	(user_id)
	constraint u124_45;

alter table 'informix'.user add constraint foreign key 
	(timezone_id)
	references 'informix'.timezone_lu
	(timezone_id) 
	constraint user_timezonelu_fk;

