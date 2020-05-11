#!/bin/bash
CDATE=`date '+%Y-%m-%d-%H%M%S'`
BACKUP_DIR=/home/apps/or_backup/$CDATE
DEPLOY_SRCPACKAGE=/home/apps/deploy_or/review.war
JBIN=/home/apps/jboss-4.0.2/bin
JBDEPLOY=/home/apps/jboss-4.0.2/server/default/deploy
JBDEPLOYAPP=$JBDEPLOY/review.war
mkdir -p $BACKUP_DIR
cp -Rvf $DEPLOY_SRCPACKAGE $BACKUP_DIR/
cp -Rvf $JBDEPLOYAPP $BACKUP_DIR/
rm -rvf $DEPLOY_SRCPACKAGE
rm -rvf $JBDEPLOYAPP