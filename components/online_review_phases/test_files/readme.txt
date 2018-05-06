a.
run the test cases

1. set up the database, executes test_files/sqls/all.sql(updated in this version)

2. change the configuration in test_files/config/DB_Factory.xml and user_project_data_store.xml,
And they still on test_files/accuracy, test_files/failure, test_files/stress,
DO NOT FORGET test_files/failure/config.xml
Please NOTE: make sure you have update all database connection in test_files.

3. setup the email-engine setting too. dev null smtp server should start first.

4. add test_files into classpath


b. since the test cases are mostly db related, so it takes long time to execute, please be patient.


c. What I have did:

1. code change according to the design and forum discussion.
2. update CS, especially Demo section, the original one is incorrect. see DemoTest also.
3. The test cases for new classes and modification of the original classes are added, but the coverage is still low, mainly because of the original code for too much try-catch blocks.
4. test_files/deliverable_management.jar and test_files/project_management.jar are updated mocks, need to be used.
5. the tcs libs are provided in test_reflib directory, in case you have problem to set it up.
