FROM openjdk:8u332-jdk-slim

RUN apt-get update && apt-get install wget -y
RUN cd /root/ \
       && wget https://github.com/wildfly/wildfly/releases/download/26.0.1.Final/wildfly-26.0.1.Final.tar.gz \
       && tar xzf wildfly-26.0.1.Final.tar.gz \
       && rm -rf wildfly-26.0.1.Final.tar.gz

ENV JAVA_OPTS="-Xms1G -Xmx1G -XX:MaxPermSize=256M -server"
ENV JBOSS_HOME=/root/wildfly-26.0.1.Final
ENV PATH=$PATH:/root/wildfly-26.0.1.Final/bin
ENV TZ=America/Indiana/Indianapolis

Add ./local/Docker_files/ifxjdbc.jar /root/wildfly-26.0.1.Final/standalone/deployments/
Add ./web/i /root/wildfly-26.0.1.Final/welcome-content/i
Add ./web/css /root/wildfly-26.0.1.Final/welcome-content/css
Add ./web/js /root/wildfly-26.0.1.Final/welcome-content/js

Add ./target/review /root/wildfly-26.0.1.Final/standalone/deployments/review.war
RUN touch /root/wildfly-26.0.1.Final/standalone/deployments/review.war.dodeploy

RUN mkdir -p /root/web/conf
Add ./conf/distribution_scripts /root/web/conf/distribution_scripts
RUN mkdir -p /root/temp/dist-gen
RUN mkdir -p /nfs_shares/tcssubmissions
RUN mkdir -p /nfs_shares/studiofiles/submissions
RUN mkdir -p /nfs_shares/tcs-downloads

## tokenized
Add ./jboss_files/deploy/tcs_informix-ds.xml /root/
Add ./token.properties.local /root/token.properties
RUN cat /root/token.properties | grep -v '^#' | grep -v '^$'| sed s/\\//\\\\\\//g | awk -F '=' '{print "s/@"$1"@/"$2"/g"}' | sed -f /dev/stdin /root/tcs_informix-ds.xml >> /root/wildfly-26.0.1.Final/standalone/deployments/informix-ds.xml
RUN rm /root/token.properties
RUN rm /root/tcs_informix-ds.xml

## add admin account
RUN /root/wildfly-26.0.1.Final/bin/add-user.sh -u 'admin' -p 'password1!'
RUN sed -i 's/<cached-connection-manager\/>/<cached-connection-manager debug="true" error="false"\/>/' /root/wildfly-26.0.1.Final/standalone/configuration/standalone.xml

CMD ["/root/wildfly-26.0.1.Final/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0","-DFOREGROUND"]