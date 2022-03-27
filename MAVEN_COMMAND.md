## Build war file

  - run command `mvn clean package`

## Deploy to JBoss

1. build war dir `mvn clean package`
2. copy `jboss_files/lib/ifxjdbc.jar` to `JBOSS_BASE/server/default/lib` this lib is for creating JNDI DataSources
3. add `jboss_files/deploy/tcs_informix-ds.xml`(update the db host, user etc.) to `JBOSS_BASE/server/default/deploy` to create JNDI DataSources.
4. copy `web/(css|i|js)` to `JBOSS_BASE/server/default/deploy/jbossweb-tomcat55.sar/ROOT.war` for static files
5. copy `target/review` to `JBOSS_BASE/server/default/deploy/review.war`

## Start JBoss

1. navigate to `JBOSS_BASE/bin` and then run `./run.sh` (you should run in this folder if you don't update `exttemplatesdir` value in token.properties)

## Run in docker

1. build war dir `mvn clean package`
2. run `docker-compose up -d` in root dir.

## Note

You need comment `login` and `cache` function when deploying locally.

- comment src/main/java/com/cronos/onlinereview/util/AuthorizationHelper.java line145-151 and then `return 132456L;`
- comment web/includes/inc_header.jsp line22
- comment web/includes/project/project_phase.jsp and web/includes/project/project_resource.jsp all `<tc-webtag:handle` tag