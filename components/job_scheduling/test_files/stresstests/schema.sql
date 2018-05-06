CREATE TABLE id_sequences (
    name                VARCHAR(255) NOT NULL  PRIMARY KEY,
    next_block_start    INT8 NOT NULL,
    block_size          INT NOT NULL,
    exhausted		int default 0
);

INSERT INTO id_sequences (name, next_block_start, block_size, exhausted) VALUES ('job', 0, 10, 0);

CREATE TABLE JOB (
JobId INT NOT NULL PRIMARY KEY,
Name VARCHAR(40) NOT NULL,
StartDate DATE,
StartTime INT,
EndDate DATE,
DateUnit VARCHAR(60) NOT NULL,
DateUnitDays VARCHAR(20),
DateUnitWeek VARCHAR(2),
DateUnitMonth VARCHAR(2),
Interval INTEGER NOT NULL,
Recurrence INTEGER NOT NULL,
Active CHAR(1) NOT NULL,
JobType VARCHAR(50) NOT NULL,
JobCommand VARCHAR(255) NOT NULL,
DependenceJobName VARCHAR(60),
DependenceJobStatus VARCHAR(20),
DependenceJobDelay VARCHAR(20)
);

CREATE TABLE Message (
MessageId INT NOT NULL PRIMARY KEY,
OwnerId INT NOT NULL,
Name VARCHAR(40) NOT NULL,
FromAddress VARCHAR(40) NOT NULL,
Subject VARCHAR(40) NOT NULL,
TemplateText LVARCHAR,
Recipients VARCHAR(255) NOT NULL
);

CREATE TABLE Group (
GroupId INTEGER NOT NULL PRIMARY KEY,
Name VARCHAR(40)
);

CREATE TABLE GroupJob (
GroupId INTEGER NOT NULL,
JobId INTEGER NOT NULL
);