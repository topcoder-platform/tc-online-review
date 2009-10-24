JAVACMD=${JAVA_HOME}/bin/java
if [ "x$JBOSS_HOME" = "x" ]; then
    JBOSS_HOME="/home/$USER/jboss-4.0.2"
    echo "WARNING: The JBOSS_HOME environment variable is not defined. setting to: $JBOSS_HOME"
fi
MAIN=com.topcoder.management.phase.autopilot.AutoPilotJob
LOGFILE=auto_pilot-`date +%Y-%m-%d-%H-%M-%S`.log

CP=""
CP=$CP:.
CP=$CP:lib/phase_handler_extend.jar
CP=$CP:$JBOSS_HOME/client/jbossall-client.jar
CP=$CP:$JBOSS_HOME/client/jboss-j2ee.jar
CP=$CP:lib/auto_pilot.jar
CP=$CP:lib/auto_screening_management.jar
CP=$CP:lib/base_exception.jar
CP=$CP:lib/class_associations.jar
CP=$CP:lib/command_line_utility.jar
CP=$CP:lib/configuration_manager.jar
CP=$CP:lib/data_validation.jar
CP=$CP:lib/database_abstraction.jar
CP=$CP:lib/db_connection_factory.jar
CP=$CP:lib/deliverable_management.jar
CP=$CP:lib/deliverable_management_persistence.jar
CP=$CP:lib/document_generator.jar
CP=$CP:lib/email_engine.jar
CP=$CP:lib/executable_wrapper.jar
CP=$CP:lib/generic_event_manager.jar
CP=$CP:lib/guid_generator.jar
CP=$CP:lib/id_generator.jar
CP=$CP:lib/job_scheduler.jar
CP=$CP:lib/logging_wrapper.jar
CP=$CP:lib/magic_numbers.jar
CP=$CP:lib/object_factory.jar
CP=$CP:lib/online_review_deliverables.jar
CP=$CP:lib/online_review_phases.jar
CP=$CP:lib/phase_management.jar
CP=$CP:lib/phase_management_persistence.jar
CP=$CP:lib/project_management.jar
CP=$CP:lib/project_management_persistence.jar
CP=$CP:lib/project_phases.jar
CP=$CP:lib/project_phase_template.jar
CP=$CP:lib/resource_management.jar
CP=$CP:lib/resource_management_persistence.jar
CP=$CP:lib/review_data_structure.jar
CP=$CP:lib/review_management.jar
CP=$CP:lib/review_management_persistence.jar
CP=$CP:lib/review_score_aggregator.jar
CP=$CP:lib/review_score_calculator.jar
CP=$CP:lib/scorecard_data_structure.jar
CP=$CP:lib/scorecard_management.jar
CP=$CP:lib/scorecard_management_persistence.jar
CP=$CP:lib/search_builder.jar
CP=$CP:lib/simple_cache.jar
CP=$CP:lib/typesafe_enum.jar
CP=$CP:lib/user_project_data_store.jar
CP=$CP:lib/weighted_calculator.jar
CP=$CP:lib/workdays.jar
CP=$CP:lib/ifx-jdbc.jar
CP=$CP:lib/mail.jar
CP=$CP:lib/log4j.jar
CP=$CP:lib/activation.jar
CP=$CP:lib/xerces.jar
CP=$CP:lib/PactsClientServices.jar
CP=$CP:lib/tcwebcommon.jar
CP=$CP:lib/tcsUtil.jar
CP=$CP:lib/shared.jar

OPTIONS="-cp $CP"

if [[ $1 != "" ]] ; then
    CMD=$1
    shift
fi

if [ "$CMD" = "start" ] ; then
	echo "-------------------------------------------------------------------"
	echo "-- JAVACMD    : $JAVACMD"
	echo "-- JBOSS_HOME : $JBOSS_HOME"
	echo "-- Classpath  : $CP"
	echo "-------------------------------------------------------------------"
    nohup $JAVACMD $OPTIONS $MAIN -config auto_pilot.xml -namespace AutoPilotJob -autopilot com.topcoder.management.phase.autopilot.AutoPilot -poll 1 >$LOGFILE 2>&1 &
    echo $! > autopilot.pid
elif [ "$CMD" = "stop" ] ; then
    kill `cat autopilot.pid`
    rm -f autopilot.pid
elif [ "$CMD" = "restart" ] ; then
    $0 stop
    $0 start
elif [ "$CMD" = "test" ] ; then
	$JAVACMD $OPTIONS $MAIN -config auto_pilot.xml -namespace AutoPilotJob -autopilot com.topcoder.management.phase.autopilot.AutoPilot -poll 1
else
    echo "Usage:"
    echo "auto_pilot.sh (start|stop|restart|test)"
    echo "      start - start auto_pilot"
    echo "      stop  - stop auto_pilot"
    echo "      restart  - restart auto_pilot"
    echo "      test - start auto_pilot without without sending the process to background"
fi

