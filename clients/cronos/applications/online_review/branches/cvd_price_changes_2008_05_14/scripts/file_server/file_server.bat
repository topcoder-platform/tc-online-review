set classpath=.
set LIB=E:/tcs/app/online_review/lib
set classpath=%classpath%;%LIB%/tcs/auto_pilot/1.0/auto_pilot.jar 
set classpath=%classpath%;%LIB%/tcs/base_exception/1.0/base_exception.jar
set classpath=%classpath%;%LIB%/tcs/heartbeat/1.0/heartbeat.jar
set classpath=%classpath%;%LIB%/tcs/configuration_manager/2.1.5/configuration_manager.jar
set classpath=%classpath%;%LIB%/tcs/command_line_utility/1.0/command_line_utility.jar
set classpath=%classpath%;%LIB%/tcs/data_validation/1.0/data_validation.jar
set classpath=%classpath%;%LIB%/tcs/db_connection_factory/1.0/db_connection_factory.jar
set classpath=%classpath%;%LIB%/tcs/id_generator/3.0/id_generator.jar
set classpath=%classpath%;%LIB%/tcs/logging_wrapper/1.2/logging_wrapper.jar
set classpath=%classpath%;%LIB%/tcs/guid_generator/1.0/guid_generator.jar
set classpath=%classpath%;%LIB%/tcs/typesafe_enum/1.0/typesafe_enum.jar
set classpath=%classpath%;%LIB%/tcs/file_system_server/1.0/file_system_server.jar
set classpath=%classpath%;%LIB%/tcs/ip_server/2.0.1/ip_server.jar
set classpath=%classpath%;%LIB%/third_party/xerces.jar
set classpath=%classpath%;%LIB%/third_party/dom.jar 
set classpath=%classpath%;%LIB%/third_party/log4j.jar
set classpath=%classpath%;%LIB%/third_party/xml-api.jar
set classpath=%classpath%;%LIB%/third_party/ifx-jdbc.jar
set classpath=%classpath%;file_server.jar

java -classpath "%classpath%" com.topcoder.onlinereview.SubmissionFileSystemServerTool -config file_server.xml
rem java -classpath "%classpath%" com.topcoder.management.phase.autopilot.AutoPilotJob -config auto_pilot.xml  -namespace com.topcoder.management.phase.autopilot.AutoPilotJob -autopilot com.topcoder.management.phase.autopilot.AutoPilot -project