/*
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.cache;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.topcoder.util.memoryusage.MemoryUsage;
import com.topcoder.util.memoryusage.MemoryUsageResult;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Provides unit test cases for SimpleMemoryUtilizationHandler class.
 *
 * @author  rem
 * @version 2.0
 * @since   2.0
 */
public class SimpleMemoryUtilizationHandlerTestCase extends TestCase {

    /**
     * Instance for testing.
     */
    private SimpleMemoryUtilizationHandler muh = new SimpleMemoryUtilizationHandler();

    /**
     * Returns the test suite for this class.
     *
     * @return a new TestSuite representing this class.
     */
    public static Test suite() {
        return new TestSuite(SimpleMemoryUtilizationHandlerTestCase.class);
    }

    /**
     * Tests method getObjectSize.
     *
     * <ul>
     * <li>Make sure NullPointerException is thrown when argument is null.
     * <li>Check this method with correct argument. Uses Memory Usage component to
     *     determine size of object and compare it with size returned by
     *     SimpleMemoryUtilization handler.
     * </ul>
     *
     * @throws Exception if any error occurs.
     */
    public void testGetObjectSize() throws Exception {
        try {
            muh.getObjectSize(null);
            fail("getObjectSize(null) should throw NullPointerException.");
        } catch (NullPointerException ex) {
            // expected exception.
        }

        Object obj = new Object[] {new ArrayList(), new HashSet()};
        MemoryUsage memoryUsage = new MemoryUsage();
        MemoryUsageResult mur = memoryUsage.getDeepMemoryUsage(obj);
        long mustBe = mur.getUsedMemory();
        assertEquals("getObjectSize should return valid result.", muh.getObjectSize(obj), mustBe);
    }
}
