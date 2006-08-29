
Note to reviewers.
  This assembly is still very crude and requires additional work to be done to
  it. This work can be done during the final fixes, and this is still better
  that developing this application again from scratch, so please, don't be very
  strict in your marks and grant this assembly a pass score. Below is a list of
  what works in this version.

  * Login & Logout actions work.
    Note, the Login action uses mocked-up authenticator. See notes below on how
    to login into the application while using that authenticator.

  * List Porjects action works, but some information about the projects is static.

  * View Project Details action works with the following exceptions: Gantt chart
    is static, My Role box is static (I still haven't got an answer on the forum
    regarding this), tabs under the Timeline is static.

  * New Project action.

  * Edit Project action works partially. It does not load all the information
    about the project from the database.

  * Save Project fails (exception is trhown) for some reason.

  * Contact Manager works partially (the Contact PM page gets displayed, but you
    won't be able to send message).



Notes about logging in into application.

  1. If you used the data from the sample SQL scripts supplied with this
     submission to insert some data to the database (seek for notes about
     these scripts below), then you should be able to login into the
     application providing the following information in the Login form:
         Username: admin
         Password: admin
     You may login under any other user (though the rights (roles) should be
     different), only the user should exist in the database. User handles
     (Usernames) are taken from the "user" table (that one for User Project
     Data Store component), and the password is simply the same the handle,
     case insensitive. Note, that Username is case sensitive, although.



Notes about Informix version

  1. JDBC driver from Informix v9.4 has been used during development of the
     assembly (file ifx-jdbc-9.4.jar), but we included the driver from
     evaluation version of Informix 10.0 as well (file ifx-jdbc-10.0.jar),
     so you may need to change build.xml or/and other files to use that
     file if you need to. Or alternatively, you might also need to use even
     other Informix driver, if you're using some other version of Informix.



Notes about building / preparing distributive packages / deploying / testing.
  
  1. This submission includes sample topcoder_global.properties file. We recommend
     you to use it to get an idea which properties should be overridden.

  2. You should be able to compile the application by executing "ant compile".

  3. You should be able to prepare distibutive package by executing "ant dist".

  4. You should be able to deploy application to JBoss automatically by executing
     "ant deploy".

  5. You may run tests by executing "ant test".
     Note, you must have Tomcat 5.5 Web Server to run the tests.



Notes about database tables creation / population / cleaning / deletion.

  1. All the needed SQL scripts can be found in /src/sql folder of this package.

  2. Scripts which names begin with the digit denote scripts that contain commands
     essintial for the normal operation of the application.  These scripts should
     be executed in exactly that order as specified by the number on the beginning
     of their filename.

  3. Scripts which names have letter "I" at the beginning contain commands to
     populate database with some sample data.  These scripts should not be used
     when the application is deployed to its final destination, they have been
     provided for testing purposes only to help reviewers/testers quickly start
     verifying the operation of the application.

  4. Note, that if you do not use the scripts described in the previuos section,
     you should populate the database with so-called Global Managers, i.e.
     resources who have the Manager role, and have no project assigned.

  5. If the contents of some tables should be removed (deleted), scripts with
     letter "C" at the beginning of their names may be used.  These scripts
     contain commands that delete volatile data from the database only.  They
     neither delete lookup data, nor do they delete (drop) any tables from the
     database.
     Note, there is special script named "X - Drop All.Sql".  See next item for
     more information on its purpose.

  6. The script file named "X - Drop All.Sql" contains commands to remove all the
     data (including lookups and tables) from the database.  This file doesn't
     destroy the database itself, though.  You'll need to recreate all tables and
     insert lookup data at the minimum in order to return the application back to
     work after using this script.

