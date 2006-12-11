CREATE TABLE email (
  user_id                       DECIMAL(10,0),
  email_id                      DECIMAL(10,0),
  email_type_id                 DECIMAL(5,0),
  address                       VARCHAR(100),
  create_date                   DATETIME YEAR TO FRACTION(3),
  modify_date                   DATETIME YEAR TO FRACTION(3),
  primary_ind                   DECIMAL(1,0),
  status_id                     DECIMAL(3,0),
  PRIMARY KEY(email_id)
);
CREATE TABLE user (
  user_id                       DECIMAL(10,0)                   NOT NULL,
  first_name                    VARCHAR(64),
  last_name                     VARCHAR(64),
  create_date                   DATETIME YEAR TO FRACTION(3),
  modify_date                   DATETIME YEAR TO FRACTION(3),
  handle                        VARCHAR(50)                     NOT NULL,
  last_login                    DATETIME YEAR TO FRACTION(3),
  status                        VARCHAR(3)                      NOT NULL,
  password                      VARCHAR(15),
  activation_code               VARCHAR(32),
  middle_name                   VARCHAR(64),
  handle_lower                  VARCHAR(50),
  timezone_id                   DECIMAL(5,0),
  PRIMARY KEY(user_id)
);
CREATE TABLE user_rating (
  user_id                       DECIMAL(10)                     NOT NULL,
  rating                        DECIMAL(10)     DEFAULT 0       NOT NULL,
  phase_id                      DECIMAL(3)                      NOT NULL,
  vol                           DECIMAL(10)     DEFAULT 0       NOT NULL,
  rating_no_vol                 DECIMAL(10)     DEFAULT 0       NOT NULL,
  num_ratings                   DECIMAL(5)      DEFAULT 0       NOT NULL,
  mod_date_time                 DATETIME YEAR TO FRACTION(3)    NOT NULL,
  create_date_time              DATETIME YEAR TO FRACTION(3)    NOT NULL,
  last_rated_project_id         DECIMAL(12,0),
  PRIMARY KEY(user_id, phase_id)
);
CREATE TABLE user_reliability (
  user_id                       DECIMAL(10,0),
  rating                        DECIMAL(5,4),
  modify_date                   DATETIME YEAR TO FRACTION(3),
  create_date                   DATETIME YEAR TO FRACTION(3),
  phase_id                      DECIMAL(12,0),
  PRIMARY KEY(user_id, phase_id)
);
