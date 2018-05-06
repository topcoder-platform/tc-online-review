CREATE TABLE project_type_lu (
  project_type_id               INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(project_type_id)
);
CREATE TABLE project_category_lu (
  project_category_id           INTEGER                         NOT NULL,
  project_type_id               INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(project_category_id),
  FOREIGN KEY(project_type_id)
    REFERENCES project_type_lu(project_type_id)
);
CREATE TABLE project_status_lu (
  project_status_id             INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(project_status_id)
);
CREATE TABLE project (
  project_id                    INTEGER                         NOT NULL,
  project_status_id             INTEGER                         NOT NULL,
  project_category_id           INTEGER                         NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(project_id),
  FOREIGN KEY(project_category_id)
    REFERENCES project_category_lu(project_category_id),
  FOREIGN KEY(project_status_id)
    REFERENCES project_status_lu(project_status_id)
);
CREATE TABLE project_info_type_lu (
  project_info_type_id          INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(25)                     NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(project_info_type_id)
);
CREATE TABLE project_info (
  project_id                    INTEGER                         NOT NULL,
  project_info_type_id          INTEGER                         NOT NULL,
  value                         LVARCHAR(4096)                  NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(project_id, project_info_type_id),
  FOREIGN KEY(project_info_type_id)
    REFERENCES project_info_type_lu(project_info_type_id),
  FOREIGN KEY(project_id)
    REFERENCES project(project_id)
);
CREATE TABLE project_audit (
  project_audit_id              INTEGER                         NOT NULL,
  project_id                    INTEGER                         NOT NULL,
  update_reason                 VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(project_audit_id),
  FOREIGN KEY(project_id)
    REFERENCES project(project_id)
);
CREATE TABLE resource (
  resource_id                   INTEGER                         NOT NULL,
  project_id                    INTEGER,
  PRIMARY KEY(resource_id),
  FOREIGN KEY(project_id)
    REFERENCES project(project_id)
);
CREATE TABLE resource_info_type_lu (
  resource_info_type_id         INTEGER                         NOT NULL,
  name                          VARCHAR(64)                     NOT NULL,
  description                   VARCHAR(254)                    NOT NULL,
  create_user                   VARCHAR(64)                     NOT NULL,
  create_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  modify_user                   VARCHAR(64)                     NOT NULL,
  modify_date                   DATETIME YEAR TO FRACTION(3)    NOT NULL,
  PRIMARY KEY(resource_info_type_id)
);
CREATE TABLE resource_info (
  resource_id                   INTEGER                         NOT NULL,
  resource_info_type_id         INTEGER                         NOT NULL,
  value                         LVARCHAR(4096)                  NOT NULL,
  PRIMARY KEY(resource_id, resource_info_type_id)
);

CREATE TABLE id_sequences (
  name VARCHAR(255) NOT NULL,
  next_block_start INTEGER NOT NULL,
  block_size INTEGER NOT NULL,
  exhausted INTEGER NOT NULL,
  PRIMARY KEY (name)
);


INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('project_id_seq', 1, 20, 0);
INSERT INTO id_sequences(name, next_block_start, block_size, exhausted)
  VALUES('project_audit_id_seq', 1, 20, 0);

INSERT INTO project_status_lu(project_status_id, name, description, create_user, create_date, modify_user, modify_date)
  VALUES(1, 'Active', 'Active', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_status_lu(project_status_id, name, description, create_user, create_date, modify_user, modify_date)
  VALUES(2, 'Inactive', 'Inactive', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_status_lu(project_status_id, name, description, create_user, create_date, modify_user, modify_date)
  VALUES(3, 'Deleted', 'Deleted', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_status_lu(project_status_id, name, description, create_user, create_date, modify_user, modify_date)
  VALUES(4, 'Cancelled - Failed Review', 'Cancelled - Failed Review', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_status_lu(project_status_id, name, description, create_user, create_date, modify_user, modify_date)
  VALUES(5, 'Cancelled - Failed Screening', 'Cancelled - Failed Screening', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_status_lu(project_status_id, name, description, create_user, create_date, modify_user, modify_date)
  VALUES(6, 'Cancelled - Zero Submissions', 'Cancelled - Zero Submissions', 'System', CURRENT, 'System', CURRENT);
INSERT INTO project_status_lu(project_status_id, name, description, create_user, create_date, modify_user, modify_date)
  VALUES(7, 'Completed', 'Completed', 'System', CURRENT, 'System', CURRENT);

INSERT INTO resource_info_type_lu(resource_info_type_id, name, description, create_user, create_date, modify_user, modify_date)
  VALUES(1, 'External Reference ID', 'External Reference ID', 'System', CURRENT, 'System', CURRENT); 
