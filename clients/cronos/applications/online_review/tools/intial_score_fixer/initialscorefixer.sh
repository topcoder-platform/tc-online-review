#!/bin/bash

CP="build/classes:conf"
for i in $(find dist -name \*jar); do
	CP="$CP:$i"
done

MAIN_CLASS=com.topcoder.management.review.scorefixer.InitialScoreFixer

java -cp $CP $MAIN_CLASS $*
