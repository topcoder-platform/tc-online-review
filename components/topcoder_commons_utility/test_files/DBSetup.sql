
-- ----------------------------------------------
-- DDL Statements for table "issue_types"
-- ----------------------------------------------
CREATE TABLE IF NOT EXISTS issue_types
(
	id BIGINT,
	name VARCHAR(50)
)
ENGINE = InnoDB;

-- ----------------------------------------------
-- DDL Statements for table "issues"
-- ----------------------------------------------
CREATE TABLE IF NOT EXISTS issues
(
	issue_type_id BIGINT,
	user_id INTEGER,
	priority INTEGER,
	start_date TIMESTAMP NULL
)
ENGINE = InnoDB;
