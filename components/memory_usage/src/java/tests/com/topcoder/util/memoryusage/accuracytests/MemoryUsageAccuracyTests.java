/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 *
 * TCS Memory Usage Version 2.0 Accuracytests.
 *
 * @ MemoryUsageAccuracyTests.java
 */
package com.topcoder.util.memoryusage.accuracytests;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.memoryusage.MemoryUsage;
import com.topcoder.util.memoryusage.MemoryUsageAnalyzer;
import com.topcoder.util.memoryusage.MemoryUsageListener;
import com.topcoder.util.memoryusage.analyzers.IBM14Analyzer;
import com.topcoder.util.memoryusage.analyzers.Sun13Analyzer;
import com.topcoder.util.memoryusage.analyzers.Sun14Analyzer;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * The <code>MemoryUsage</code>'s Accuracy Tests.
 * This accuracy tests addresses the functionality provided
 * by the <code>MemoryUsage</code> class.
 * </p>
 *
 * @author zmg
 * @version 2.0
 */
public class MemoryUsageAccuracyTests extends TestCase {
    /**
     * <p>
     * The value of the namespace of used for test.
     * </p>
     */
    protected static final String NAMESPACE = "com.topcoder.util.memoryusage.MemoryUsage";

    /**
     * <p>
     * Number of objects to instantiate for getAverageSize().
     * </p>
     */
    private static int ITERATIONS = 10000;

    /**
     * <p>
     * The a list (non-null and non-empty) of analyzers used for test.
     * </p>
     */
    private MemoryUsageAnalyzer[] analyzers;

    /**
     * <p>
     * The instance of <code>MemoryUsage</code> used for tests.
     * </p>
     */
    private MemoryUsage test;

    /**
     * <p>
     * The instance of <c>MockListener</c> used for tests.
     * </p>
     */
    private MemoryUsageListener listener = new MockListener();

    /**
     * <p>
     * The instance of <code>ConfigManager</code> for test.
     * </p>
     */
    private ConfigManager cm = null;

    /**
     * <p>
     * Test suite of <code>MemoryUsageAccuracyTests</code>.
     * </p>
     *
     * @return Test suite of <code>MemoryUsageAccuracyTests</code>.
     */
    public static Test suite() {
        return new TestSuite(MemoryUsageAccuracyTests.class);
    }

    /**
     * <p>
     * Initialization for all tests here.
     * </p>
     *
     * @throws Exception exception to JUnit.
     */
    protected void setUp() throws Exception {
        // clear all the configs.
        TestHelper.clearConfig();
        cm = ConfigManager.getInstance();

        // XML file used to creat MemoryUsage.
        cm.add("accuracytests/config1.xml");

        analyzers = new MemoryUsageAnalyzer[] {
            new Sun13Analyzer(), new Sun14Analyzer()
        };
    }

    /**
     * <p>
     * Tear down the accuracy test environment.
     * </p>
     *
     * @throws Exception exception to JUnit.
     */
    protected void tearDown() throws Exception {
        // clear all the configs.
        TestHelper.clearConfig();
    }

    /**
    * <p>
    * Accuracy Test of the <code>MemoryUsage()</code> constructor.
    * </p>
    *
    * @throws Exception exception to JUnit.
    */
    public void testConstructor() throws Exception {
        // creat the MemoryUsage by default name space
        test = new MemoryUsage();

        // get the fallBack analyzer to check the constructor
        MemoryUsageAnalyzer analyzer = test.getFallBackAnalyzer();
        assertTrue("analyzer should be an instance of Sun13Analyzer",
            analyzer instanceof Sun13Analyzer);
    }

    /**
     * <p>
     * Accuracy Test of the <code>MemoryUsage(String)</code> constructor.
     * </p>
     *
     * @throws Exception exception to JUnit.
     */
    public void testConstructor2() throws Exception {
        // creat the MemoryUsage by given name space
        test = new MemoryUsage(NAMESPACE);

        // get the fallBack analyzer to check the constructor
        MemoryUsageAnalyzer analyzer = test.getFallBackAnalyzer();
        assertTrue("analyzer should be an instance of Sun13Analyzer",
            analyzer instanceof Sun13Analyzer);
    }

    /**
     * <p>
     * Accuracy Test of the <code>MemoryUsage(String)</code> constructor.
     * when the value of default_fallback_analyzer_flag is <c>true</c>
     * </p>
     *
     * @throws Exception exception to JUnit.
     */
    public void testConstructor2_True() throws Exception {
        // clear all the configs.
        TestHelper.clearConfig();

        // XML file used to creat MemoryUsage.
        cm.add("accuracytests/config_true.xml");

        // creat the MemoryUsage by given name space
        test = new MemoryUsage(NAMESPACE);

        // get the fallBack analyzer to check the constructor
        MemoryUsageAnalyzer analyzer = test.getFallBackAnalyzer();
        assertTrue("analyzer should be an instance of Sun14Analyzer",
            analyzer instanceof Sun14Analyzer);
    }

    /**
     * <p>
     * Accuracy Test of the <code>MemoryUsage(String)</code> constructor.
     * when the value of default_fallback_analyzer_flag is <c>false</c>
     * </p>
     *
     * @throws Exception exception to JUnit.
     */
    public void testConstructor2_False() throws Exception {
        // clear all the configs.
        TestHelper.clearConfig();

        // XML file used to creat MemoryUsage.
        cm.add("accuracytests/config_false.xml");

        // creat the MemoryUsage by given name space
        test = new MemoryUsage(NAMESPACE);

        // get the fallBack analyzer to check the constructor
        assertNull("analyzer should be null", test.getFallBackAnalyzer());
    }

    /**
     * <p>
     * Accuracy Test of the <code>MemoryUsage(String)</code> constructor.
     * when the value of analyzers_namespace is an <c>empty</c> string.
     * </p>
     *
     * @throws Exception exception to JUnit.
     */
    public void testConstructor2_Empty() throws Exception {
        // clear all the configs.
        TestHelper.clearConfig();

        // XML file used to creat MemoryUsage.
        cm.add("accuracytests/config_empty.xml");

        // creat the MemoryUsage by given name space
        test = new MemoryUsage(NAMESPACE);

// get the fallBack analyzer to check the constructor
        MemoryUsageAnalyzer analyzer = test.getFallBackAnalyzer();
        assertTrue("analyzer should be an instance of Sun13Analyzer",
            analyzer instanceof Sun13Analyzer);
    }

    /**
     * <p>
     * Accuracy Test of the <code>MemoryUsage(String)</code> constructor.
     * when the value of default_fallback_analyzer_flag is <c>true</c>, but
     * fallbackAnalyzer is not null, so "useDefaultFallback" parameter will be ignored.
     * </p>
     *
     * @throws Exception exception to JUnit.
     */
    public void testConstructor2_Ignore() throws Exception {
        // clear all the configs.
        TestHelper.clearConfig();

        // XML file used to creat MemoryUsage.
        cm.add("accuracytests/config_ignored.xml");

        // creat the MemoryUsage by given name space
        test = new MemoryUsage(NAMESPACE);

        // get the fallBack analyzer to check the constructor
        MemoryUsageAnalyzer analyzer = test.getFallBackAnalyzer();
        assertTrue("analyzer should be an instance of Sun13Analyzer",
            analyzer instanceof Sun13Analyzer);
    }

    /**
     * <p>
     * Accuracy Test of the <code>MemoryUsage(MemoryUsageAnalyzer[], MemoryUsageAnalyzer,
     * boolean)</code> constructor.
     * </p>
     *
     * @throws Exception exception to JUnit.
     */
    public void testConstructor3() throws Exception {
        // creat the MemoryUsage by given param.
        MemoryUsageAnalyzer analyzer = new IBM14Analyzer();
        test = new MemoryUsage(analyzers, analyzer, false);

        // get the fallBack analyzer to check the constructor
        assertEquals("analyzer should be equal", analyzer,
            test.getFallBackAnalyzer());

        test = new MemoryUsage(analyzers, null, true);

        // get the fallBack analyzer to check the constructor
        MemoryUsageAnalyzer analyzer14 = test.getFallBackAnalyzer();
        assertTrue("analyzer should be an instance of Sun14Analyzer",
            analyzer14 instanceof Sun14Analyzer);

        test = new MemoryUsage(analyzers, null, false);

        // get the fallBack analyzer to check the constructor
        assertNull("analyzer should be null", test.getFallBackAnalyzer());

        test = new MemoryUsage(analyzers, analyzer, true);

        // get the fallBack analyzer to check the constructor
        assertEquals("analyzer should be equal", analyzer,
            test.getFallBackAnalyzer());
    }

    /**
     * <p>
     * Accuracy Test of the <code>getEmbeddedObjects(Object)</code> method.
     * </p>
     *
     * @throws Exception exception to JUnit.
     */
    public void testgetEmbeddedObjects() throws Exception {
        // get the embedded objects.
        Object[] objects = MemoryUsage.getEmbeddedObjects(new Class1());
        assertNotNull(objects);
        assertEquals("Incorrect # of embedded objects for Class1", 0, objects.length);

        // get the embedded objects.
        objects = MemoryUsage.getEmbeddedObjects(Class2.class.newInstance());
        assertNotNull(objects);
        assertEquals("Incorrect # of embedded objects for Class1", 1, objects.length);

        // get the embedded objects.
        objects = MemoryUsage.getEmbeddedObjects(new Class3());
        assertNotNull(objects);
        assertEquals("Incorrect # of embedded objects for Class1", 3, objects.length);
    }

    /**
     * <p>
     * Test the deep memory usage with cusom classes in JVM 1.4.
     * </p>
     *
     * @throws Exception exception to JUnit.
     */
    public void testgetDeepMemoryUsage() throws Exception {
        // creat the MemoryUsage by default name space
        test = new MemoryUsage();

        assertEquals("class TestA size not correct",
            TestHelper.getAverageSize(TestA.class, ITERATIONS),
            test.getDeepMemoryUsage(new TestA()).getUsedMemory());

        assertEquals("class TestB size not correct",
            TestHelper.getAverageSize(TestB.class, ITERATIONS),
            test.getDeepMemoryUsage(new TestB()).getUsedMemory());

        assertEquals("class TestC size not correct",
            TestHelper.getAverageSize(TestC.class, ITERATIONS),
            test.getDeepMemoryUsage(new TestC()).getUsedMemory());

        assertEquals("class TestD size not correct",
            TestHelper.getAverageSize(TestD.class, ITERATIONS),
            test.getDeepMemoryUsage(new TestD()).getUsedMemory());

        assertEquals("class TestD size not correct",
                TestHelper.getAverageSize(TestE.class, ITERATIONS),
                test.getDeepMemoryUsage(new TestE(), listener).getUsedMemory());
    }

    /**
     * <p>
     * Test the shallow memory usage with cusom classes in JVM 1.4.
     * </p>
     *
     * @throws Exception exception to JUnit.
     */
    public void testgetShallowMemoryUsage()
        throws Exception {
        // creat the MemoryUsage by default name space
        test = new MemoryUsage();

        long lengthD = TestHelper.getAverageSize(TestD.class, ITERATIONS);
        long lengthE = TestHelper.getAverageSize(TestE.class, ITERATIONS);

        assertEquals("class TestD shallow memory size not correct", lengthD,
            test.getShallowMemoryUsage(new TestD()).getUsedMemory());

        assertEquals("class TestE shallow memory size not correct", lengthE - lengthD,
            test.getShallowMemoryUsage(new TestE()).getUsedMemory());
    }
}

////////////////
//  Test classes
//
    /**
     * <p>
     * The class used for test.
     * </p>
     */
class Class1 {
    private boolean a;
    byte b;
    public char c;
    short d;
    protected int e;
    long f;
    public float g;
    double h;
 }

    /**
     * <p>
     * The class used for test.
     * </p>
     */
class Class2 extends Class1 {
    Integer i = null;
    private Class1 j = new Class1();
    String s = null;
 }

    /**
     * <p>
     * The class used for test.
     * </p>
     */
class Class3 extends Class2 {
    private boolean[] aa;
    byte[] bb;
    public char[] cc;
    short[] dd;
    protected int[] ee;
    long[] ff;
    public float[] gg;
    double[] hh;
    Object[] jj = new Object[100];
    Class1 [] aaa = { new Class1(), new Class2()};
 }

