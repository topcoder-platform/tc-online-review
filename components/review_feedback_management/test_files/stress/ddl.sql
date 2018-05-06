create table 'informix'.project (
    project_id INT not null,
    project_status_id INT not null,
    project_category_id INT not null,
    project_studio_spec_id INTEGER,                        
    create_user VARCHAR(64) not null,
    create_date DATETIME YEAR TO FRACTION not null,
    modify_user VARCHAR(64) not null,
    modify_date DATETIME YEAR TO FRACTION not null,
    tc_direct_project_id INT
)
extent size 1000 next size 1000
lock mode row;   

alter table 'informix'.project add constraint primary key 
    (project_id)
    constraint pk_project;
    
CREATE TABLE "informix".review_feedback (
    review_feedback_id serial NOT NULL,
    project_id INT not null,
    reviewer_user_id DECIMAL(10,0) NOT NULL,
    score INTEGER NOT NULL,
    feedback_text lvarchar(4096),
    create_user VARCHAR(64) NOT NULL,
    create_date DATETIME YEAR TO FRACTION NOT NULL
)
extent size 3000 next size 3000
lock mode row;


alter table 'informix'.review_feedback add constraint primary key
  (review_feedback_id)
  constraint pk_review_feedback; 

    
  alter table 'informix'.review_feedback add constraint foreign key
    (project_id)
    references 'informix'.project
    (project_id)
    constraint fk_reviewfeedback_project_projectid;
    
    