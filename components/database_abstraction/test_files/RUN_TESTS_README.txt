In order to run the tests, the following steps should be taken:
1. Set up two odbc datasources named "Tester" and "AccuracyTester", which refer to "test_files/Tester.mdb" and "test_files/aaccuracy/Tester.mdb" respectively.
2. Configure an Informix table with

CREATE TABLE ABSTRACTION_TABLE(
  ID NUMERIC(10) NOT NULL,
  NAME VARCHAR(10),
  AGE INT8,
  BLOB_T BLOB,
  CLOB_T CLOB,
  DATE_T DATE,
  URL VARCHAR(100));

biotrail, suhugo