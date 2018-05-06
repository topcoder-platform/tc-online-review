/*
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.cache;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Provides unit test cases for SerializableObjectByteConverter class.
 *
 * @author  rem
 * @version 2.0
 * @since   2.0
 */
public class SerializableObjectByteConverterTestCase extends TestCase {

    /**
     * Instance to test.
     */
    private SerializableObjectByteConverter converter;

    /**
     * Returns the test suite for this class.
     *
     * @return a new TestSuite representing this class.
     */
    public static Test suite() {
        return new TestSuite(SerializableObjectByteConverterTestCase.class);
    }

    /**
     * Set up.
     *
     * @throws Exception if any error occurs during setup.
     */
    public void setUp() throws Exception {
        super.setUp();
        converter = new SerializableObjectByteConverter();
    }

    /**
     * Test method getBytes with wrong input data.
     * <ul>
     * <li>Make sure IllegalArgumentException is thrown when argument doesn't implement Serializable interface.
     * </ul>
     */
    public void testGetBytesFail() {
        try {
            converter.getBytes(new Object());
            fail("IllegalArgumentException should be thrown when call getBytes() with not Serializable argument.");
        } catch (IllegalArgumentException ex) {
            // expected exception.
        }
    }

    /**
     * Test methods getBytes and getObject with correct input data.
     * <ul>
     * <li>Make sure that serialization and deserialization works correctly.
     * </ul>
     */
    public void testSerializeAndDeserialize() {
        byte[] byteArray = converter.getBytes("test string");
        assertEquals("test string", converter.getObject(byteArray));
    }

    /**
     * Several tests with null and empty arguments.
     * <ul>
     * <li>Make sure getBytes(null) returns empty array.
     * <li>Make sure getObject(null) returns null.
     * <li>Make sure getObject(empty array) returns null.
     * </ul>
     */
    public void testNull() {
        assertTrue("getBytes(null) should return empty array", converter.getBytes(null).length == 0);
        assertNull("getObject(null) should return null", converter.getObject(null));
        assertNull("getObject(empty array) should return null", converter.getObject(new byte[0]));
    }
}

