/**
 * Copyright © 2004, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.generator.guid;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * Test the IDGeneratorAdapter class.
 * </p>
 * <p>
 * It will test:
 * </p>
 * <ul>
 * <li>the getIDName method, it should return a fixed class name</li>
 * <li>the testGetNextID method to see if it returns different long values</li>
 * </ul>
 * 
 * @author TCSDEVELOPER
 * @author TCSDESIGNER
 * @version 1.0
 */
public class IDGeneratorAdapterTests extends TestCase {

    /**
     * Instance of IDGeneratorAdapter used to test the class.
     */
    private static IDGeneratorAdapter idGen = new IDGeneratorAdapter();

    /**
     * return a suite of tests.
     * 
     * @return a suite of tests
     */
    public static Test suite() {
        return new TestSuite(IDGeneratorAdapterTests.class);
    }

    /**
     * Test that getIDName is returning the expected class name (com.topcoder.util.generator.guid.IDGeneratorAdapter).
     *  
     */
    public void testGetIDName() {
        assertEquals("bad ID Name", "com.topcoder.util.generator.guid.IDGeneratorAdapter", idGen.getIDName());
    }


    /**
     * Test that the getNextID is returning different elements.
     *  
     */
    public void testGetNextID() {
        Set set = new HashSet();
        int repeated = 0;
        for (int i = 0; i < 100000; i++) {
            if (!set.add(new Long(idGen.getNextID()))) {
                repeated++;
            }
        }
        assertEquals("There are some elements repeated. However, it is not impossible that this happens."
                + "Please run the tests again.", 0, repeated);

     }
}