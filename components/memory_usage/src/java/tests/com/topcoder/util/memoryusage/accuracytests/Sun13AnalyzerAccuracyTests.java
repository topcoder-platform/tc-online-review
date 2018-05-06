/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 *
 * TCS Memory Usage Version 2.0 Accuracytests.
 *
 * @ Sun13AnalyzerAccuracyTests.java
 */
package com.topcoder.util.memoryusage.accuracytests;

import com.topcoder.util.memoryusage.MemoryUsage;
import com.topcoder.util.memoryusage.MemoryUsageListener;
import com.topcoder.util.memoryusage.analyzers.Sun13Analyzer;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.HashMap;
import java.util.Map;


/**
 * <p>
 * The <code>BaseAnalyzer</code>'s Accuracy Tests.
 * This accuracy tests addresses the functionality provided
 * by the <code>BaseAnalyzer</code> class by <code>Sun13Analyzer</code>.
 * </p>
 *
 * @author zmg
 * @version 2.0
 */
public class Sun13AnalyzerAccuracyTests extends TestCase {

    /**
     * <p>
     * Number of objects to instantiate for getAverageSize().
     * </p>
     */
    private static int ITERATIONS = 10000;

    /**
     * <p>
     * The instance of <c>Sun13Analyzer</c> used for tests.
     * </p>
     */
    private Sun13Analyzer test = new Sun13Analyzer();

    /**
     * <p>
     * The instance of <c>MockListener</c> used for tests.
     * </p>
     */
    private MemoryUsageListener listener = new MockListener();

    /**
     * <p>
     * Original JVM version.
     * </p>
     */
    private String jvmVersion;

    /**
     * <p>
     * Original vendor.
     * </p>
     */
    private String vendor;

    /**
     * <p>
     * Test suite of <code>Sun13AnalyzerAccuracyTests</code>.
     * </p>
     *
     * @return Test suite of <code>Sun13AnalyzerAccuracyTests</code>.
     */
    public static Test suite() {
        return new TestSuite(Sun13AnalyzerAccuracyTests.class);
    }

    /**
     * <p>
     * Set the JVM to JDK1.4. Keep track of original JVM so that it can be restored later.
     * </p>
     *
     * @throws Exception in case of any error. Exception will be caught by the test runner.
     */
    public void setUp() throws Exception {
        jvmVersion = System.getProperty("java.class.version");
        System.setProperty("java.class.version", "47.0");
        vendor = System.getProperty("java.vendor");
        System.setProperty("java.vendor", "Sun Microsystems");
    }

    /**
     * <p>
     * Restore original JVM version and vendor.
     * </p>
     */
    public void tearDown() {
        System.setProperty("java.class.version", jvmVersion);
        System.setProperty("java.vendor", vendor);
    }

    /**
     * <p>
     * Test the deep memory usage with cusom classes in JVM 1.4.
     * </p>
     *
     * @throws Exception exception to JUnit.
     */
    public void testSun13AccuracyCustomClass_Deep() throws Exception {
        assertEquals("class TestA size not correct",
            TestHelper.getAverageSize(TestA.class, ITERATIONS),
            test.getDeepMemoryUsage(new TestA(), listener).getUsedMemory());

        assertEquals("class TestB size not correct",
            TestHelper.getAverageSize(TestB.class, ITERATIONS),
            test.getDeepMemoryUsage(new TestB(), listener).getUsedMemory());

        assertEquals("class TestC size not correct",
            TestHelper.getAverageSize(TestC.class, ITERATIONS),
            test.getDeepMemoryUsage(new TestC(), listener).getUsedMemory());

        assertEquals("class TestD size not correct",
            TestHelper.getAverageSize(TestD.class, ITERATIONS),
            test.getDeepMemoryUsage(new TestD(), listener).getUsedMemory());
    }

    /**
     * <p>
     * Test the deep memory usage with cusom classes in JVM 1.4.
     * This classes contains some primitive variables and null.
     * </p>
     *
     * @throws Exception exception to JUnit.
     */
    public void testSun13AccuracyCustomClass_Simple() throws Exception {
        // size : 8 + 3 * 4 = 22 -> 24
        assertEquals("class SimpleClassA size not correct", 24,
            test.getDeepMemoryUsage(new SimpleClassA(), listener).getUsedMemory());

        // size : 8 + 4 + 8 + 4 = 24 -> 24
        assertEquals("class TestB size not correct", 24,
            test.getDeepMemoryUsage(new SimpleClassB(), listener).getUsedMemory());
    }

    /**
     * <p>
     * Test the deep memory usage with built in classes in JVM 1.4.
     * </p>
     *
     * @throws Exception exception to JUnit.
     */
    public void testSun13AccuracyBuiltInClass_Deep() throws Exception {
        assertEquals("class String size not correct",
            TestHelper.getAverageSize(String.class, ITERATIONS),
            test.getDeepMemoryUsage(new String(), listener).getUsedMemory());

        assertEquals("class HashMap size not correct",
            TestHelper.getAverageSize(HashMap.class, ITERATIONS),
            test.getDeepMemoryUsage(new HashMap(), listener).getUsedMemory());

        assertEquals("class StringBuffer size not correct",
            TestHelper.getAverageSize(StringBuffer.class, ITERATIONS),
            test.getDeepMemoryUsage(new StringBuffer(), listener).getUsedMemory());
    }

    /**
     * <p>
     * Test the shallow memory usage with cusom classes in JVM 1.4.
     * </p>
     *
     * @throws Exception exception to JUnit.
     */
    public void testSun13AccuracyCustomClass_Shallow()
        throws Exception {
        long lengthD = TestHelper.getAverageSize(TestD.class, ITERATIONS);
        long lengthE = TestHelper.getAverageSize(TestE.class, ITERATIONS);

        assertEquals("class TestD shallow memory size not correct", lengthD,
            test.getShallowMemoryUsage(new TestD()).getUsedMemory());

        assertEquals("class TestE shallow memory size not correct",
            lengthE - lengthD,
            test.getShallowMemoryUsage(new TestE()).getUsedMemory());
    }

    /**
     * <p>
     * Test the shallow memory usage with Arrays in JVM 1.4.
     * </p>
     *
     * @throws Exception exception to JUnit.
     */
    public void testSun13AccuracyArray_Shallow() throws Exception {
        // the size is : 12 + 1 * 10 = 22 -> 24
        byte[] array1 = new byte[10];

        assertEquals("byte array memory size not correct", 24,
            test.getShallowMemoryUsage(array1).getUsedMemory());

        // the size is : 12 + 2 * 10 = 32 -> 32
        short[] array2 = new short[10];

        assertEquals("short array memory size not correct", 32,
            test.getShallowMemoryUsage(array2).getUsedMemory());

        // the size is : 12 + 4 * 10 = 52 -> 56
        float[] array3 = new float[10];
        assertEquals("float array memory size not correct", 56,
            test.getShallowMemoryUsage(array3).getUsedMemory());

        // the size is : 12 + 8 * 10 = 92 -> 96
        double[] array4 = new double[10];
        assertEquals("double array memory size not correct", 96,
            test.getShallowMemoryUsage(array4).getUsedMemory());

        // the size is : 12 + 4 * 10 = 52 -> 56
        Object[] array5 = new Object[10];
        assertEquals("object array memory size not correct", 56,
            test.getShallowMemoryUsage(array5).getUsedMemory());
    }

    /**
     * <p>
     * Test the shallow memory usage with <c>null</c> in JVM 1.4.
     * </p>
     *
     * @throws Exception exception to JUnit.
     */
    public void testSun13AccuracyNull_Shallow() throws Exception {
        assertEquals("null memory size not correct", 4,
            test.getShallowMemoryUsage(null).getUsedMemory());
    }
}
