Online Review Login Demo README:

1. JBoss should be used to run this demo.
   Otherwise, you may need to change some configurations.
   For JBoss, you can simply copy this folder to deploy directory.

2. Take care of the files under WEB-INF/classes.
   They are configuration files for ConfigManager.

3. In order to keep the submission small, 
   all the jars under WEB-INF\lib are removed.
   You should put them back to this folder,
   or configure WEB-INF/web.xml to let them in CLASSPATH.
   Here is a list of their names:

-- Third party packages.
-- All of them can be found in struts or jboss distributions.
antlr.jar
commons-beanutils.jar
commons-digester.jar
commons-fileupload.jar
commons-logging.jar
commons-validator.jar
jakarta-oro.jar
struts.jar

-- TCS components
authentication_factory.jar
base_exception.jar
configuration_manager.jar
JNDI_Utility.jar
logging_wrapper.jar
security_manager.jar
typesafe_enum.jar
tcsUtil.jar 
configuration_api.jar
simple_cache.jar

       --NOTE: This component isn't downloadable. It is extracted from security_manager.ear.
                           You can also find it in test_files/ directory.