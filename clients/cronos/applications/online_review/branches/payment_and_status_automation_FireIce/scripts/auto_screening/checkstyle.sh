script_path=$(dirname $0)

@jdk_home@/bin/java -classpath $script_path/lib/checkstyle-all-4.2.jar com.puppycrawl.tools.checkstyle.Main -c tc_checks.xml -r $1
exit 0
