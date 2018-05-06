/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.memoryusage;

import com.topcoder.util.memoryusage.MemoryUsageHelper.FieldsListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests of MemoryUsageHelper.
 *
 * @author TexWiller
 * @version 2.0
 */
public class MemoryUsageHelperTest extends TestCase {

    /**
     * Standard TestCase constructor: creates a new MemoryUsageHelperTest object.
     *
     * @param testName The name to be given to this TestCase.
     */
    public MemoryUsageHelperTest(String testName) {
        super(testName);
    }

    /**
     * Sets up the fixture.
     */
    protected void setUp() {
    }

    /**
     * Tears down the fixture.
     */
    protected void tearDown() {
        while (!TestsHelper.isVVStackEmpty()) {
            TestsHelper.popVendorVersion();
        }
    }

    /**
     * Static method expected by JUnit test runners. Returns a Test containing all
     * the testXXX methods in this class.
     *
     * @return A new Test, containing all the testXXX methods in this class.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(MemoryUsageHelperTest.class);
        return suite;
    }

    /**
     * Test of getFieldSize() method. Checks that all the primitive types
     * return their expected value. Checks also for Object type to return 4.
     */
    public void testGetFieldSize() {
        assertEquals("Wrong double type size", MemoryUsageHelper.getFieldSize(Double.TYPE), 8);
        assertEquals("Wrong long type size", MemoryUsageHelper.getFieldSize(Long.TYPE), 8);
        assertEquals("Wrong int type size", MemoryUsageHelper.getFieldSize(Integer.TYPE), 4);
        assertEquals("Wrong float type size", MemoryUsageHelper.getFieldSize(Float.TYPE), 4);
        assertEquals("Wrong short type size", MemoryUsageHelper.getFieldSize(Short.TYPE), 2);
        assertEquals("Wrong char type size", MemoryUsageHelper.getFieldSize(Character.TYPE), 2);
        assertEquals("Wrong boolean type size", MemoryUsageHelper.getFieldSize(Boolean.TYPE), 1);
        assertEquals("Wrong byte type size", MemoryUsageHelper.getFieldSize(Byte.TYPE), 1);
        assertEquals("Wrong Object type size", MemoryUsageHelper.getFieldSize(Object.class), 4);
    }

    /**
     * Test of getFieldSize() method. Should throw an IllegalArgumentException
     * when the parameter is null.
     * Expected exception: IllegalArgumentException.
     */
    public void testGetFieldSizeErr() {
        try {
            MemoryUsageHelper.getFieldSize(null);
            fail("Should have thrown IllegalArgumentException on null parameter");
        } catch (IllegalArgumentException ex) {
            // Test passed
        }
    }

    /**
     * Test of checkVendorVersion method. Sets a custom vendor and version,
     * and checks if the method reports them correctly.
     */
    public void testCheckVendorVersion1() {
        TestsHelper.pushVendorVersion("---- Test vendor ----", "**** Test version ****");
        assertTrue("checkVendorVersion did not recognize correct vendor and version",
                MemoryUsageHelper.checkVendorVersion("Test vendor", "Test version"));
        TestsHelper.popVendorVersion();
    }

    /**
     * Test of checkVendorVersion method. Sets a custom vendor and version,
     * and checks if the method detects the vendor is wrong.
     */
    public void testCheckVendorVersion2() {
        TestsHelper.pushVendorVersion("---- Test vendor ----", "**** Test version ****");
        assertFalse("checkVendorVersion did not recognize incorrect vendor",
                MemoryUsageHelper.checkVendorVersion("WRONG", "Test version"));
        TestsHelper.popVendorVersion();
    }

    /**
     * Test of checkVendorVersion method. Sets a custom vendor and version,
     * and checks if the method detects the version is wrong.
     */
    public void testCheckVendorVersion3() {
        TestsHelper.pushVendorVersion("---- Test vendor ----", "**** Test version ****");
        assertFalse("checkVendorVersion did not recognize incorrect version",
                MemoryUsageHelper.checkVendorVersion("Test vendor", "WRONG"));
        TestsHelper.popVendorVersion();
    }

    /**
     * Test of checkVendorVersion method: should throw an IllegalArgumentException
     * when the first argument is <code>null</code>.
     */
    public void testCheckVendorVersionErr1() {
        try {
            MemoryUsageHelper.checkVendorVersion(null, "Test");
            fail("Should have thrown IllegalArgumentException on null first argument");
        } catch (IllegalArgumentException ex) {
            // Test passed
        }
    }

    /**
     * Test of checkVendorVersion method: should throw an IllegalArgumentException
     * when the second argument is <code>null</code>.
     */
    public void testCheckVendorVersionErr2() {
        try {
            MemoryUsageHelper.checkVendorVersion("Test", null);
            fail("Should have thrown IllegalArgumentException on null second argument");
        } catch (IllegalArgumentException ex) {
            // Test passed
        }
    }

    /**
     * Test of checkVendorVersion method: should throw an IllegalArgumentException
     * when the first argument is empty.
     */
    public void testCheckVendorVersionErr3() {
        try {
            MemoryUsageHelper.checkVendorVersion(" ", "Test");
            fail("Should have thrown IllegalArgumentException on empty first argument");
        } catch (IllegalArgumentException ex) {
            // Test passed
        }
    }

    /**
     * Test of checkVendorVersion method: should throw an IllegalArgumentException
     * when the second argument is empty.
     */
    public void testCheckVendorVersionErr4() {
        try {
            MemoryUsageHelper.checkVendorVersion("Test", "   ");
            fail("Should have thrown IllegalArgumentException on empty second argument");
        } catch (IllegalArgumentException ex) {
            // Test passed
        }
    }

    /**
     * Test of getEmbeddedObjects method. Tests mainly for the FieldsListener
     * to work properly.
     * @throws MemoryUsageException If protection errors occur: should not happen.
     */
    public void testGetEmbeddedObjects1() throws MemoryUsageException {
        final List fields = new ArrayList();
        FieldsListener listener = new FieldsListener() {
            public void fieldReached(Field field) {
                fields.add(field);
            }
        };
        TestsHelper.ClassA classA = new TestsHelper.ClassA();
        Object[] embedded = MemoryUsageHelper.getEmbeddedObjects(classA, listener);
        TestsHelper.listsEqual(fields, TestsHelper.ClassA.getFields(), false);
        TestsHelper.listsEqual(Arrays.asList(embedded), classA.getEmbeddedShallow(), true);
    }

    /**
     * Test of getEmbeddedObjects method. Tests for proper recognition of inheritance.
     * @throws MemoryUsageException If protection errors occur: should not happen.
     */
    public void testGetEmbeddedObjects2() throws MemoryUsageException {
        final List fields = new ArrayList();
        FieldsListener listener = new FieldsListener() {
            public void fieldReached(Field field) {
                fields.add(field);
            }
        };
        TestsHelper.ClassB classB = new TestsHelper.ClassB();
        Object[] embedded = MemoryUsageHelper.getEmbeddedObjects(classB, listener);
        TestsHelper.listsEqual(fields, TestsHelper.ClassB.getFields(), false);
        TestsHelper.listsEqual(Arrays.asList(embedded), classB.getEmbeddedShallow(), true);
    }

    /**
     * Test of checkObjectNotNull() method, with regular object.
     */
    public void testCheckObjectNotNull1() {
        try {
            MemoryUsageHelper.checkObjectNotNull(new Object(), "object");
        } catch (IllegalArgumentException ex) {
            fail("Should not throw an IllegalArgumentException with non-null object");
        }
    }

    /**
     * Test of checkObjectNotNull() method, with null object.
     * Expected exception: IllegalArgumentException.
     */
    public void testCheckObjectNotNull2() {
        try {
            MemoryUsageHelper.checkObjectNotNull(null, "object");
            fail("Should have thrown an IllegalArgumentException with null object");
        } catch (IllegalArgumentException ex) {
            // Test passed
        }
    }

    /**
     * Test of checkStringNotNullOrEmpty() method, with regular string.
     */
    public void testCheckStringNotNullOrEmpty1() {
        try {
            MemoryUsageHelper.checkStringNotNullOrEmpty("full", "string");
        } catch (IllegalArgumentException ex) {
            fail("Should not throw an IllegalArgumentException with a proper string");
        }
    }

    /**
     * Test of checkStringNotNullOrEmpty() method, with null string.
     * Expected exception: IllegalArgumentException.
     */
    public void testCheckStringNotNullOrEmpty2() {
        try {
            MemoryUsageHelper.checkStringNotNullOrEmpty(null, "string");
            fail("Should have thrown an IllegalArgumentException with null string");
        } catch (IllegalArgumentException ex) {
            // Test passed
        }
    }

    /**
     * Test of checkStringNotNullOrEmpty() method, with empty string.
     * Expected exception: IllegalArgumentException.
     */
    public void testCheckStringNotNullOrEmpty3() {
        try {
            MemoryUsageHelper.checkStringNotNullOrEmpty(" ", "string");
            fail("Should have thrown an IllegalArgumentException with empty string");
        } catch (IllegalArgumentException ex) {
            // Test passed
        }
    }

    /**
     * Test of checkArrayContentNotNull() method, with a <code>null</code> array.
     * Expected exception: IllegalArgumentException.
     */
    public void testCheckArrayContentNotNull1() {
        try {
            MemoryUsageHelper.checkArrayContentNotNull(null, "name");
            fail("Should have thrown an IllegalArgumentException on a null array");
        } catch (IllegalArgumentException ex) {
            // Test passed
        }
    }

    /**
     * Test of checkArrayContentNotNull() method, with an array
     * with one <code>null</code> element.
     * Expected exception: IllegalArgumentException.
     */
    public void testCheckArrayContentNotNull2() {
        try {
            MemoryUsageHelper.checkArrayContentNotNull(new Object[] {new Object(), null, this }, "name");
            fail("Should have thrown an IllegalArgumentException on an array with a null element");
        } catch (IllegalArgumentException ex) {
            // Test passed
        }
    }

    /**
     * Test of isStatic method. Checks a static field is reported as such,
     * and a non-static field is not reported as static.
     * @throws NoSuchFieldException If the field does not exist; should not occur
     */
    public void testIsStatic() throws NoSuchFieldException {
        Field field = TestsHelper.ClassA.class.getDeclaredField("STATIC");
        assertTrue("Static field not reported as such", MemoryUsageHelper.isStatic(field));
        field = TestsHelper.ClassA.class.getDeclaredField("intA1");
        assertFalse("Not static field reported as such", MemoryUsageHelper.isStatic(field));
    }

}
