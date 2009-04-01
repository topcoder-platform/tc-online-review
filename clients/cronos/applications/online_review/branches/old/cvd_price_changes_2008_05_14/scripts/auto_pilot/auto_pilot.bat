@echo off
set classpath=.
set LIB=E:/tcs/app/online_review/lib
set classpath=%classpath%;%JBOSS_HOME%/client/jbossall-client.jar
set classpath=%classpath%;%JBOSS_HOME%/client/jboss-j2ee.jar
set classpath=%classpath%;%LIB%/tcs/auto_pilot/1.0/auto_pilot.jar 
set classpath=%classpath%;%LIB%/tcs/base_exception/1.0/base_exception.jar
set classpath=%classpath%;%LIB%/tcs/class_associations/1.0/class_associations.jar
set classpath=%classpath%;%LIB%/tcs/configuration_manager/2.1.5/configuration_manager.jar
set classpath=%classpath%;%LIB%/tcs/command_line_utility/1.0/command_line_utility.jar
set classpath=%classpath%;%LIB%/tcs/database_abstraction/1.1/database_abstraction.jar
set classpath=%classpath%;%LIB%/tcs/data_validation/1.0/data_validation.jar
set classpath=%classpath%;%LIB%/tcs/db_connection_factory/1.0/db_connection_factory.jar
set classpath=%classpath%;%LIB%/tcs/executable_wrapper/1.0/executable_wrapper.jar
set classpath=%classpath%;%LIB%/tcs/id_generator/3.0/id_generator.jar
set classpath=%classpath%;%LIB%/tcs/job_scheduler/1.0/job_scheduler.jar
set classpath=%classpath%;%LIB%/tcs/logging_wrapper/1.2/logging_wrapper.jar
set classpath=%classpath%;%LIB%/tcs/object_factory/2.0.1/object_factory.jar
set classpath=%classpath%;%LIB%/tcs/project_management/1.0/project_management.jar
set classpath=%classpath%;%LIB%/tcs/project_management_persistence/1.0/project_management_persistence.jar
set classpath=%classpath%;%LIB%/tcs/phase_management/1.0.1/phase_management.jar
set classpath=%classpath%;%LIB%/tcs/phase_management_persistence/1.0/phase_management_persistence.jar
set classpath=%classpath%;%LIB%/tcs/project_phases/2.0/project_phases.jar
set classpath=%classpath%;%LIB%/tcs/search_builder/1.3/search_builder.jar
set classpath=%classpath%;%LIB%/tcs/typesafe_enum/1.0/typesafe_enum.jar
set classpath=%classpath%;%LIB%/tcs/workdays/1.0/workdays.jar
set classpath=%classpath%;%LIB%/third_party/xerces.jar
set classpath=%classpath%;%LIB%/third_party/log4j.jar
set classpath=%classpath%;%LIB%/third_party/ifx-jdbc.jar

java -classpath "%classpath%" com.topcoder.management.phase.autopilot.AutoPilotJob -config auto_pilot.xml -namespace AutoPilotJob -autopilot com.topcoder.management.phase.autopilot.AutoPilot -poll 5
rem java -classpath "%classpath%" com.topcoder.management.phase.autopilot.AutoPilotJob -config auto_pilot.xml  -namespace com.topcoder.management.phase.autopilot.AutoPilotJob -autopilot com.topcoder.management.phase.autopilot.AutoPilot -project