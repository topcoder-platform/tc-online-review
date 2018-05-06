Instructions:

- To run the test, j2ee.jar, fscontext.jar and providerutil.jar need to be added into build path

- Some JDNIUtils methods require default jndi factory, you can add <sysproperty key="java.naming.factory.initial" value="com.sun.jndi.fscontext.RefFSContextFactory" /> under <junt ...> under <target name="test" ...> in build.xml


