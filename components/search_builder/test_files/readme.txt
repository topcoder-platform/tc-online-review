    For runing my testcase, please follow the section 4.1 in CS.
The ldap schema in section 4.1 should be added in ldap before run the testcase,
and make should your ldap server is running when run the testcase.
Before run the testcase, the ldap init state should be empty(no entry exists).

And the test java for version 1.2 is end with 12, such as


run the sql for you database before run testcase.

All the jars needed refer to my build.xml

Some useless classes in old version is remove, and it will make easy for reviewer,
if PM required to keep them, they can be added in final fix.

The CS is updated.
Thanks for reading, good luck!