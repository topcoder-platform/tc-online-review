#!/bin/bash
CDATE=`date '+%Y-%m-%d-%H%M%S'`
BACKUP_DIR=/home/apps/or_backup/$CDATE
DEPLOY_SRCPACKAGE=/home/apps/deploy_or/review.war
JBIN=/home/apps/jboss-4.0.2/bin
JBDEPLOY=/home/apps/jboss-4.0.2/server/default/deploy
JBDEPLOYAPP=$JBDEPLOY/review.war
mkdir -p $JBDEPLOYAPP
cd $JBDEPLOYAPP
/usr/local/java/jdk1.7.0_95/bin/jar -xf $DEPLOY_SRCPACKAGE
