/**
 * Copyright (c)2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.security.authenticationfactory.stresstests;

import junit.framework.TestCase;

import com.topcoder.security.authenticationfactory.Authenticator;
import com.topcoder.security.authenticationfactory.DefaultKeyConverter;
import com.topcoder.security.authenticationfactory.Principal;
import com.topcoder.security.authenticationfactory.Response;
import com.topcoder.security.authenticationfactory.http.basicimpl.HTTPBasicAuthenticator;
import com.topcoder.security.authenticationfactory.http.basicimpl.HttpResourceImpl;
import com.topcoder.util.config.ConfigManager;

/**
 * Benchmark tests for the HTTPBasicAuthenticator class.
 * 
 * @author adic
 * @version 2.0
 */
public class HTTPBasicAuthenticatorBenchmark extends TestCase {

	/**
     * The timeout for one test.
     */
    private static final int TIMEOUT = 3000;
    
    /**
     * The HTTPBasicAuthenticator config namespace.
     */
    private static final String NAMESPACE = HTTPBasicAuthenticator.class.getName();
    
    /**
     * The DefaultKeyConverter config namespace.
     */
    private static final String NAMESPACE2 = DefaultKeyConverter.class.getName();
    
    /**
     * The HTTPBasicAuthenticatorBenchmark config namespace.
     */
    private static final String NAMESPACE3 = "com.topcoder.security.authenticationfactory.stresstests";
    
    /**
     * The HTTP authenticator instance.
     */
    private Authenticator authenticator = null;

    /**
     * Setup tests.
     *
     * @exception Exception propagate exceptions to JUnit
     */
    public void setUp() throws Exception {
        super.setUp();
        ConfigManager cm = ConfigManager.getInstance();
        if (cm.existsNamespace(NAMESPACE)) {
        	cm.removeNamespace(NAMESPACE);
        }
        cm.add(NAMESPACE, "stress3.xml", ConfigManager.CONFIG_MULTIPLE_XML_FORMAT);
        if (cm.existsNamespace(NAMESPACE2)) {
        	cm.removeNamespace(NAMESPACE2);
        }
        cm.add(NAMESPACE2, "stress3.xml", ConfigManager.CONFIG_MULTIPLE_XML_FORMAT);
        authenticator = new HTTPBasicAuthenticator(NAMESPACE);
        
        if (cm.existsNamespace(NAMESPACE3)) {
        	cm.removeNamespace(NAMESPACE3);
        }
        cm.add(NAMESPACE3, "stress_params.xml", ConfigManager.CONFIG_MULTIPLE_XML_FORMAT);
    }
    
    /**
     * Cleanup.
     *
     * @exception Exception propagate exceptions to JUnit
     */
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Benchmark test for the authentication.
     * 
     * @throws Exception propagated to JUnit
     */
    public void testAuthenticate() throws Exception {                
        long tic = System.currentTimeMillis();
        int count = 0;
        while (System.currentTimeMillis() - tic < TIMEOUT) {
            Principal user = new Principal("id");
            user.addMapping(HTTPBasicAuthenticator.PROTOCOL_KEY, getProp("protocol"));
            user.addMapping(HTTPBasicAuthenticator.HOST_KEY, getProp("host"));
            user.addMapping(HTTPBasicAuthenticator.PORT_KEY, new Integer(getProp("port")));
            user.addMapping(HTTPBasicAuthenticator.FILE_KEY, getProp("path"));
            user.addMapping(HTTPBasicAuthenticator.USER_NAME_KEY, "user");
            user.addMapping(HTTPBasicAuthenticator.PASSWORD_KEY, "pass".toCharArray());

            Response response = authenticator.authenticate(user);
            assertTrue(response.isSuccessful());
            HttpResourceImpl resource = (HttpResourceImpl) response.getDetails();
            assertTrue(resource.getHttpConnection().getResponseCode() == 200);
            assertEquals("http://localhost:8080/webapp1/index.html", resource.getOriginalURL());
            count++;
        }
        System.out.println(count * 1000 / TIMEOUT + " non-cached authentications / second");
    }
    
    private String getProp(String prop) throws Exception {
        return (String) ConfigManager.getInstance().getProperty(NAMESPACE3, prop);
    }
    
}
