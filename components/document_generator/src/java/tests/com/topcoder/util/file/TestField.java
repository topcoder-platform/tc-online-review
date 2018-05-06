/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file;

import com.topcoder.util.file.fieldconfig.Field;
import junit.framework.TestCase;

/**
 * <p>
 * Title: TestField
 * </p>
 *
 * <p>
 * Description: Test whole <code>Field</code> class.
 * </p>
 *
 * <p>
 * Company: TopCoder Software
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class TestField extends TestCase {
    /** Mutable field. */
    private Field fieldMutable = null;

    /** Read-only field. */
    private Field fieldReadOnly = new Field("name", "value", "description", true);

    /**
     * Create new mutable field.
     */
    public void setUp() {
        // Create mutable field.
        fieldMutable = new Field("name", "value", "description", false);
    }

    /**
     * Test {@link Field#Field(String, String, String, boolean)} constructor on
     * good data.
     */
    public void testConstructor() {
        // Create mutable field.
        fieldMutable = new Field("some_name", "some_value", "some_description", false);

        // Check the properties of field.
        assertEquals("The name property is incorrect.", fieldMutable.getName(), "some_name");
        assertEquals("The value property is incorrect.", fieldMutable.getValue(), "some_value");
        assertEquals("The description property is incorrect.", fieldMutable.getDescription(), "some_description");
        assertFalse("The read-only property is incorrect.", fieldMutable.isReadOnly());
    }

    /**
     * Test {@link Field#getName()} method.
     */
    public void testGetName() {
        // Check name property.
        assertEquals("The name property is incorrect.", fieldReadOnly.getName(), "name");
    }

    /**
     * Test {@link Field#getValue()} method.
     */
    public void testGetValue() {
        // Check name property.
        assertEquals("The value property is incorrect.", fieldReadOnly.getValue(), "value");
    }

    /**
     * Test {@link Field#setValue(String)} method on good data.
     */
    public void testSetValue() {
        // Set new value.
        fieldMutable.setValue("new_value");

        // Check new value.
        assertEquals("The value property is incorrect.", fieldMutable.getValue(), "new_value");
    }

    /**
     * Test {@link Field#setValue(String)} method with <code>null</code> as
     * argument.
     */
    public void testSetValueWithNull() {
        try {
            // Try to set value to null.
            fieldMutable.setValue(null);

            // Fail.
            fail("Should throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * Test {@link Field#setValue(String)} method, when field is read-only.
     */
    public void testSetValueWhithReadOnly() {
        try {
            // Try to set new value, when field is read-only.
            fieldReadOnly.setValue("new_value");

            // Fail.
            fail("Should throw IllegalStatetException.");
        } catch (IllegalStateException e) {
            // Success.
        }
    }

    /**
     * Test {@link Field#getDescription()} method.
     */
    public void testGetDescription() {
        assertEquals("The description property is incorrect.", fieldMutable.getDescription(), "description");
    }

    /**
     * <p>
     * Tests Field#isReadOnly() for accuracy.
     * </p>
     *
     * <p>
     * It verifies Field#isReadOnly() is correct.
     * </p>
     *
     * @since 2.1
     */
    public void testisReadOnly() {
        assertTrue("Failed to return the value correctly.", fieldReadOnly.isReadOnly());
    }
}
