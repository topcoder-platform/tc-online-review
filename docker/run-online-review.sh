#! /bin/bash

set -e
set -x

echo "informixoltp_tcp        onsoctcp        $SERVERNAME               sqlexec" > /opt/ibm/informix/etc/sqlhosts.informixoltp_tcp
echo "sqlexec $SERVERPORT/tcp" >> /etc/services

mkdir -p /nfs_shares/studiofiles
mkdir -p /nfs_shares/tcssubmissions
mkdir -p /nfs_shares/tcs-downloads

if [ "x${NFS_IP}" != "x"  ]; then
  mount ${NFS_IP}:/nfs_shares/studiofiles /nfs_shares/studiofiles
  mount ${NFS_IP}:/nfs_shares/tcssubmissions /nfs_shares/tcssubmissions
  mount ${NFS_IP}:/nfs_shares/tcs-downloads /nfs_shares/tcs-downloads
fi

if [ "x${JAVA_OPTS}" = "x" ]; then
  export JAVA_OPTS="-Xms256m -Xmx512m"
fi

rm -rf ${JBOSS_HOME}/server/default

set +x
s3cmd --access_key=${ACCESS_KEY} --secret_key=${SECRET_KEY} --region=${REGION} \
 --progress --stats --verbose get s3://${BUCKET}/default.zip /tmp/default.zip
set -x

cd ${JBOSS_HOME}/server
unzip -qq /tmp/default.zip && rm -f /tmp/default.zip

set +x
s3cmd --access_key=${ACCESS_KEY} --secret_key=${SECRET_KEY} --region=${REGION} \
 --progress --stats --verbose sync s3://${BUCKET}/online_review /tmp
set -x

cd ${JBOSS_HOME}/server/default/deploy
cp -f /tmp/online_review/jboss_files/deploy/tcs_informix-ds.xml informix-ds.xml
cp -f /tmp/online_review/review.war .
cp -Rf /tmp/online_review/jboss_files/deploy/static.ear .

rm -rf /tmp/online_review

cd ${JBOSS_HOME}
bin/run.sh -b 0.0.0.0 -DFOREGROUND