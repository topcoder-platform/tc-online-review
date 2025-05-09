FROM maven:3.6.1-jdk-8

RUN cd /root/ \
       && wget https://github.com/wildfly/wildfly/releases/download/26.1.2.Final/wildfly-26.1.2.Final.tar.gz \
       && tar xzf wildfly-26.1.2.Final.tar.gz \
       && rm -rf wildfly-26.1.2.Final.tar.gz

ENV JAVA_OPTS="-Xms4G -Xmx4G -XX:MaxPermSize=512M -server"
ENV JBOSS_HOME=/root/wildfly-26.1.2.Final
ENV PATH=$PATH:/root/wildfly-26.1.2.Final/bin
ENV TZ=America/Indiana/Indianapolis

Add ./web/i /root/wildfly-26.1.2.Final/welcome-content/i
Add ./web/css /root/wildfly-26.1.2.Final/welcome-content/css
Add ./web/js /root/wildfly-26.1.2.Final/welcome-content/js

Add ./target/review /root/wildfly-26.1.2.Final/standalone/deployments/review.war
RUN touch /root/wildfly-26.1.2.Final/standalone/deployments/review.war.dodeploy

RUN mkdir -p /root/web/conf
Add ./conf/distribution_scripts /root/web/conf/distribution_scripts
RUN mkdir -p /root/temp/dist-gen
RUN mkdir -p /nfs_shares/tcssubmissions
RUN mkdir -p /nfs_shares/studiofiles/submissions
RUN mkdir -p /nfs_shares/tcs-downloads

## tokenized
Add ./token.properties.local /root/token.properties
RUN cat /root/token.properties | grep -v '^#' | grep -v '^$'| sed s/\\//\\\\\\//g | awk -F '=' '{print "s/@"$1"@/"$2"/g"}'
RUN rm /root/token.properties

## add admin account
RUN /root/wildfly-26.1.2.Final/bin/add-user.sh -u 'admin' -p 'password1!'
RUN sed -i 's/<http-listener name="default" socket-binding="http" redirect-socket="https" enable-http2="true"\/>/<http-listener name="default" socket-binding="http" redirect-socket="https" enable-http2="true" max-parameters="5000"\/>/' /root/wildfly-26.1.2.Final/standalone/configuration/standalone.xml

CMD ["/root/wildfly-26.1.2.Final/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0","-DFOREGROUND"]