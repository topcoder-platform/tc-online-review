FROM maven:3.6.1-jdk-7

RUN cd /root/ \
       && wget http://downloads.sourceforge.net/project/jboss/JBoss/JBoss-4.0.2/jboss-4.0.2.tar.gz \
       && tar xzf jboss-4.0.2.tar.gz \
       && rm -rf jboss-4.0.2.tar.gz

ENV JBOSS_HOME=/root/jboss-4.0.2
ENV PATH=$PATH:/root/jboss-4.0.2/bin

Add ./Docker_files/ifxjdbc.jar /root/jboss-4.0.2/server/default/lib/
Add ./Docker_files/informix-ds.xml /root/jboss-4.0.2/server/default/deploy/
Add ./web/i /root/jboss-4.0.2/server/default/deploy/jbossweb-tomcat55.sar/ROOT.war/i
Add ./web/css /root/jboss-4.0.2/server/default/deploy/jbossweb-tomcat55.sar/ROOT.war/css
Add ./web/js /root/jboss-4.0.2/server/default/deploy/jbossweb-tomcat55.sar/ROOT.war/js

Add ./target/review /root/jboss-4.0.2/server/default/deploy/review.war
Add ./Docker_files/OnlineReview.xml /root/jboss-4.0.2/server/default/deploy/review.war/WEB-INF/classes/

CMD ["/root/jboss-4.0.2/bin/run.sh","-DFOREGROUND"]