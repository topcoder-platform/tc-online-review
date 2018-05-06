1. Configure libs in build.xml
2. Run test_files/create_schema.sql and script.sql in Informix
   NOTE: The target database should support transaction.
3. Change the connection parameters in test_files/DBConnection_Factory.xml
4. Run <ant test>

The resource_management.jar is in test_files/lib, its source code is in test_files/source_resource_management


Thank you!