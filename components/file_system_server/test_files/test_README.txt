1. Initialize your mysql database. 
2. Execute the following sql scripts to set up the databases. 
-test_files\database\id_generator.sql
3. Update the following configuration files, you need to modify the connection string for 
databases. 
-test_files\DBConnectionFactoryImpl.xml
4. Place test_files/accuracy/FSSDiskSpaceChecker.dll on your PATH 
(if not testing in Windows, your will need to compile the library from the provided sources).
