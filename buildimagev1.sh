#!/bin/bash
set -eo pipefail
APP_NAME=$1
UPDATE_CACHE=""
docker build -f ECSDockerfile -t $APP_NAME:latest .

echo "DB_SERVER=$DB_SERVER" >>api.env
echo "DB_PORT=$DB_PORT" >>api.env
echo "DB_INFORMIXSERVER=$DB_INFORMIXSERVER" >>api.env
echo "DB_USERNAME=$DB_USERNAME" >>api.env
echo "DB_PASSWORD=$DB_PASSWORD" >>api.env
echo "DW_SERVER=$DW_SERVER" >>api.env
echo "DW_PORT=$DW_PORT" >>api.env
echo "DW_INFORMIXSERVER=$DW_INFORMIXSERVER" >>api.env
echo "DW_USERNAME=$DW_USERNAME" >>api.env
echo "DW_PASSWORD=$DW_PASSWORD" >>api.env
echo "DB_DS1=$DB_DS1" >>api.env
echo "DB_DS2=$DB_DS2" >>api.env
echo "DB_DS3=$DB_DS3" >>api.env
echo "DB_DS4=$DB_DS4" >>api.env
echo "GRPC_PORT=$GRPC_PORT" >>api.en
echo "GRPC_IMAGE=$GRPC_IMAGE" >>api.env

TAG=$AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$AWS_REPOSITORY:$CIRCLE_BUILD_NUM

sed -i='' "s|tc-online-review:latest|$TAG|" docker-compose.yml
sed -i='' "s|tc-or-grpc-server:latest|$GRPC_IMAGE|" docker-compose.yml

#docker-compose -f docker/docker-compose-grpc.yml build