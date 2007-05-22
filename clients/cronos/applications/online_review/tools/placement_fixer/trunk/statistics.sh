#!/bin/bash
CP=""
for i in $(find lib -name \*jar); do
	CP="$CP:$i"
done
CP="$CP:build/lib/OR_score_fixer.jar"
CP="conf$CP"
MAIN="com.topcoder.onlinereview.fixer.OnlineReviewScoreRankFixer"

echo "classpath: $CP"
java -classpath $CP $MAIN > report.txt
