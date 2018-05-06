/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.memoryusage;

import java.util.ArrayList;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests of BaseAnalyzer.
 *
 * @author TexWiller
 * @version 2.0
 */
public class BaseAnalyzerTest extends TestCase {

    /**
     * BaseAnalyzer used in the tests. Created in setUp().
     */
    private BaseAnalyzer analyzer;

    /**
     * ClassA instance used in the tests. Created in setUp().
     */
    private TestsHelper.ClassA classA;

    /**
     * ClassB instance used in the tests. Created in setUp().
     */
    private TestsHelper.ClassB classB;

    /**
     * ClassC instance used in the tests. Created in setUp().
     */
    private TestsHelper.ClassC classC;

    /**
     * Standard TestCase constructor: creates a new BaseAnalyzerTest object.
     *
     * @param testName The name to be given to this TestCase.
     */
    public BaseAnalyzerTest(String testName) {
        super(testName);
    }

    /**
     * Sets up the fixture.
     */
    protected void setUp() {
        analyzer = new BaseAnalyzerImpl();
        classA = new TestsHelper.ClassA();
        classB = new TestsHelper.ClassB();
        classC = new TestsHelper.ClassC();
    }

    /**
     * Tears down the fixture.
     */
    protected void tearDown() {
    }

    /**
     * Static method expected by JUnit test runners. Returns a Test containing all
     * the testXXX methods in this class.
     *
     * @return A new Test, containing all the testXXX methods in this class.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(BaseAnalyzerTest.class);
        return suite;
    }

    /**
     * Test of constructor of BaseAnalyzer.
     */
    public void testConstructor() {
        new BaseAnalyzer("Test", "Test") {
            public boolean matchesJVM() {
                return true;
            }
        };
    }


    /**
     * Test of getShallowMemoryUsage method, with <code>null</code> parameter:
     * should return 0 memory usage.
     * @throws MemoryUsageException If protection errors occur: should not happen.
     */
    public void testGetShallowMemoryUsageNull() throws MemoryUsageException {
        MemoryUsageResult result = analyzer.getShallowMemoryUsage(null);
        assertEquals("Wrong memory usage result reported for null object", 4, result.getUsedMemory());
    }

    /**
     * Test of getShallowMemoryUsage method. Checks the sizes for
     * the three classes ClassA, ClassB and ClassC.
     * @throws MemoryUsageException If protection errors occur: should not happen.
     */
    public void testGetShallowMemoryUsage() throws MemoryUsageException {
        MemoryUsageResult result = analyzer.getShallowMemoryUsage(classA);
        assertEquals("Wrong shallow memory size for ClassA", 32, result.getUsedMemory());
        result = analyzer.getShallowMemoryUsage(classB);
        assertEquals("Wrong shallow memory size for ClassB", 48, result.getUsedMemory());
        result = analyzer.getShallowMemoryUsage(classC);
        assertEquals("Wrong shallow memory size for ClassC", 56, result.getUsedMemory());
    }

    /**
     * Test of getDeepMemoryUsage() method, without listener.
     * @throws MemoryUsageException If protection errors occur: should not happen.
     */
    public void testGetDeepMemoryUsage1() throws MemoryUsageException {
        MemoryUsageResult result = analyzer.getDeepMemoryUsage(classA, null);
        assertEquals("Wrong deep memory size for ClassA",
                40, result.getUsedMemory());
        result = analyzer.getDeepMemoryUsage(classB, null);
        assertEquals("Wrong deep memory size for ClassB",
                152, result.getUsedMemory());
    }

    /**
     * Test of getDeepMemoryUsage() method, with listener and no inheritance.
     * Checks the objects passed to the listener are the expected ones.
     * @throws MemoryUsageException If protection errors occur: should not happen.
     */
    public void testGetDeepMemoryUsage2() throws MemoryUsageException {
        MemoryUsageListenerImpl listener = new MemoryUsageListenerImpl();
        MemoryUsageResult result = analyzer.getDeepMemoryUsage(classA, listener);
        assertEquals("Wrong deep memory size for ClassA",
                40, result.getUsedMemory());
        TestsHelper.listsEqual(listener.getReachedObject(), classA.getEmbeddedDeep(), true);
    }

    /**
     * Test of getDeepMemoryUsage() method, with listener and inheritance.
     * Checks the objects passed to the listener are the expected ones.
     * @throws MemoryUsageException If protection errors occur: should not happen.
     */
    public void testGetDeepMemoryUsage3() throws MemoryUsageException {
        MemoryUsageListenerImpl listener = new MemoryUsageListenerImpl();
        MemoryUsageResult result = analyzer.getDeepMemoryUsage(classB, listener);
        assertEquals("Wrong deep memory size for ClassB",
                152, result.getUsedMemory());
        TestsHelper.listsEqual(listener.getReachedObject(), classB.getEmbeddedDeep(), true);
    }

    /**
     * Test of analyze() method, shallow check of memory sizes.
     * @throws MemoryUsageException If protection errors occur: should not happen.
     */
    public void testAnalyze1() throws MemoryUsageException {
        MemoryUsageResult result = analyzer.analyze(classA, false, null);
        assertEquals("Wrong shallow memory size for ClassA", 32, result.getUsedMemory());
        result = analyzer.analyze(classB, false, null);
        assertEquals("Wrong shallow memory size for ClassB", 48, result.getUsedMemory());
        result = analyzer.analyze(classC, false, null);
        assertEquals("Wrong shallow memory size for ClassC", 56, result.getUsedMemory());
    }

    /**
     * Test of analyze() method, deep check of memory sizes.
     * The calculated values are compared with the ones estimated by TestsHelper.evaluateMemory().
     * @throws MemoryUsageException If protection errors occur: should not happen.
     */
    public void testAnalyze2() throws MemoryUsageException {
        MemoryUsageResult result = analyzer.analyze(classA, true, null);
        assertEquals("Wrong deep memory size for ClassA",
                40, result.getUsedMemory());
        result = analyzer.analyze(classB, true, null);
        assertEquals("Wrong deep memory size for ClassB",
                152, result.getUsedMemory());
        result = analyzer.analyze(classC, true, null);
        assertEquals("Wrong deep memory size for ClassC",
                240, result.getUsedMemory());
    }

    /**
     * Test of analyze() method, shallow check of listener calls: should never be called.
     * @throws MemoryUsageException If protection errors occur: should not happen.
     */
    public void testAnalyze3() throws MemoryUsageException {
        MemoryUsageListenerImpl listener = new MemoryUsageListenerImpl();
        MemoryUsageResult result = analyzer.analyze(classB, false, listener);
        assertEquals("Unexpected listener calls arrived", 0, listener.getReachedObject().size());
    }

    /**
     * Test of analyze() method, deep check of listener calls,
     * no inheritance.
     * @throws MemoryUsageException If protection errors occur: should not happen.
     */
    public void testAnalyze4() throws MemoryUsageException {
        MemoryUsageListenerImpl listener = new MemoryUsageListenerImpl();
        MemoryUsageResult result = analyzer.analyze(classA, true, listener);
        TestsHelper.listsEqual(listener.getReachedObject(), classA.getEmbeddedDeep(), true);
    }

    /**
     * Test of analyze() method, deep check of listener calls,
     * with inheritance.
     * @throws MemoryUsageException If protection errors occur: should not happen.
     */
    public void testAnalyze5() throws MemoryUsageException {
        MemoryUsageListenerImpl listener = new MemoryUsageListenerImpl();
        MemoryUsageResult result = analyzer.analyze(classB, true, listener);
        TestsHelper.listsEqual(listener.getReachedObject(), classB.getEmbeddedDeep(), true);
    }

    /**
     * Test of analyze() method, deep check of listener calls on a complex object
     * (with inheritance, self-references, double references, null objects).
     * @throws MemoryUsageException If protection errors occur: should not happen.
     */
    public void testAnalyze6() throws MemoryUsageException {
        MemoryUsageListenerImpl listener = new MemoryUsageListenerImpl();
        MemoryUsageResult result = analyzer.analyze(classC, true, listener);
        TestsHelper.listsEqual(listener.getReachedObject(), classC.getEmbeddedDeep(), true);
    }

    /**
     * Test of getFieldSize() method. Must return 4 for everything, except for double and long,
     * for which it must return 8.
     */
    public void testGetFieldSize() {
        assertEquals("Wrong double type size", 8, analyzer.getFieldSize(Double.TYPE));
        assertEquals("Wrong long type size", 8, analyzer.getFieldSize(Long.TYPE));
        assertEquals("Wrong int type size", 4, analyzer.getFieldSize(Integer.TYPE));
        assertEquals("Wrong float type size", 4, analyzer.getFieldSize(Float.TYPE));
        assertEquals("Wrong short type size", 4, analyzer.getFieldSize(Short.TYPE));
        assertEquals("Wrong char type size", 4, analyzer.getFieldSize(Character.TYPE));
        assertEquals("Wrong boolean type size", 4, analyzer.getFieldSize(Boolean.TYPE));
        assertEquals("Wrong byte type size", 4, analyzer.getFieldSize(Byte.TYPE));
        assertEquals("Wrong Object type size", 4, analyzer.getFieldSize(Object.class));
        assertEquals("Wrong String type size", 4, analyzer.getFieldSize(String.class));
    }

    /**
     * Test of getFieldSize() method, with <code>null</code> parameter.
     * Expected exception: IllegalArgumentException.
     */
    public void testGetFieldSizeNull() {
        try {
            analyzer.getFieldSize(null);
            fail("getFieldSize() did not throw an IllegalArgumentException when passed a null parameter");
        } catch (IllegalArgumentException ex) {
            // Test passed
        }
    }

    /**
     * Test of getArraySize method.
     */
    public void testGetArraySize() {
        assertEquals("Wrong arraySize value", 10, analyzer.getArraySize(new byte[10]));
        assertEquals("Wrong arraySize value", 20, analyzer.getArraySize(new short[10]));
        assertEquals("Wrong arraySize value", 40, analyzer.getArraySize(new int[10]));
        assertEquals("Wrong arraySize value", 80, analyzer.getArraySize(new double[10]));
    }

    /**
     * Test of getArraySize method, with <code>null</code> parameter.
     * Expected exception: IllegalArgumentException.
     */
    public void testGetArraySizeNull() {
        try {
            analyzer.getArraySize(null);
            fail("getArraySize() did not throw an IllegalArgumentException when passed a null parameter");
        } catch (IllegalArgumentException ex) {
            // Test passed
        }
    }

    /**
     * Test of getArraySize method, with not array parameter.
     * Expected exception: IllegalArgumentException.
     */
    public void testGetArraySizeErr() {
        try {
            analyzer.getArraySize(new Object());
            fail("getArraySize() did not throw an IllegalArgumentException when passed a non-array parameter");
        } catch (IllegalArgumentException ex) {
            // Test passed
        }
    }

    /**
     * Test of getObjectBase() method.
     */
    public void testGetObjectBase() {
        assertEquals("Wrong objectBase value", 8, analyzer.getObjectBase());
    }

    /**
     * Test of getArrayBase() method.
     */
    public void testGetArrayBase() {
        assertEquals("Wrong arrayBase value", 12, analyzer.getArrayBase());
    }

    /**
     * Test of getObjectAlign() method.
     */
    public void testGetObjectAlign() {
        assertEquals("Wrong objectAlign value", 8, analyzer.getObjectAlign());
    }

    /**
     * Test of getArrayAlign() method.
     */
    public void testGetArrayAlign() {
        assertEquals("Wrong arrayAlign value", 8, analyzer.getArrayAlign());
    }

    /**
     * Test of toString() method.
     */
    public void testToString() {
        assertNotNull("toString() returned empty string", analyzer.toString());
    }

    /**
     * Dummy implementation of BaseAnalyzer. matchesJVM() simply returns
     * always <code>true</code>.
     */
    private class BaseAnalyzerImpl extends BaseAnalyzer {

        /**
         * Creates a new BaseAnalyzer implementation. Vendor and version
         * parameters of the super constructor are dummy strings.
         */
        public BaseAnalyzerImpl() {
            super("Dummy vendor", "Dummy version");
        }

        /**
         * Tests whether this BaseAnalyzer supports the current JVM.
         *
         * @return Always <code>true</code>.
         */
        public boolean matchesJVM() {
            return true;
        }

        /**
         * Publicly exports the analuze() method, in order to be able to test it.
         * @param obj The object to get the memory usage for; can be <code>null</code>
         * @param listener The MemoryUsageListener to specify which objects should
         * be included in the memory usage sum, and which should not. Can be <code>null</code>,
         * meaning that all the objects will be included.
         * @param goDeep <code>true</code> if embedded objects should be recursively traversed
         * (deep memory usage), <code>false</code> otherwise (shallow memory usage)
         * @return The memory usage result of the specified object
         * @throws MemoryUsageException If exceptions occurred while traversing the object
         */
        public MemoryUsageResult analyze(Object obj, boolean goDeep, MemoryUsageListener listener)
            throws MemoryUsageException {
            return super.analyze(obj, goDeep, listener);
        }
    }

    /**
     * Straightforward implementation of MemoryUsageListener interface.
     * It accepcts every reached object, keeping track of them in a list.
     */
    private class MemoryUsageListenerImpl implements MemoryUsageListener {

        /**
         * The list which is used to keep track of every reached object.
         */
        private List reachedObjects = new ArrayList();

        /**
         * Records every reached object in the list, allowing its traversal.
         * @param obj The object reached
         * @return Always <code>true</code>
         */
        public boolean objectReached(Object obj) {
            reachedObjects.add(obj);
            return true;
        }

        /**
         * Returns the list of all the objects reached with this MemoryUsageListener.
         * @return A non-<code>null</code> list of objects
         */
        public List getReachedObject() {
            return reachedObjects;
        }
    }
}
