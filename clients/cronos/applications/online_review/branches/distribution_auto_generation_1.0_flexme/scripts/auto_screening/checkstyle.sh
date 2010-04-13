script_path=$(dirname $0)

/usr/local/java/jdk1.5.0_08/bin/java -classpath $script_path/lib/checkstyle-all-4.2.jar com.puppycrawl.tools.checkstyle.Main -c tc_checks.xml -r $1
exit 0
