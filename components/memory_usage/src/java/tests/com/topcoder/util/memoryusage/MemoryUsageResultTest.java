/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */


package com.topcoder.util.memoryusage;

import java.util.HashMap;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests the MemoryUsageResults class.
 *
 * @author BryanChen
 * @author TexWiller
 * @version 2.0
 */
public class MemoryUsageResultTest extends TestCase {

    /**
     * Standard TestCase constructor: creates a new MemoryUsageResultTest object.
     *
     * @param testName The name to be given to this TestCase.
     */
    public MemoryUsageResultTest(String testName) {
        super(testName);
    }

    /**
     * Static method expected by JUnit test runners. Returns a Test containing all
     * the testXXX methods in this class.
     *
     * @return A new Test, containing all the testXXX methods in this class.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(MemoryUsageResultTest.class);
        return suite;
    }

    /**
     * Tests getObjectCount() and cumulate() methods.
     */
    public void testResultGetObjectCount() {
        MemoryUsageResult result = new MemoryUsageResult();
        assertEquals("object count not equal", 0, result.getObjectCount());

        result.cumulate(2, 4);
        assertEquals("object count not equal", 2, result.getObjectCount());

        result.cumulate(239, 7);
        assertEquals("object count not equal", 241, result.getObjectCount());

        result.cumulate(59, 23);
        assertEquals("object count not equal", 300, result.getObjectCount());
    }

    /**
     * Tests cumulate() properly throws IllegalArgumentException
     * on negative or zero values.
     * Expected exception: IllegalArgumentException.
     */
    public void testCumulateErr() {
        MemoryUsageResult result = new MemoryUsageResult();
        try {
            result.cumulate(-26, 254);
            fail("cumulate() allowed negative object count");
        } catch (IllegalArgumentException ex) {
            // Test passed
        }

        try {
            result.cumulate(6, -2);
            fail("cumulate() allowed negative memory size");
        } catch (IllegalArgumentException ex) {
            // Test passed
        }

        try {
            result.cumulate(0, 25);
            fail("cumulate() allowed zero object count");
        } catch (IllegalArgumentException ex) {
            // Test passed
        }

        try {
            result.cumulate(6, 0);
            fail("cumulate() allowed zero memory size");
        } catch (IllegalArgumentException ex) {
            // Test passed
        }
    }

    /**
     * Testing the getDetail().
     */
    public void testGetDetail() {
        HashMap map = new HashMap();
        MemoryUsageDetail detail = new MemoryUsageDetail(String.class);
        detail.cumulate(2, 10);
        map.put(String.class, detail);
        detail = new MemoryUsageDetail(Integer.class);
        detail.cumulate(2, 10);
        map.put(Integer.class, detail);
        detail = new MemoryUsageDetail(HashMap.class);
        detail.cumulate(15, 1234);
        map.put(HashMap.class, detail);

        MemoryUsageResult result = new MemoryUsageResult();
        result.setDetails(map);

        detail = result.getDetail(Integer.class);

        assertEquals("Object count incorrect", 2, detail.getObjectCount());
        assertEquals("Memory count incorrect", 10, detail.getUsedMemory());

        detail = result.getDetail(String.class);

        assertEquals("Object count incorrect", 2, detail.getObjectCount());
        assertEquals("Memory count incorrect", 10, detail.getUsedMemory());

        detail = result.getDetail(HashMap.class);

        assertEquals("Object count incorrect", 15, detail.getObjectCount());
        assertEquals("Memory count incorrect", 1234, detail.getUsedMemory());

        detail = result.getDetail(Double.class);

        assertNull("MemoryUsageDetail should be null", detail);
    }

    /**
     * Testing the getDetails().
     */
    public void testGetDetails() {
        HashMap map = new HashMap();
        MemoryUsageResult result = new MemoryUsageResult();
        result.setDetails(map);

        MemoryUsageDetail[] detail = result.getDetails();

        assertNotNull("MemoryUsageDetail array should not be null", detail);
        assertEquals("MemoryUsageDetail array length incorrect", 0, detail.length);

        MemoryUsageDetail mem = new MemoryUsageDetail(String.class);
        map.put(String.class, mem);

        mem = new MemoryUsageDetail(Integer.class);
        map.put(Integer.class, mem);

        result.setDetails(map);

        detail = result.getDetails();

        assertNotNull("MemoryUsageDetail array should not be null", detail);
        assertEquals("MemoryUsageDetail array length incorrect", 2, detail.length);

        mem = detail[0];
        if (mem.getDetailClass() == String.class) {
            assertEquals("Object count incorrect", 0, mem.getObjectCount());
            assertEquals("Memory count incorrect", 0, mem.getUsedMemory());

            mem = detail[1];
            assertEquals("Object count incorrect", 0, mem.getObjectCount());
            assertEquals("Memory count incorrect", 0, mem.getUsedMemory());
            assertEquals("Detail class incorrect", Integer.class, mem.getDetailClass());
        } else {
            assertEquals("Object count incorrect", 0, mem.getObjectCount());
            assertEquals("Memory count incorrect", 0, mem.getUsedMemory());

            mem = detail[1];
            assertEquals("Object count incorrect", 0, mem.getObjectCount());
            assertEquals("Memory count incorrect", 0, mem.getUsedMemory());
            assertEquals("Detail class incorrect", String.class, mem.getDetailClass());
        }
    }

}
