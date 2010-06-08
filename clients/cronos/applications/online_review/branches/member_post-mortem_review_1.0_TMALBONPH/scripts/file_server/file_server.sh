CP=""
CP=$CP:/home/onlinereview/online_review/lib/tcs/auto_pilot/1.0/auto_pilot.jar 
CP=$CP:/home/onlinereview/online_review/lib/tcs/base_exception/1.0/base_exception.jar
CP=$CP:/home/onlinereview/online_review/lib/tcs/heartbeat/1.0/heartbeat.jar
CP=$CP:/home/onlinereview/online_review/lib/tcs/configuration_manager/2.1.5/configuration_manager.jar
CP=$CP:/home/onlinereview/online_review/lib/tcs/command_line_utility/1.0/command_line_utility.jar
CP=$CP:/home/onlinereview/online_review/lib/tcs/data_validation/1.0/data_validation.jar
CP=$CP:/home/onlinereview/online_review/lib/tcs/db_connection_factory/1.0/db_connection_factory.jar
CP=$CP:/home/onlinereview/online_review/lib/tcs/id_generator/3.0/id_generator.jar
CP=$CP:/home/onlinereview/online_review/lib/tcs/logging_wrapper/1.2/logging_wrapper.jar
CP=$CP:/home/onlinereview/online_review/lib/tcs/guid_generator/1.0/guid_generator.jar
CP=$CP:/home/onlinereview/online_review/lib/tcs/typesafe_enum/1.0/typesafe_enum.jar
CP=$CP:/home/onlinereview/online_review/lib/tcs/file_system_server/1.0/file_system_server.jar
CP=$CP:/home/onlinereview/online_review/lib/tcs/ip_server/2.0.1/ip_server.jar
CP=$CP:/home/onlinereview/online_review/lib/third_party/xerces.jar
CP=$CP:/home/onlinereview/online_review/lib/third_party/log4j.jar
CP=$CP:/home/onlinereview/online_review/lib/third_party/ifx-jdbc.jar
CP=$CP:file_server.jar

/usr/java/jdk1.5.0_06/bin/java -cp $CP com.topcoder.onlinereview.SubmissionFileSystemServerTool -config file_server.xml
