#!/bin/bash
#Preparing paackage

#APPNAME="onlinereview"
APPNAME=$1
VER=`date "+%Y%m%d%H%M%S"`
SCRIPTDIR="./deployscripts"


create_cdpacakge()
{
  AWS_CD_PACKAGE_NAME="${APPNAME}-${VER}.zip"
  PACKAGEPATH="dist-cdpack"
  rm -rf $PACKAGEPATH
  mkdir -p $PACKAGEPATH
  cp -Rvf $SCRIPTDIR/* $PACKAGEPATH/
  cp build/ant/online_review/dist/review.war $PACKAGEPATH/
  zip -j $AWS_CD_PACKAGE_NAME $PACKAGEPATH/*
}



create_cdpacakge
echo "export ORAPPVER=\"$VER\"">apppackagever


