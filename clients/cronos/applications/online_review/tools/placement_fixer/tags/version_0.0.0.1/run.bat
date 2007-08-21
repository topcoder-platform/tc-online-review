@echo off
set CP=conf
set CP=%CP%;build\classes
set CP=%CP%;lib\ext\log4j.jar
set CP=%CP%;lib\ext\ifxjdbc.jar
set CP=%CP%;lib\tcs\base_exception\1.0\base_exception.jar
set CP=%CP%;lib\tcs\configuration_manager\2.1.5\configuration_manager.jar
set CP=%CP%;lib\tcs\db_connection_factory\1.0\db_connection_factory.jar
set CP=%CP%;lib\tcs\logging_wrapper\1.2\logging_wrapper.jar
set CP=%CP%;lib\tcs\typesafe_enum\1.0\typesafe_enum.jar

set MAIN=com.topcoder.onlinereview.fixer.OnlineReviewScoreRankFixer
echo "ClassPath: %CP%"
java -classpath %CP% %MAIN% %*