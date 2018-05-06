To setup testing environment.

0. Suppose JBoss is used to test.
   If other AppServer is used, configuration needs change.

1. Execute Security_Manager.SQL script.
   Please change mysql-ds.xml according to the database used.

2. Depoly mysql-ds.xml to JBoss. (just copy to deploy folder)

3. Deploy security_app.ear to JBoss.
   This package is found from the security manager component.
   If you change the configuration file in this EAR,
   You may need to change test_files/Online_Review_Login.xml accordingly.

-- Thank you!