JAVACMD=${JAVA_HOME}/bin/java
MAIN=com.cronos.onlinereview.autoscreening.tool.ScreeningTool
LOGFILE=auto_screening-`date +%Y-%m-%d-%H-%M-%S`.log

CP=$CP:lib/auto_screening_management.jar
CP=$CP:lib/auto_screening_tool.jar
CP=$CP:lib/auto_screening_tool_persistence.jar
CP=$CP:lib/base_exception.jar
CP=$CP:lib/class_associations.jar
CP=$CP:lib/compression_utility.jar
CP=$CP:lib/command_line_utility.jar
CP=$CP:lib/configuration_manager.jar
CP=$CP:lib/data_validation.jar
CP=$CP:lib/database_abstraction.jar
CP=$CP:lib/db_connection_factory.jar
CP=$CP:lib/directory_validation.jar
CP=$CP:lib/executable_wrapper.jar
CP=$CP:lib/file_system_server.jar
CP=$CP:lib/file_upload.jar
CP=$CP:lib/generic_event_manager.jar
CP=$CP:lib/guid_generator.jar
CP=$CP:lib/heartbeat.jar
CP=$CP:lib/id_generator.jar
CP=$CP:lib/ip_server.jar
CP=$CP:lib/job_scheduler.jar
CP=$CP:lib/logging_wrapper.jar
CP=$CP:lib/magic_numbers.jar
CP=$CP:lib/object_factory.jar
CP=$CP:lib/search_builder.jar
CP=$CP:lib/simple_cache.jar
CP=$CP:lib/typesafe_enum.jar
CP=$CP:lib/user_project_data_store.jar
CP=$CP:lib/xmi_parser.jar
CP=$CP:lib/ifx-jdbc.jar 
CP=$CP:lib/log4j.jar
CP=$CP:lib/xerces.jar
CP=$CP:conf
CP=$CP:.


OPTIONS="-cp $CP"

if [[ $1 != "" ]] ; then
    CMD=$1
    shift
fi

if [ "$CMD" = "start" ] ; then
    nohup $JAVACMD $OPTIONS $MAIN -interval 30000 -screenerId 100 -configNamespace com.cronos.onlinereview.autoscreening.tool.Screener >$LOGFILE 2>&1 &
    echo $! > auto_screening.pid
elif [ "$CMD" = "stop" ] ; then
    kill `cat auto_screening.pid`
    rm -f autopilot.pid
elif [ "$CMD" = "restart" ] ; then
    kill `cat auto_screening.pid`
    rm -f autopilot.pid
    nohup $JAVACMD $OPTIONS $MAIN -interval 30000 -screenerId 100 -configNamespace com.cronos.onlinereview.autoscreening.tool.Screener >$LOGFILE 2>&1 &
    echo $! > auto_screening.pid
elif [ "$CMD" = "test" ] ; then
    $JAVACMD $OPTIONS $MAIN -interval 30000 -screenerId 100 -configNamespace com.cronos.onlinereview.autoscreening.tool.Screener
else
    echo "Usage:"
    echo "auto_screening.sh (run|start|stop|restart)"
    echo "      start - start auto_screening"
    echo "      stop  - stop auto_screening"
    echo "      restart  - restart auto_screening"
fi