1. Modify the the path for dependencies in "build.xml".

2. Create databases tcs and test in mysql server, and change connection string in all the configure
   xml files under test_files folder.


Suggestion:
Don't use the path which contains spaces, like "D:\DB Connection Factory" would fail on finding some files
for testing. "D:\DB_Connection_Factory" is ok.