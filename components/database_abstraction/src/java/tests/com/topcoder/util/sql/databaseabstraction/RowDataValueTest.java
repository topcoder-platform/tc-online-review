/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction;

import junit.framework.TestCase;

/**
 * Test case for RowDataValue.
 *
 * @author justforplay
 * @version 1.1
 * @since 1.1
 */
public class RowDataValueTest extends TestCase {

    /**
     * Instance of RowDataValue for unit test.
     */
    private RowDataValue dataValue;

    /**
     * Initialize dataValue.
     */
    public void setUp() {
        dataValue = new RowDataValue("TEST");
    }

    /**
     * <p>
     * Test constructor RowDataValue(Object).
     * </p>
     * <p>
     * Verify:RowDataValue can be instantiated correctly.
     * </p>
     */
    public void testRowDataValue1() {
        assertNotNull("Unable to instantiate RowDataValue.", dataValue);
        assertEquals("RowDataValue is not initialized correctly.", dataValue.getOriginalValue(), "TEST");
        assertEquals("RowDataValue is not initialized correctly.", dataValue.getMappedValue(), "TEST");
    }

    /**
     * <p>
     * Test constructor RowDataValue(Object).
     * </p>
     * <p>
     * Verify:RowDataValue can be instantiated correctly with null parameter.
     * </p>
     */
    public void testRowDataValue2() {
        RowDataValue dataValue2 = new RowDataValue(null);
        assertNotNull("Unable to instantiate RowDataValue.", dataValue2);
        assertNull("RowDataValue is not initialized correctly.", dataValue2.getOriginalValue());
        assertNull("RowDataValue is not initialized correctly.", dataValue2.getMappedValue());
    }

    /**
     * <p>
     * Test setMappedValue(Object).
     * </p>
     * <p>
     * Verify: setMappedValue is correct.
     * </p>
     */
    public void testSetMappedValue() {
        dataValue.setMappedValue("TEST_MAP");
        assertEquals("setMappedValue(Object) is incorrect.", dataValue.getMappedValue(), "TEST_MAP");
    }

    /**
     * <p>
     * Test getMappedValue().
     * </p>
     * <p>
     * Verify; mappedValue can be retrieved correctly.
     * </p>
     */
    public void testGetMappedValue() {
        assertEquals("mappedValue can't be retrieved correctly.", dataValue.getMappedValue(), "TEST");
    }

    /**
     * <p>
     * Test getOriginalValue().
     * </p>
     * <p>
     * Verify: originalValue can be retrieved correctly.
     * </p>
     */
    public void testGetOriginalValue() {
        assertEquals("originalValue can't be retrieved correctly.", dataValue.getOriginalValue(), "TEST");
    }

}
