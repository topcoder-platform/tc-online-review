/**
 * Copyright ?2004, TopCoder, Inc. All rights reserved
 */

package com.topcoder.util.file.accuracytests;

import com.topcoder.util.file.fieldconfig.Field;

import junit.framework.TestCase;

/**
 * <p>Test the Field class</p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class FieldAccuracyTests extends TestCase {
    /**
     * Field instance for test.
     */
    Field field = null;

    /**
     * Test the constructor.
     */
    public void testConstructor() {
        field = new Field("testName", "testVal", "testDesc", true);

        assertEquals("testName", field.getName());
        assertEquals("testVal", field.getValue());
        assertEquals("testDesc", field.getDescription());
        assertTrue("test read only", field.isReadOnly());
        field = new Field("testName2", "testVal2", "testDesc2", false);

        assertEquals("testName2", field.getName());
        assertEquals("testVal2", field.getValue());
        assertEquals("testDesc2", field.getDescription());
        assertFalse("test read only", field.isReadOnly());
    }

    /**
     * Tests setting value.
     */
    public void testSetValue() {
        field = new Field("testName3", "testVal3", "testDesc3", false);

        assertEquals("testVal3", field.getValue());
        field.setValue("testVal4");
        assertEquals("testVal4", field.getValue());
    }
}