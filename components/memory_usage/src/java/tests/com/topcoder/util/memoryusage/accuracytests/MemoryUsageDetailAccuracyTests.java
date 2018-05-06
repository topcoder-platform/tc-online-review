/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 *
 * TCS Memory Usage Version 2.0 Accuracytests.
 *
 * @ MemoryUsageDetailAccuracyTests.java
 */
package com.topcoder.util.memoryusage.accuracytests;

import com.topcoder.util.memoryusage.MemoryUsageDetail;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * The <code>MemoryUsageDetail</code>'s Accuracy Tests.
 * This accuracy tests addresses the functionality provided
 * by the <code>MemoryUsageDetail</code> class.
 * </p>
 *
 * @author zmg
 * @version 2.0
 */
public class MemoryUsageDetailAccuracyTests extends TestCase {
    /**
     * <p>
     * The instance of <code>MemoryUsageDetail</code> used for tests.
     * </p>
     */
    private MemoryUsageDetail test = new MemoryUsageDetail(String.class);

    /**
     * <p>
     * Test suite of <code>MemoryUsageDetailAccuracyTests</code>.
     * </p>
     *
     * @return Test suite of <code>MemoryUsageDetailAccuracyTests</code>.
     */
    public static Test suite() {
        return new TestSuite(MemoryUsageDetailAccuracyTests.class);
    }

    /**
    * <p>
    * Accuracy Test of the <code>MemoryUsageDetail(class)</code> constructor.
    * </p>
    */
    public void testConstructor() {
        // get the class to check the constructor
        assertEquals("class type should be equal", String.class,
            test.getDetailClass());
        test = new MemoryUsageDetail(MemoryUsageDetail.class);
        assertEquals("class type should be equal", MemoryUsageDetail.class,
            test.getDetailClass());
    }

    /**
     * <p>
     * Accuracy Test of the <code>cumulate(int, long)</code> method.
     * </p>
     */
    public void testgetDetailClass() {
        // get the object count and used memory to check cumulate method.
        assertEquals("object count should be equal", 0, test.getObjectCount());
        assertEquals("used memory should be equal", 0L, test.getUsedMemory());

        for (int i = 0; i < 100; i++) {
            test.cumulate(5, 10L);
        }

        // get the object count and used memory to check cumulate method.
        assertEquals("object count should be equal", 500, test.getObjectCount());
        assertEquals("used memory should be equal", 1000L, test.getUsedMemory());
    }
}
