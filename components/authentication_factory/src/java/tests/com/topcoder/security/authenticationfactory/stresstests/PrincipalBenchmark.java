/**
 * Copyright (c)2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.security.authenticationfactory.stresstests;

import junit.framework.TestCase;

import com.topcoder.security.authenticationfactory.Principal;

/**
 * Benchmark tests for the Principal class.
 * 
 * @author adic
 * @version 2.0
 */
public class PrincipalBenchmark extends TestCase {

	/**
     * The timeout for one test.
     */
    private static final int TIMEOUT = 3000;
    
    /**
     * The AuthenticationFactory config namespace.
     */
    private Principal principal = null;

    /**
     * Setup tests.
     *
     * @exception Exception propagate exceptions to JUnit
     */
    public void setUp() throws Exception {
        super.setUp();
        principal = new Principal("abc");
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
     * Benchmark test for the addMapping method.
     * 
     * @throws Exception propagated to JUnit
     */
    public void testAddMapping() throws Exception {                
        long tic = System.currentTimeMillis();
        int count = 0;
        while (System.currentTimeMillis() - tic < TIMEOUT) {
        	principal.addMapping(Integer.toString(count), "abc");
            count++;
        }
        System.out.println(count * 1000 / TIMEOUT + " addMapping calls / second");
        principal.clearMappings();
    }
    
    /**
     * Benchmark test for the removeMapping method.
     * 
     * @throws Exception propagated to JUnit
     */
    public void testRemoveMapping() throws Exception {
        for (int i = 0; i < 30000; i++) {
            principal.addMapping(Integer.toString(i), "abc");
        }
        long tic = System.currentTimeMillis();
        for (int i = 0; i < 30000; i++) {
            principal.removeMapping(Integer.toString(i));
        }
        long tac = System.currentTimeMillis();
        System.out.println(30000 * 1000 / (tac - tic) + " removeMapping calls / second");
        principal.clearMappings();
    }
    
    /**
     * Benchmark test for the containsKey method.
     * 
     * @throws Exception propagated to JUnit
     */
    public void testContainsKey() throws Exception {
        for (int i = 0; i < 1000; i++) {
            principal.addMapping(Integer.toString(i), "abc");
        }
        long tic = System.currentTimeMillis();
        int count = 0;
        while (System.currentTimeMillis() - tic < TIMEOUT) {
        	assertTrue(principal.containsKey(Integer.toString(count % 1000)));
            count++;
        }
        System.out.println(count * 1000 / TIMEOUT + " containsKey calls / second");
        principal.clearMappings();
    }
    
    /**
     * Benchmark test for the getValue method.
     * 
     * @throws Exception propagated to JUnit
     */
    public void testGetValue() throws Exception {
        for (int i = 0; i < 1000; i++) {
            principal.addMapping(Integer.toString(i), "abc");
        }
        long tic = System.currentTimeMillis();
        int count = 0;
        while (System.currentTimeMillis() - tic < TIMEOUT) {
        	assertTrue(principal.getValue(Integer.toString(count % 1000)).equals("abc"));
            count++;
        }
        System.out.println(count * 1000 / TIMEOUT + " getValue calls / second");
        principal.clearMappings();
    }
    
    /**
     * Benchmark test for the getKeys method.
     * 
     * @throws Exception propagated to JUnit
     */
    public void testGetKeys() throws Exception {
        for (int i = 0; i < 1000; i++) {
            principal.addMapping(Integer.toString(i), "abc");
        }
        long tic = System.currentTimeMillis();
        int count = 0;
        while (System.currentTimeMillis() - tic < TIMEOUT) {
        	assertTrue(principal.getKeys() != null);
            count++;
        }
        System.out.println(count * 1000 / TIMEOUT + " getKeys calls / second");
        principal.clearMappings();
    }
    
}
