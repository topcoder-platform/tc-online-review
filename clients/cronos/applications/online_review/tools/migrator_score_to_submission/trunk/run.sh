#!/bin/bash
CP=""
for i in $(find lib -name \*jar); do
	CP="$CP:$i"
done
CP="$CP:build/lib/migrator_score_to_submission.jar"
CP="conf$CP"
MAIN="com.topcoder.onlinereview.migration.ORMigration"

echo "classpath: $CP"
java -classpath $CP $MAIN $@
