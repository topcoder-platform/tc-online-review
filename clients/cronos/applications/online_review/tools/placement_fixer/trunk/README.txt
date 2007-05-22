Requirement:
The application has to find any final scores that doesn't correspond to the average of the three post-appeals scores.
After running, it has to return a report listing what projects has position changes:
a new first place a new second place a submission change from non-passing to passing a submission change from passing to non-passing
The tables that must be updated if there is a change are: project_result, project_info, resource_info, submission.

Additionally, the application has to provide a command line switch to change between
two ways of running:  Only generates the report (default) Generates the report and performs the database changes.
Thanks,

===============================================================================

1) modify the config.xml in conf directory, the config file only cotains the configuration of 
DB connection factory.

2) run statistics.bat to generate the report into report.txt

3) run update.bat to generate the report and fix the incorrect data in DB.

