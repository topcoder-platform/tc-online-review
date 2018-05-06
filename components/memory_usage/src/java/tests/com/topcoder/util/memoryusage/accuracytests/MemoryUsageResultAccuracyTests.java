/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 *
 * TCS Memory Usage Version 2.0 Accuracytests.
 *
 * @ MemoryUsageResultAccuracyTests.java
 */
package com.topcoder.util.memoryusage.accuracytests;

import com.topcoder.util.memoryusage.MemoryUsageDetail;
import com.topcoder.util.memoryusage.MemoryUsageResult;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.HashMap;
import java.util.Map;


/**
 * <p>
 * The <code>MemoryUsageResult</code>'s Accuracy Tests.
 * This accuracy tests addresses the functionality provided
 * by the <code>MemoryUsageResult</code> class.
 * </p>
 *
 * @author zmg
 * @version 2.0
 */
public class MemoryUsageResultAccuracyTests extends TestCase {
    /**
     * <p>
     * The instance of <code>MemoryUsageResult</code> used for tests.
     * </p>
     */
    private MemoryUsageResult test = new MemoryUsageResult();

    /**
     * <p>
     * The instance of <code>MemoryUsageDetail</code> used for tests.
     * </p>
     */
    private MemoryUsageDetail detail1 = new MemoryUsageDetail(String.class);

    /**
     * <p>
     * The instance of <code>MemoryUsageDetail</code> used for tests.
     * </p>
     */
    private MemoryUsageDetail detail2 = new MemoryUsageDetail(Byte.class);

    /**
     * <p>
     * The instance of <code>HashMap</code> used for tests.
     * </p>
     */
    private Map map;

    /**
     * <p>
     * Test suite of <code>MemoryUsageResultAccuracyTests</code>.
     * </p>
     *
     * @return Test suite of <code>MemoryUsageResultAccuracyTests</code>.
     */
    public static Test suite() {
        return new TestSuite(MemoryUsageResultAccuracyTests.class);
    }

    /**
     * <p>
     * Initialize the instances for accuracy tests.
     * </p>
     */
    public void setUp() {
        map = new HashMap();
        map.put(String.class, detail1);
        map.put(Byte.class, detail2);
    }

    /**
    * <p>
    * Accuracy Test of the <code>setDetails(Map)</code> constructor.
    * </p>
    */
    public void testsetDetails() {
        // check the constructor
        assertNotNull("should not null", test);
        test.setDetails(map);

        // get the all MemoryUsageDetail in the map.
        assertEquals("length should be equal", 2, test.getDetails().length);

        // get the MemoryUsageDetail by it's class.
        assertEquals("MemoryUsageDetail should be equal", detail1,
            test.getDetail(String.class));
        assertEquals("MemoryUsageDetail should be equal", detail2,
            test.getDetail(Byte.class));

        // get the MemoryUsageDetail which is not exist.
        assertNull("should be null", test.getDetail(Boolean.class));
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
