
Notes on testing some actions from Project Admin & Review

  1. Upload Submission

     To go to the upload submission page you should enter the following into
     the Address field of your browser:

       http://localhost:8080/online_review/actions/UploadSubmission.do?method=uploadSubmission&pid=<id-of-project>

		 where <id-of-project> part is the ID of any of the existing projects. The
		 user you are logged under must have a Submitter role for that project to
		 be able to upload submission.
		 After uploading a file to the server you may inspect a folder the path to
		 which is specified by the "FileStorePath" property in the configuration
		 file. You may also excute the following SQL commands to verify the state
		 of the database after uploading the file:

		   SELECT * FROM upload;
		   SELECT * FROM submission;

		 Also, it is possible to use an ID of the newly-created submission in
		 subsequent testing of actions from Project Review assembly.

  2. Download Submission

     To download a submission you need to know an ID of its corresponding active
     upload (Managers can download even uploads that have "Deleted" status
     though). Here is the link to start download:

       http://localhost:8080/online_review/actions/DownloadSubmission.do?method=downloadSubmission&pid=<id-of-upload>

     It is advisable to first use Upload Submission action to create some
     uploads in the file store.


Notes on testing some actions from Project Review assembly.

  1. Create Screening

     To create screening you should enter the following into the Address field of
     your browser:

       http://localhost:8080/online_review/actions/CreateScreening.do?method=createScreening&sid=<id-of-submission>

     where <id-of-submission> part is an integer number. If you used helper SQL
     scripts, which insert test data into the database, there are following
     submission IDs available: 3000, 3001, and 3002.

  2. Create Review & Create Approval

     The links for these two actions are almost the same as the link for Create
     Screening action:

       http://localhost:8080/online_review/actions/CreateReview.do?method=createReview&sid=<id-of-submission>

     for Create Review, and

       http://localhost:8080/online_review/actions/CreateApproval.do?method=createApproval&sid=<id-of-submission>

     for Create Approval actions.

  3. Edit Screening

     To edit screening you should create its draft version first (by using
     Create Screening action). After that, you can use the following link to
     edit screening:

       http://localhost:8080/online_review/actions/EditScreening.do?method=editScreening&rid=<id-of-review>

     where <id-of-review> is the ID of screening review created by Create Screening
     action. You may find out the actual value for this ID by inspecting the contents
     of your database (table "review" in particular).

  4. Edit Review & Edit Approval

     The links for these two actions are:

       http://localhost:8080/online_review/actions/EditReview.do?method=editReview&rid=<id-of-review>
     and
       http://localhost:8080/online_review/actions/EditApproval.do?method=editApproval&rid=<id-of-review>

     respectively. Refer to the previous section to find out where to ged a value
     for <id-of-review> part of the above two links.

  5. Save Screening, Save Review & Save Approval

     You don't have to undertake special actions to access these Struts Actions.
     They may easily be accessed from appropriate Create/Edit pages.

  6. View Screening

     To view screening you must finish (commit) some screening review.
     The link for this Struts Action is:

       http://localhost:8080/online_review/actions/ViewScreening.do?method=viewScreening&rid=<id-of-review>

     Refer to section 3 for information on <id-of-review>.
     Note, that you may view only finished (committed) screenings.

  7. View Review & View Approval

     The links for these two actions are:

       http://localhost:8080/online_review/actions/ViewReview.do?method=viewReview&rid=<id-of-review>
     and
       http://localhost:8080/online_review/actions/ViewApproval.do?method=viewApproval&rid=<id-of-review>

     Refer to section 3 for information on <id-of-review>.
     Note, that you may view only finished (committed) reviews and approvals.

  8. Note also, that you must log in under an appropriate user to be able to
     perform the aforementioned actions.


Notes about logging in into the application.

  1. If you used the data from the sample SQL scripts supplied with this
     submission to insert some data to the database (seek for notes about
     these scripts below), then you should be able to login into the
     application providing the following information in the Login form:
         Username: admin
         Password: admin
     You may login under any other user (though the rights (roles) should be
     different), only the user should exist in the database. User handles
     (Usernames) are taken from the "user" table (that one for User Project
     Data Store component), and the password is simply the same as the handle,
     case insensitive. Note, that Username is case sensitive, although.



Notes about Informix version

  Although we used Informix v9.4 JDBC driver during the development of Admin
  part of assembly, we had to switch to the Informix v10.0 one, because of
  the read-only feature used by one of the componets (the Deliverables
  Management Persistence one). So, from now on, only the driver from evaluation
  version of Informix 10.0 is included with the submission. If you have
  Informix 9.4 installed on your machine, it should work pretty well with the
  newer version of the driver (at least it worked for us).


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
     letter "X" at the beginning of their names may be used.  These scripts
     contain commands that delete volatile data from the database only.  They
     neither delete lookup data, nor do they delete (drop) any tables from the
     database.
     Note, there is special script named "X - Drop All.Sql".  See next item for
     more information on its purpose.

  6. The script file named "X - Drop All.Sql" contains commands to remove all the
     data (including lookups and tables) from the database.  This file doesn't
     destroy the database itself, though.  You'll need to recreate all tables and
     insert lookup data at the minimum in order to return the application back to
     working state after using this script.

