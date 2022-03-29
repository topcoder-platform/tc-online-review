#! /bin/bash
set -e

mv .deploy/circleci/build.properties .
mv .deploy/circleci/token.properties .
#add svn user name and password to build.properies
echo "build.svn.username=$SVN_USERNAME" >> build.properties
echo "build.svn.password=$SVN_PASSWORD" >> build.properties
mvn clean package

