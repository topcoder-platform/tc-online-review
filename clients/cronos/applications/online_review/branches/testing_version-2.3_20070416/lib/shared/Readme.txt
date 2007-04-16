The files in this directory are here to support navigation links. They are
needed to specify the url of the TC and/or TCS web server locations in these
links. 

They are normally deployed with the TC site, so they aren't intended
to be part of the build process. Instead, if you don't have a copy of the TC
site deployed, you should be able to put shared.jar and 
ApplicationServer.properties into your classpath to make the app work. 

For example, if you are running JBoss and using the "default" server, put
shared.jar in <jboss_home>/server/default/lib and ApplicationServer.properties
in <jboss_home>/server/default/conf.