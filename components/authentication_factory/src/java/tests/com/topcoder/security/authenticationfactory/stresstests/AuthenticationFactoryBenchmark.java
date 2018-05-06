/**
 * Copyright (c)2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.security.authenticationfactory.stresstests;

import junit.framework.TestCase;

import com.topcoder.security.authenticationfactory.AuthenticationFactory;
import com.topcoder.security.authenticationfactory.Authenticator;
import com.topcoder.security.authenticationfactory.DefaultKeyConverter;
import com.topcoder.util.config.ConfigManager;

/**
 * Benchmark tests for the AuthenticationFactory class.
 * 
 * @author adic
 * @version 2.0
 */
public class AuthenticationFactoryBenchmark extends TestCase {

	/**
     * The timeout for one test.
     */
    private static final int TIMEOUT = 3000;
    
    /**
     * The AuthenticationFactory config namespace.
     */
    private static final String NAMESPACE = AuthenticationFactory.class.getName();
    
    /**
     * The DefaultKeyConverter config namespace.
     */
    private static final String NAMESPACE2 = DefaultKeyConverter.class.getName();

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
        cm.add(NAMESPACE, "stress1.xml", ConfigManager.CONFIG_MULTIPLE_XML_FORMAT);
        if (cm.existsNamespace(NAMESPACE2)) {
        	cm.removeNamespace(NAMESPACE2);
        }
        cm.add(NAMESPACE2, "stress1.xml", ConfigManager.CONFIG_MULTIPLE_XML_FORMAT);
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
     * Benchmark test for the getAuthenticator method.
     * 
     * @throws Exception propagated to JUnit
     */
    public void t2estGetAuthenticator() throws Exception {                
        long tic = System.currentTimeMillis();
        int count = 0;
        while (System.currentTimeMillis() - tic < TIMEOUT) {
        	char idx = (char) (48 + Math.random() * 15);
        	Authenticator auth = AuthenticationFactory.getInstance().getAuthenticator("a" + idx);        	
            assertTrue(idx < 58 ^ auth == null);
            count++;
        }
        System.out.println(count * 1000 / TIMEOUT + " getAuthenticator calls / second");
    }
    
    /**
     * Benchmark test for the refresh method.
     * 
     * @throws Exception propagated to JUnit
     */
    public void testRefresh() throws Exception {                
        long tic = System.currentTimeMillis();
        int count = 0;
        while (System.currentTimeMillis() - tic < TIMEOUT) {        	
        	AuthenticationFactory.getInstance().refresh();
            count++;
        }
        System.out.println(count * 1000.0 / TIMEOUT + " refresh calls / second");
    }
    
}
