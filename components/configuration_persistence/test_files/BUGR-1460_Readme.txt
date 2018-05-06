Changes from BUGR-1460

* XmlFilePersistence.loadFile(), PropertyFilePersistence.loadFile()
  These methods were updated to support reading configuration from files
  inside archives (JAR / WAR / EAR) in the component's classpath.

* ConfigurationPersistence 
  Javadocs for this interface were updated to document the revised behavior
  of the two provided concrete implementations.

* Helper
  Two Helper methods were added.

* ConfigurationFileManager
  Made minor internal changes in support of the new feature.

* Tests
  Tests were added to exercise the new feature.

* test_reflib/resource.jar
  This file contains configuration files read by the new tests.  It must be in
  the component's runtime classpath for the new tests to pass.

