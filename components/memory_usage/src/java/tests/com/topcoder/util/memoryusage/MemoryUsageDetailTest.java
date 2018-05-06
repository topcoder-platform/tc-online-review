/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.memoryusage;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests the MemoryUsageDetail class.
 *
 * @author BryanChen
 * @author TexWiller
 * @version 2.0
 */
public class MemoryUsageDetailTest extends TestCase {

    /**
     * Standard TestCase constructor: creates a new MemoryUsageDetailTest object.
     *
     * @param testName The name to be given to this TestCase.
     */
    public MemoryUsageDetailTest(String testName) {
        super(testName);
    }

    /**
     * Static method expected by JUnit test runners. Returns a Test containing all
     * the testXXX methods in this class.
     *
     * @return A new Test, containing all the testXXX methods in this class.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(MemoryUsageDetailTest.class);
        return suite;
    }

    /**
     * Test of the constructor with <code>null</code> class argument.
     * Expected exception: IllegalArgumentException.
     */
    public void testConstructorErr() {
        try {
            new MemoryUsageDetail(null);
            fail("MemoryUsageDetail constructor accepted null parameter");
        } catch (IllegalArgumentException ex) {
            // Test passed
        }
    }

    /**
     * Tests constructor and getDetailClass().
     */
    public void testGetDetailClass() {
        MemoryUsageDetail detail = new MemoryUsageDetail(String.class);
        assertEquals(String.class, detail.getDetailClass());

        detail = new MemoryUsageDetail(Integer.class);
        assertEquals(Integer.class, detail.getDetailClass());

        detail = new MemoryUsageDetail(MemoryUsageDetail.class);
        assertEquals(MemoryUsageDetail.class, detail.getDetailClass());
    }

    /**
     * Tests getObjectCount() and cumulate().
     */
    public void testDetailGetObjectCount() {
        MemoryUsageDetail detail = new MemoryUsageDetail(String.class);
        assertEquals("incorrect object count", 0, detail.getObjectCount());

        detail.cumulate(1, 4);
        assertEquals("incorrect object count", 1, detail.getObjectCount());

        detail.cumulate(23, 3);
        assertEquals("incorrect object count", 24, detail.getObjectCount());

        detail.cumulate(2, 23);
        assertEquals("incorrect object count", 26, detail.getObjectCount());
    }

    /**
     * Tests cumulate() properly throws IllegalArgumentException
     * on negative or zero values.
     * Expected exception: IllegalArgumentException.
     */
    public void testCumulateErr() {
        MemoryUsageDetail detail = new MemoryUsageDetail(String.class);
        try {
            detail.cumulate(-26, 254);
            fail("cumulate() allowed negative object count");
        } catch (IllegalArgumentException ex) {
            // Test passed
        }

        try {
            detail.cumulate(6, -2);
            fail("cumulate() allowed negative memory size");
        } catch (IllegalArgumentException ex) {
            // Test passed
        }

        try {
            detail.cumulate(0, 25);
            fail("cumulate() allowed zero object count");
        } catch (IllegalArgumentException ex) {
            // Test passed
        }

        try {
            detail.cumulate(6, 0);
            fail("cumulate() allowed zero memory size");
        } catch (IllegalArgumentException ex) {
            // Test passed
        }
    }


    /**
     * Tests getUsedMemory.
     */
    public void testDetailGetUsedMemory() {
        MemoryUsageDetail detail = new MemoryUsageDetail(Character.class);
        assertEquals("incorrect memory count", 0, detail.getUsedMemory());

        detail.cumulate(1, 24);
        assertEquals("incorrect memory count", 24, detail.getUsedMemory());

        detail.cumulate(6, 109);
        assertEquals("incorrect memory count", 133, detail.getUsedMemory());

        detail.cumulate(114, 67);
        assertEquals("incorrect memory count", 200, detail.getUsedMemory());
    }

}
