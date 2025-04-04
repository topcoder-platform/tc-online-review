#!/usr/bin/env bash
set -eo pipefail

ECS_TAG="latest"

#ENV_CONFIG=`echo "$ENV" | tr '[:upper:]' '[:lower:]'`


# configure_aws_cli
TAG=$AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$AWS_REPOSITORY:$CIRCLE_BUILD_NUM
docker tag $APPNAME:$ECS_TAG $TAG
eval $(aws ecr get-login --region $AWS_REGION --no-include-email)
docker push $TAG


ecs-cli configure --region us-east-1 --cluster $AWS_ECS_CLUSTER
ecs-cli compose --project-name $AWS_ECS_SERVICE service up --launch-type FARGATE


