You should update the DB configuration from the following files:
test_files/dbfactory.xml
test_files/accuracytests/dbconnectionfactory.xml
test_files/failuretests/DBConnectionFactory.xml
test_files/failuretests/dbfactory.xml
test_files/stresstests/dbconfig.xml

What I have done.

1. Code refactor since new compile target is used.
1.1 use generic type for collections.
1.2 remove the explicit boxing code, like new Long(submssion.getId()).