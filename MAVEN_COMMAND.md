## Build war file

  - run command `mvn clean package`

## Deploy to JBoss

1. build war dir `mvn clean package`
2. copy `web/(css|i|js)` to `JBOSS_BASE/server/default/deploy/jbossweb-tomcat55.sar/ROOT.war` for static files
3. copy `target/review` to `JBOSS_BASE/server/default/deploy/review.war`

## Start JBoss

1. navigate to `JBOSS_BASE/bin` and then run `./run.sh` (you should run in this folder if you don't update `exttemplatesdir` value in token.properties)

## Run in docker

1. replace `token.properties` to `token.properties.local` on pom.xml line 894
2. add `127.0.0.1       local.topcoder-dev.com` to your hosts
3. build war dir `mvn clean package`
4. run `docker-compose up -d` in root dir.
5. open `https://topcoder-dev.com/login` and use `dok/appirio123` to login.
6. open `http://local.topcoder-dev.com:8080/review`

## Build docker image for dev and prod

> before build this image, you should make sure you've set the token.properties corresponding to the environment
> if you use docker container to run this project, you should use `host.docker.internal` to connect the host machine.
 
1. build war dir `mvn clean package`
2. run `docker build -t tc-online-review:dev -f ECSDockerfile --platform=linux/amd64 .` in root dir.
3. run `docker-compose -f docker-compose-dev.yml up -d` in root dir to start docker container.
