create table 'informix'.link_type_lu (
    link_type_id INT not null,
    link_type_name VARCHAR(64) not null
)


alter table 'informix'.link_type_lu add constraint primary key 
	(link_type_id)
	constraint pk_link_type_lu;

create table 'informix'.linked_project_xref (
    source_project_id INT,
    dest_project_id INT,
    link_type_id INT
)


alter table 'informix'.linked_project_xref add constraint primary key 
	(source_project_id, dest_project_id)
	constraint pk_linked_project_xref;

alter table 'informix'.linked_project_xref add constraint foreign key 
	(source_project_id)
	references 'informix'.project
	(project_id) 
	constraint fk_linked_project_xref_project_projectid;

alter table 'informix'.linked_project_xref add constraint foreign key 
	(dest_project_id)
	references 'informix'.project
	(project_id) 
	constraint fk_linked_project_xref_project_lu_destprojectid;

alter table 'informix'.linked_project_xref add constraint foreign key 
	(link_type_id)
	references 'informix'.link_type_lu
	(link_type_id) 
	constraint fk_linked_project_xref_link_type_lu_linktypeid;
