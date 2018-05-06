/*
 * TCS LDAP SDK Interface 1.0
 *
 * ModificationTest.java
 *
 * Copyright (c), 2004. TopCoder, Inc. All rights reserved.
 *
 */
package com.topcoder.util.net.ldap.sdkinterface;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * A test case testing the behavior of <code>Modification</code> class. No special settings are required to run this
 * test.
 *
 * @author  isv
 * @version 1.0 05/18/2004
 */
public class ModificationTest extends TestCase {

    public ModificationTest(String testName) {
        super(testName);
    }

    /**
     * Tests the <code>Modification(int, String)</code> constructor. Verifies that the constructor initializes the
     * private variables correctly. Also tests the imvalid arguments hadling.
     */
    public void testConstructor_int_String() {
        Modification modification = null;

        modification = new Modification(Modification.ADD, "attribute name");
        assertEquals("The type should be set as specified", Modification.ADD, modification.getType());
        assertEquals("The attribute name should be set as specified", "attribute name",
                modification.getAttributeName());
        assertNull("The attribute values should not be initialized", modification.getValues());

        modification = new Modification(Modification.REPLACE, "attribute name");
        assertEquals("The type should be set as specified", Modification.REPLACE, modification.getType());
        assertEquals("The attribute name should be set as specified", "attribute name",
                modification.getAttributeName());
        assertNull("The attribute values should not be initialized", modification.getValues());

        modification = new Modification(Modification.DELETE_ATTRIBUTE, "attribute name");
        assertEquals("The type should be set as specified", Modification.DELETE_ATTRIBUTE, modification.getType());
        assertEquals("The attribute name should be set as specified", "attribute name",
                modification.getAttributeName());
        assertNull("The attribute values should not be initialized", modification.getValues());

        modification = new Modification(Modification.DELETE_VALUE, "attribute name");
        assertEquals("The type should be set as specified", Modification.DELETE_VALUE, modification.getType());
        assertEquals("The attribute name should be set as specified", "attribute name",
                modification.getAttributeName());
        assertNull("The attribute values should not be initialized", modification.getValues());

        modification = new Modification(Modification.ADD, "another attribute name");
        assertEquals("The type should be set as specified", Modification.ADD, modification.getType());
        assertEquals("The attribute name should be set as specified", "another attribute name",
                modification.getAttributeName());
        assertNull("The attribute values should not be initialized", modification.getValues());

        modification = new Modification(Modification.REPLACE, "another attribute name");
        assertEquals("The type should be set as specified", Modification.REPLACE, modification.getType());
        assertEquals("The attribute name should be set as specified", "another attribute name",
                modification.getAttributeName());
        assertNull("The attribute values should not be initialized", modification.getValues());

        modification = new Modification(Modification.DELETE_ATTRIBUTE, "another attribute name");
        assertEquals("The type should be set as specified", Modification.DELETE_ATTRIBUTE, modification.getType());
        assertEquals("The attribute name should be set as specified", "another attribute name",
                modification.getAttributeName());
        assertNull("The attribute values should not be initialized", modification.getValues());

        modification = new Modification(Modification.DELETE_VALUE, "another attribute name");
        assertEquals("The type should be set as specified", Modification.DELETE_VALUE, modification.getType());
        assertEquals("The attribute name should be set as specified", "another attribute name",
                modification.getAttributeName());
        assertNull("The attribute values should not be initialized", modification.getValues());

        // Tests the invalid arguments handling
        try {
            modification = new Modification(Modification.ADD, (String) null);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}

        try {
            modification = new Modification(Modification.REPLACE, (String) null);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}

        try {
            modification = new Modification(Modification.DELETE_ATTRIBUTE, (String) null);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}

        try {
            modification = new Modification(Modification.DELETE_VALUE, (String) null);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}

        try {
            modification = new Modification(Modification.ADD, "");
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            modification = new Modification(Modification.REPLACE, "");
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            modification = new Modification(Modification.DELETE_VALUE, "");
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            modification = new Modification(Modification.DELETE_ATTRIBUTE, "");
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            modification = new Modification(Modification.ADD, "         ");
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            modification = new Modification(Modification.REPLACE, "          ");
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            modification = new Modification(Modification.DELETE_VALUE, "         ");
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            modification = new Modification(Modification.DELETE_ATTRIBUTE, "       ");
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            modification = new Modification(Modification.ADD + Modification.REPLACE, "attribute name");
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            modification = new Modification(-1, "attribute name");
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}
    }

    /**
     * Tests the <code>Modification(int, String, Values)</code> constructor. Verifies that the constructor initializes
     * the private variables correctly. Also tests the imvalid arguments hadling.
     */
    public void testConstructor_int_String_Values() {
        Modification modification = null;
        Values values = new Values("Sample values");

        modification = new Modification(Modification.ADD, "attribute name", values);
        assertEquals("The type should be set as specified", Modification.ADD, modification.getType());
        assertEquals("The attribute name should be set as specified", "attribute name",
                modification.getAttributeName());
        assertNotNull("The attribute values should not be initialized", modification.getValues());
        assertEquals("The attribute name should be set as specified", values, modification.getValues());

        modification = new Modification(Modification.REPLACE, "attribute name", values);
        assertEquals("The type should be set as specified", Modification.REPLACE, modification.getType());
        assertEquals("The attribute name should be set as specified", "attribute name",
                modification.getAttributeName());
        assertNotNull("The attribute values should not be initialized", modification.getValues());
        assertEquals("The attribute name should be set as specified", values, modification.getValues());

        modification = new Modification(Modification.DELETE_ATTRIBUTE, "attribute name", values);
        assertEquals("The type should be set as specified", Modification.DELETE_ATTRIBUTE, modification.getType());
        assertEquals("The attribute name should be set as specified", "attribute name",
                modification.getAttributeName());
        assertNotNull("The attribute values should not be initialized", modification.getValues());
        assertEquals("The attribute name should be set as specified", values, modification.getValues());

        modification = new Modification(Modification.DELETE_VALUE, "attribute name", values);
        assertEquals("The type should be set as specified", Modification.DELETE_VALUE, modification.getType());
        assertEquals("The attribute name should be set as specified", "attribute name",
                modification.getAttributeName());
        assertNotNull("The attribute values should not be initialized", modification.getValues());
        assertEquals("The attribute name should be set as specified", values, modification.getValues());

        modification = new Modification(Modification.ADD, "another attribute name", values);
        assertEquals("The type should be set as specified", Modification.ADD, modification.getType());
        assertEquals("The attribute name should be set as specified", "another attribute name",
                modification.getAttributeName());
        assertNotNull("The attribute values should not be initialized", modification.getValues());
        assertEquals("The attribute name should be set as specified", values, modification.getValues());

        modification = new Modification(Modification.REPLACE, "another attribute name", values);
        assertEquals("The type should be set as specified", Modification.REPLACE, modification.getType());
        assertEquals("The attribute name should be set as specified", "another attribute name",
                modification.getAttributeName());
        assertNotNull("The attribute values should not be initialized", modification.getValues());
        assertEquals("The attribute name should be set as specified", values, modification.getValues());

        modification = new Modification(Modification.DELETE_ATTRIBUTE, "another attribute name", values);
        assertEquals("The type should be set as specified", Modification.DELETE_ATTRIBUTE, modification.getType());
        assertEquals("The attribute name should be set as specified", "another attribute name",
                modification.getAttributeName());
        assertNotNull("The attribute values should not be initialized", modification.getValues());
        assertEquals("The attribute name should be set as specified", values, modification.getValues());

        modification = new Modification(Modification.DELETE_VALUE, "another attribute name", values);
        assertEquals("The type should be set as specified", Modification.DELETE_VALUE, modification.getType());
        assertEquals("The attribute name should be set as specified", "another attribute name",
                modification.getAttributeName());
        assertNotNull("The attribute values should not be initialized", modification.getValues());
        assertEquals("The attribute name should be set as specified", values, modification.getValues());

        // Tests the invalid arguments handling
        try {
            modification = new Modification(Modification.ADD, (String) null, values);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}

        try {
            modification = new Modification(Modification.REPLACE, (String) null, values);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}

        try {
            modification = new Modification(Modification.DELETE_ATTRIBUTE, (String) null, values);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}

        try {
            modification = new Modification(Modification.DELETE_VALUE, (String) null, values);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}

        try {
            modification = new Modification(Modification.ADD, "", values);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            modification = new Modification(Modification.REPLACE, "", values);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            modification = new Modification(Modification.DELETE_VALUE, "", values);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            modification = new Modification(Modification.DELETE_ATTRIBUTE, "", values);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            modification = new Modification(Modification.ADD, "         ", values);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            modification = new Modification(Modification.REPLACE, "          ", values);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            modification = new Modification(Modification.DELETE_VALUE, "         ", values);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            modification = new Modification(Modification.DELETE_ATTRIBUTE, "       ", values);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            modification = new Modification(Modification.ADD + Modification.REPLACE, "attribute name", values);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            modification = new Modification(-1, "attribute name", values);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}
    }

    /**
     * Tests the <code>getValues()</code> method. Simply verifies that method returns the same instance that was
     * specified at construction each time the method is called. Also verifies that the method returns an original
     * <code>Values</code> object but not a copy.
     */
    public void testMethodGetValues() {
        Modification modification = null;
        Values values = null;
        byte[] bytes = new byte[] {1, 2, 3, 4, 5};

        values = new Values("Sample values");

        modification = new Modification(Modification.ADD, "attribute name", values);

        assertNotNull("The nethod should never return null after the object was constructed with Values object",
                modification.getValues());
        assertTrue("The method should return the same instance of Values as specified at construction time",
                values == modification.getValues());
        assertTrue("The method should return the same instance of Values as specified at construction time",
                values == modification.getValues());

        values = modification.getValues();
        values.setTextValues(new String[] {"new attribute name"});
        values.setBinaryValues(new byte[][] {bytes});
        assertEquals("The method should not return a copy of original Values object",
                values.getTextValues().get(0), modification.getValues().getTextValues().get(0));
        assertEquals("The method should not return a copy of original Values object",
                values.getBinaryValues().get(0), modification.getValues().getBinaryValues().get(0));
    }

    /**
     * Tests the <code>getAttributeName()</code> method. Simply verifies that method returns the same attribute name as
     * specified at construction each time the method is called.
     */
    public void testMethodGetAttributeName() {
        Modification modification = null;

        modification = new Modification(Modification.ADD, "attribute name");
        assertEquals("The method should return the same attribute name as specified at construction",
                "attribute name", modification.getAttributeName());
        assertEquals("The method should return the same attribute name as specified at construction",
                "attribute name", modification.getAttributeName());

        modification = new Modification(Modification.ADD, "another attribute name");
        assertEquals("The method should return the same attribute name as specified at construction",
                "another attribute name", modification.getAttributeName());
        assertEquals("The method should return the same attribute name as specified at construction",
                "another attribute name", modification.getAttributeName());

        modification = new Modification(Modification.ADD, "new attribute name");
        assertEquals("The method should return the same attribute name as specified at construction",
                "new attribute name", modification.getAttributeName());
        assertEquals("The method should return the same attribute name as specified at construction",
                "new attribute name", modification.getAttributeName());
    }

    /**
     * Tests the <code>getType()</code> method. Simply verifies that method returns the same type as specified at
     * construction each time the method is called.
     */
    public void testMethodGetType() {
        Modification modification = null;

        modification = new Modification(Modification.ADD, "attribute name");
        assertEquals("The method should return the same modification type as specified at construction",
                Modification.ADD, modification.getType());
        assertEquals("The method should return the same modification type as specified at construction",
                Modification.ADD, modification.getType());

        modification = new Modification(Modification.REPLACE, "attribute name");
        assertEquals("The method should return the same modification type as specified at construction",
                Modification.REPLACE, modification.getType());
        assertEquals("The method should return the same modification type as specified at construction",
                Modification.REPLACE, modification.getType());

        modification = new Modification(Modification.DELETE_ATTRIBUTE, "attribute name");
        assertEquals("The method should return the same modification type as specified at construction",
                Modification.DELETE_ATTRIBUTE, modification.getType());
        assertEquals("The method should return the same modification type as specified at construction",
                Modification.DELETE_ATTRIBUTE, modification.getType());

        modification = new Modification(Modification.DELETE_VALUE, "attribute name");
        assertEquals("The method should return the same modification type as specified at construction",
                Modification.DELETE_VALUE, modification.getType());
        assertEquals("The method should return the same modification type as specified at construction",
                Modification.DELETE_VALUE, modification.getType());
    }

    public static Test suite() {
        return new TestSuite(ModificationTest.class);
    }
}
