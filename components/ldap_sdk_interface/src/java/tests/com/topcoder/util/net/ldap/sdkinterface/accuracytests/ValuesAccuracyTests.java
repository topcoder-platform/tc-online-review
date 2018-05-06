/**
 *
 * Copyright ?2004, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.net.ldap.sdkinterface.accuracytests;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import com.topcoder.util.net.ldap.sdkinterface.Values;

/**
 * Tests the various methods in the Values class
 *
 * @author BryanChen
 * @version 1.0
 */
public class ValuesAccuracyTests extends TestCase {
    /** Instance variable to use in testing */
    private Values values;

    /**
     * Sets the environment up for testing, just instantiates the values variable
     */
    protected void setUp() {
        values = new Values();
    }

    /**
     * Tests the accuracy of the various add methods
     */
    public void testValuesAddAccuracy() {
        values.add("hi");
        assertEquals("incorrect size", 1, values.getTextValues().size());
        assertEquals("incorrect element", "hi", values.getTextValues().get(0));

        values.add(new String[]{"bye", "hello"});
        assertEquals("incorrect element", "bye", values.getTextValues().get(1));
        assertEquals("incorrect element", "hello", values.getTextValues().get(2));

        byte[] bytes = new byte[]{5, 6, 7, 8};
        values.add(bytes);
        assertNotNull("shouldn't be null", values.getBinaryValues().get(0));

        byte[] bytes2 = (byte[]) values.getBinaryValues().get(0);
        assertEquals("incorrect bytes size", bytes.length, bytes2.length);
        for (int i = 0; i < bytes.length; i++) {
            assertEquals("incorrect element", bytes[i], bytes2[i]);
        }
    }

    /**
     * Tests the accuracy of the various delete methods
     */
    public void testValuesDeleteAccuracy() {
        values.add("hi");
        values.add(new String[]{"bye", "hello"});
        assertEquals(3, values.getTextValues().size());

        assertTrue(values.delete("hi"));
        assertTrue(values.delete("hello"));
        assertTrue(values.delete("bye"));
        assertEquals(0, values.getTextValues().size());
    }

    /**
     * Tests the accuracy of the various set methods
     */
    public void testValuesSetArraysAccuracy() {
        values.setTextValues(new String[]{"hi", "bye", "hello"});
        assertEquals(3, values.getTextValues().size());

        values.setTextValues(new String[]{"hi", "bye"});
        assertEquals(2, values.getTextValues().size());
        assertEquals("hi", values.getTextValues().get(0));
        assertEquals("bye", values.getTextValues().get(1));

        values.setBinaryValues(new byte[][]{new byte[]{3}, new byte[]{5}});
        assertEquals(2, values.getBinaryValues().size());

        values.setBinaryValues(new byte[][]{new byte[]{4, 5, 6}});
        assertEquals(1, values.getBinaryValues().size());
    }

    /**
     * Returns the suite of tests
     * @return the suite of tests
     */
    public static Test suite() {
        return new TestSuite(ValuesAccuracyTests.class);
    }
}
