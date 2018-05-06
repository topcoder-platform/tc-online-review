For running all the unit tests:

1. Install informix 10.00, and create a server for the component.

2. Install jdbc driver for informix, and add ifxjdbc.jar, ifxjdbcx.jar, ifxlang.jar, ifxlsupp.jar,
   ifxsqlj.jar, ifxtools.jar which are under the lib directory of the installed driver into the
   System ClassPath.
   
3. Use client of informix, connect to the server, and then create a database.

4. Use the "table_create.sql" under "\test_files" to create and alter tables into the database
   you have just created.
   
5. Change the connections config in the "SampleConfig.xml" and "FailureConfig.xml" under "\test_files",
   "Config.xml" under "\test_files\Accuracy", "InvalidConfigFile.xml" and "ConfigFile.xml" under "\test_files\FailureTests",
   "db_connection_factory.xml" under "\test_files\stresstest", 
   
   Like: 
    <Property name="jdbc_url">
        <Value>jdbc:informix-sqli://[Name or IP]:[Port]/[Database Name]:INFORMIXSERVER=[Server Name]</Value>
    </Property>
    <Property name="user">
        <Value>[User Name]</Value>
    </Property>
    <Property name="password">
        <Value>[Psw]</Value>
    </Property>
   
   Here:
   [Name or IP]     should be the name of your computer, or you can use your ip.
   [Port]           should be the port which you specified during your server creating (Step 1).
   [Database Name]  should be the name of your database which you specified during your database creating (Step 3).
   [Server Name]    should be the server name which you specified during your server creating (Step 1).
   [User Name]      should be the user name which you specified during your server creating (Step 1).
   [Psw]            should be the password of the user which you specified during your server creating (Step 1).
   
   
Notice! The informix only ask for networking, Localhost is not useful, and you must not change ip setting during the test.
