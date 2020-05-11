#!/bin/bash

ENV=$1
APPVER=$2
APPNAME=$3

if [ -z $APPVER ] || [ -z $ENV ];
then
 echo "The script need to be executed with version ex:deploy.sh ENV 123"
 exit 1
fi

JQ="jq --raw-output --exit-status"
COUNTER_LIMIT=20


AWS_CD_PACKAGE_NAME="${APPNAME}-${APPVER}.zip"
if [ "$AWS_S3_KEY_LOCATION" = "" ] ;
then
    AWS_S3_KEY="${AWS_CD_PACKAGE_NAME}"
else
    AWS_S3_KEY="$AWS_S3_KEY_LOCATION/${AWS_CD_PACKAGE_NAME}"
fi
DEPLOYID=""
#log Function - Used to provide information of execution information with date and time
log()
{
   echo "`date +'%D %T'` : $1"
}

#track_error function validates whether the application execute without any error
track_error()
{
   if [ $1 != "0" ]; then
        log "$2 exited with error code $1"
        log "completed execution IN ERROR at `date`"
        exit $1
   fi
}

#uploading to S3 bucket
upload_cd_pakcage()
{
    S3_URL=""
	if [ "$AWS_S3_KEY_LOCATION" = "" ] ;
	then
		S3_URL="s3://${AWS_S3_BUCKET}/"
	else
		S3_URL="s3://${AWS_S3_BUCKET}/${AWS_S3_KEY_LOCATION}/"
	fi
	aws s3 cp ${AWS_CD_PACKAGE_NAME} $S3_URL
	track_error $? "Package S3 deployment"
	log "CD Package uploaded successfully to S3 bucket $S3_URL"
}
#register the revision in Code deploy
update_cd_app_revision()
{
	aws deploy register-application-revision --application-name "${AWS_CD_APPNAME}" --s3-location "bucket=${AWS_S3_BUCKET},bundleType=zip,key=${AWS_S3_KEY}"
	track_error $? "CD applicaton register"
	log "CD application register completed successfully"
}
#Invoke the code deploy
cd_deploy()
{
	RESULT=`aws deploy create-deployment --application-name "${AWS_CD_APPNAME}" --deployment-config-name "${AWS_CD_DG_CONFIGURATION}" --deployment-group-name "${AWS_CD_DG_NAME}" --s3-location "bucket=${AWS_S3_BUCKET},bundleType=zip,key=${AWS_S3_KEY}"`
	track_error $? "CD applicaton deployment intiation"
        DEPLOYID=`echo $RESULT | $JQ .deploymentId`
	log "CD application deployment initiation completed successfully. Please find the $DEPLOYID"
}
#Checing the status
cd_deploy_status()
{
	echo "check tatusget info aws deploy get-deployment --deployment-id $DEPLOYID"
        counter=0
        BUFFER=0
        DEPLOYMENT_STATUS=`aws deploy get-deployment --deployment-id "$DEPLOYID" | $JQ .deploymentInfo.status`
        if [ "$DEPLOYMENT_STATUS" = "Succeeded" ] || [ "$DEPLOYMENT_STATUS" = "Failed" ];
        then
           BUFFER=1
        fi
        while [ "$BUFFER" = "0" ]
        do
           echo "Current Deployment status : $DEPLOYMENT_STATUS"
           echo "Waiting for 15 sec to check the Deployment status...."
           sleep 15
           DEPLOYMENT_STATUS=`aws deploy get-deployment --deployment-id "$DEPLOYID" | $JQ .deploymentInfo.status`
           if [ "$DEPLOYMENT_STATUS" = "Succeeded" ] || [ "$DEPLOYMENT_STATUS" = "Failed" ];
           then
             BUFFER=1
           fi
           counter=`expr $counter + 1`
           if [[ $counter -gt $COUNTER_LIMIT ]] ; then
		echo "Deployment does not reach staedy state with in 600 seconds. Please check"
		exit 1
           fi
        done
        if  [[ "$DEPLOYMENT_STATUS" = "Succeeded" ]] ;
        then
          echo "Deployment status is $DEPLOYMENT_STATUS"
        else
          echo "Deployment Failed. Please caheck AWS Code Deploy  event logs"
          exit 1
        fi
	
}
# configure_aws_cli
upload_cd_pakcage
update_cd_app_revision
if [ "$APPDEPLOY" = "1" ] ;
then
    echo "Proceeding deployment"
else
    echo "User skipped deployment by updating the DEPLOY variable other than 1"
    exit 0
fi
cd_deploy
cd_deploy_status


