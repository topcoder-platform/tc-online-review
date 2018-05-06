/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.memoryusage.analyzers;

import com.topcoder.util.memoryusage.MemoryUsageAnalyzer;
import com.topcoder.util.memoryusage.TestsHelper;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import junit.framework.TestCase;

/**
 * Helper class for easier testing of the classes in the
 * <code>com.topcoder.util.memoryusage.analyzers</code> package.
 *
 * @author TexWiller
 * @version 2.0
 */
public abstract class GenericAnalyzerTest extends TestCase {

    /**
     * Stores the analyzer to perform tests upon. Extending classes
     * must initialize this variable in the setUp() method.
     */
    protected MemoryUsageAnalyzer analyzer;

    /**
     * Stores the vendor string to be tested in matchesJVM() method.
     */
    private String vendor;

    /**
     * Stores the version string to be tested in matchesJVM() method.
     */
    private String version;

    /**
     * Specifies whether the getFieldSizes() method must be tested.
     */
    private boolean testFieldSizes;

    /**
     * Specifies the value expected from the getObjectBase() method;
     * if it is -1, the method must not be tested.
     */
    private int objectBase;

    /**
     * Cretes a new GenericAnalyzerTest.
     * @param testName The name of the TestCase.
     * @param vendor The string that will be temporarily put as the vendor system property.
     * @param version The string that will be temporarily put as the version system property.
     * @param testFieldSizes Specifies whether the getFieldSizes() method must be tested.
     * @param objectBase Specifies the value expected from the getObjectBase() method. Can be -1,
     * to indicate that this method must not be tested.
     */
    public GenericAnalyzerTest(String testName, String vendor, String version, boolean testFieldSizes, int objectBase) {
        super(testName);
        this.vendor = vendor;
        this.version = version;
        this.testFieldSizes = testFieldSizes;
        this.objectBase = objectBase;
    }

    /**
     * Test of matchesJVM method with correct vendor and version.
     */
    public void testMatchesJVM() {
        TestsHelper.pushVendorVersion(vendor, version);
        try {
            assertTrue(analyzer.getClass().getName() + " does not match its JVM", analyzer.matchesJVM());
        } finally {
            TestsHelper.popVendorVersion();
        }
    }

    /**
     * Test of matchesJVM method with correct version and wrong vendor.
     */
    public void testNotMatchesJVM1() {
        TestsHelper.pushVendorVersion("Dummy vendor", version);
        try {
            assertFalse(analyzer.getClass().getName() + " matches a JVM with wrong vendor", analyzer.matchesJVM());
        } finally {
            TestsHelper.popVendorVersion();
        }
    }

    /**
     * Test of matchesJVM method with correct vendor and wrong version.
     */
    public void testNotMatchesJVM2() {
        TestsHelper.pushVendorVersion(vendor, "Dummy version");
        try {
            assertFalse(analyzer.getClass().getName() + " matches a JVM with wrong version", analyzer.matchesJVM());
        } finally {
            TestsHelper.popVendorVersion();
        }
    }

    /**
     * Test of getFieldSize method: checks the field size for all primitive types,
     * and Object and String types. The expected values are the real one.
     * @throws NoSuchMethodException If the current <i>analyzer</i> does not have a getFieldSize()
     * method
     * @throws IllegalAccessException If the getFieldSize() method of the current <i>analyzer</i>
     * is not public
     * @throws InvocationTargetException If the getFieldSize() method throws an unexpected exception
     */
    public void testGetFieldSize() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (!testFieldSizes) {
            return;
        }
        assertFieldSize(analyzer, Double.TYPE, 8);
        assertFieldSize(analyzer, Long.TYPE, 8);
        assertFieldSize(analyzer, Integer.TYPE, 4);
        assertFieldSize(analyzer, Float.TYPE, 4);
        assertFieldSize(analyzer, Short.TYPE, 2);
        assertFieldSize(analyzer, Character.TYPE, 2);
        assertFieldSize(analyzer, Byte.TYPE, 1);
        assertFieldSize(analyzer, Boolean.TYPE, 1);
        assertFieldSize(analyzer, Object.class, 4);
        assertFieldSize(analyzer, String.class, 4);
    }

    /**
     * Failure test for the getFieldSize() method, with <code>null</code> parameter.
     * @throws NoSuchMethodException If the current <i>analyzer</i> does not have a getFieldSize()
     * method
     * @throws IllegalAccessException If the getFieldSize() method of the current <i>analyzer</i>
     * is not public
     * @throws InvocationTargetException If the getFieldSize() method throws an unexpected exception
     */
    public void testGetFieldSizeFail() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (!testFieldSizes) {
            return;
        }
        assertFieldSize(analyzer, null, 0);
    }

    /**
     * Helper method to call the getFieldSize() method. A TestCase assertion will fail if
     * the method does not return the expected value.
     * @param analyzer The MemoryUsageAnalyzer which should export a public getFieldSize() method to test.
     * @param fieldClass The parameter to call the getFieldSize() method with. Can be <code>null</code>,
     * in which case an IllegalArgumentException is expected.
     * @param expectedValue The value expected to be returned from the method.
     * @throws NoSuchMethodException If the current <i>analyzer</i> does not have a getFieldSize()
     * method
     * @throws IllegalAccessException If the getFieldSize() method of the current <i>analyzer</i>
     * is not public
     * @throws InvocationTargetException If the getFieldSize() method throws an unexpected exception
     */
    private static void assertFieldSize(MemoryUsageAnalyzer analyzer, Class fieldClass, int expectedValue)
        throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method m = analyzer.getClass().getDeclaredMethod("getFieldSize", new Class[] {Class.class});
        try {
            Integer retValue = (Integer) m.invoke(analyzer, new Object[] {fieldClass });
            assertEquals("getFieldSize reported wrong value on type " + fieldClass.getName(),
                    expectedValue, retValue.intValue());
        } catch (InvocationTargetException ex) {
            if ((fieldClass != null) || (!(ex.getTargetException() instanceof IllegalArgumentException))) {
                throw ex;
            }
        }
    }

    /**
     * Test of getObjectBase method.
     * @throws NoSuchMethodException If the current <i>analyzer</i> does not have a getObjectBase()
     * method
     * @throws IllegalAccessException If the getObjectBase() method of the current <i>analyzer</i>
     * is not public
     * @throws InvocationTargetException If the getObjectBase() method throws an unexpected exception
     */
    public void testGetObjectBase() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (objectBase < 0) {
            return;
        }
        Method m = analyzer.getClass().getDeclaredMethod("getObjectBase", new Class[0]);
        Integer retValue = (Integer) m.invoke(analyzer, new Object[0]);
        assertEquals("Wrong objectBase value found", objectBase, retValue.intValue());
    }
}
