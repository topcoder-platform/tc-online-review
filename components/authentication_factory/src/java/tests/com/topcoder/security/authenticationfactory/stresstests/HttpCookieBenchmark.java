/**
 * Copyright (c)2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.security.authenticationfactory.stresstests;

import junit.framework.TestCase;

import com.topcoder.security.authenticationfactory.http.HttpCookie;

/**
 * Benchmark tests for the HttpCookie class.
 * 
 * @author adic
 * @version 2.0
 */
public class HttpCookieBenchmark extends TestCase {

	/**
     * The timeout for one test.
     */
    private static final int TIMEOUT = 3000;
    
    /**
     * Setup tests.
     *
     * @exception Exception propagate exceptions to JUnit
     */
    public void setUp() throws Exception {
        super.setUp();
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
     * Benchmark test for the constructor (that invokes the cookie parsing).
     * 
     * @throws Exception propagated to JUnit
     */
    public void testConstructor() throws Exception {                
        long tic = System.currentTimeMillis();
        int count = 0;
        while (System.currentTimeMillis() - tic < TIMEOUT) {
            HttpCookie cookie = new HttpCookie("name=value; Path=/myPath");
            assertTrue(cookie.getName().equals("name"));
            assertTrue(cookie.getValue().equals("value"));
            count++;
        }
        System.out.println(count * 1000 / TIMEOUT + " cookies created / second");
    }
    
}
