/*
 * TCS LDAP SDK Interface 1.0
 *
 * ValuesTest.java
 *
 * Copyright (c), 2004. TopCoder, Inc. All rights reserved.
 *
 */
package com.topcoder.util.net.ldap.sdkinterface;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * A test case testing the behavior of <code>Values</code> class. No special settings are required to run this test.
 *
 * @author  isv
 * @version 1.0 05/18/2004
 */
public class ValuesTest extends TestCase {

    public ValuesTest(String testName) {
        super(testName);
    }

    /**
     * Tests the non-argument constructor. Verifies that the binary and text values are initialized but empty.
     */
    public void testConstructor() {

        Values values = new Values();

        assertNotNull("The text values should be initialized", values.getTextValues());
        assertNotNull("The binary values should be initialized", values.getBinaryValues());
        assertEquals("The text values should be empty", 0, values.getTextValues().size());
        assertEquals("The binary values should be empty", 0, values.getBinaryValues().size());

    }

    /**
     * Tests the <code>Values(byte[][])</code> constructor. Verifies that the binary values are set with provided data
     * and text values are initialized but empty. Also tests the invalid arguments handling.
     */
    public void testConstructor_byte2DArray() {
        byte[] bytes1 = new byte[] {1, 2, 3, 4, 5};
        byte[] bytes2 = new byte[] {6, 7, 8, 9, 0};
        byte[][] bytes = new byte[][] {bytes1, bytes2};
        Values values = new Values(bytes);

        assertNotNull("The text values should be initialized", values.getTextValues());
        assertNotNull("The binary values should be initialized", values.getBinaryValues());
        assertEquals("The text values should be empty", 0, values.getTextValues().size());
        assertEquals("The binary values should be set to specified value", 2, values.getBinaryValues().size());
        assertEquals("The binary values should be set to specified value", bytes1, values.getBinaryValues().get(0));
        assertEquals("The binary values should be set to specified value", bytes2, values.getBinaryValues().get(1));
        assertTrue("The binary value should be properly initialized",
                areEqual(bytes1, (byte[]) values.getBinaryValues().get(0)));
        assertTrue("The binary value should be properly initialized",
                areEqual(bytes2, (byte[]) values.getBinaryValues().get(1)));

        // Tests the invalid arguments handling
        try {
            values = new Values((byte[][]) null);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}

        try {
            values = new Values(new byte[][] {bytes1, bytes2, null});
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}

    }

    /**
     * Tests the <code>Values(byte[])</code> constructor. Verifies that the binary values are set with provided data and
     * text values are initialized but empty. Also tests the invalid arguments handling.
     */
    public void testConstructor_byteArray() {
        byte[] bytes = new byte[] {1, 2, 3, 4, 5};
        Values values = new Values(bytes);

        assertNotNull("The text values should be initialized", values.getTextValues());
        assertNotNull("The binary values should be initialized", values.getBinaryValues());
        assertEquals("The text values should be empty", 0, values.getTextValues().size());
        assertEquals("The binary values should be set to specified value", 1, values.getBinaryValues().size());
        assertEquals("The binary values should be set to specified value", bytes, values.getBinaryValues().get(0));
        assertTrue("The binary value should be properly initialized",
                areEqual(bytes, (byte[]) values.getBinaryValues().get(0)));

        // Tests the invalid arguments handling
        try {
            values = new Values((byte[]) null);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}
    }

    /**
     * Tests the <code>Values(String[])</code> constructor. Verifies that the text values are set with provided data and
     * binary values are initialized but empty. Also tests the invalid arguments handling.
     */
    public void testConstructor_StringArray() {
        String[] value = new String[] {"String1", "String2"};
        Values values = new Values(value);

        assertNotNull("The text values should be initialized", values.getTextValues());
        assertNotNull("The binary values should be initialized", values.getBinaryValues());
        assertEquals("The binary values should be empty", 0, values.getBinaryValues().size());
        assertEquals("The text values should be set to specified value", 2, values.getTextValues().size());
        assertEquals("The text values should be set to specified value", value[0], values.getTextValues().get(0));
        assertEquals("The text values should be set to specified value", value[1], values.getTextValues().get(1));

        // Tests the invalid arguments handling
        try {
            values = new Values((String[]) null);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}

        try {
            values = new Values(new String[] {"String1", "String2", null});
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}
    }

    /**
     * Tests the <code>Values(String)</code> constructor. Verifies that the text values are set with provided data and
     * binary values are initialized but empty. Also tests the invalid arguments handling.
     */
    public void testConstructor_String() {
        String value = "String value";
        Values values = new Values(value);

        assertNotNull("The text values should be initialized", values.getTextValues());
        assertNotNull("The binary values should be initialized", values.getBinaryValues());
        assertEquals("The binary values should be empty", 0, values.getBinaryValues().size());
        assertEquals("The text values should be set to specified value", 1, values.getTextValues().size());
        assertEquals("The text values should be set to specified value", value, values.getTextValues().get(0));

        // Tests the invalid arguments handling
        try {
            values = new Values((String) null);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}
    }

    /**
     * Tests the <code>setBinaryValues(byte[][])</code> method. Verifies that existing binary values are replaced with
     * new ones while the text values remain unaffected. Also tests the invalid arguments handling.
     */
    public void testMethodSetBinaryValues_byte2DArray() {
        byte[] bytes1 = new byte[] {1, 1, 1};
        byte[] bytes2 = new byte[] {2, 2, 2};
        byte[] bytes3 = new byte[] {3, 3, 3};
        Values values = new Values();

        values.add("String1");
        values.add("String2");
        values.add(bytes1);

        values.setBinaryValues(new byte[][] {bytes2, bytes3});

        assertEquals("The text values should remain the same", 2,values.getTextValues().size());
        assertEquals("The text values should remain the same", "String1",values.getTextValues().get(0));
        assertEquals("The text values should remain the same", "String2",values.getTextValues().get(1));

        assertEquals("The binary values should be replaced with new values", 2,values.getBinaryValues().size());
        assertFalse("The previous binary data should be removed", values.getBinaryValues().contains(bytes1));
        assertEquals("The new binary data should be in effect", bytes2, values.getBinaryValues().get(0));
        assertEquals("The new binary data should be in effect", bytes3, values.getBinaryValues().get(1));
        assertTrue("The new binary data should be in effect",
                areEqual(bytes2, (byte[]) values.getBinaryValues().get(0)));
        assertTrue("The new binary data should be in effect",
                areEqual(bytes3, (byte[]) values.getBinaryValues().get(1)));

        // Tests the invalid arguments handling
        try {
            values.setBinaryValues((byte[][]) null);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}

        try {
            values.setBinaryValues(new byte[][] {bytes1, bytes2, null});
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}
    }

    /**
     * Tests the <code>clear()</code> method. Verifies that both text an dbinary values are cleared but not nullified.
     */
    public void testMethodClear() {
        Values values = new Values();

        values.add("String1");
        values.add("String2");
        values.add("String3");

        values.add(new byte[] {1, 2, 3});
        values.add(new byte[] {4, 5, 6});

        values.clear();

        assertNotNull("The text values should not be nullified", values.getTextValues());
        assertNotNull("The binary values should not be nullified", values.getBinaryValues());

        assertEquals("The text values should be empty", 0, values.getTextValues().size());
        assertEquals("The binary values should be empty", 0, values.getBinaryValues().size());
    }

    /**
     * Tests the <code>add(byte[][])</code> method. Verifies that specified data is added and the existing binary values
     * are not affected. Also tests the invalid arguments handling.
     */
    public void testMethodAdd_byte2DArray() {
        byte[] bytes1 = new byte[] {1, 1, 1};
        byte[] bytes2 = new byte[] {2, 2, 2};
        byte[] bytes3 = new byte[] {3, 3, 3};
        Values values = new Values(bytes1);

        values.add(new byte[][] {bytes2, bytes3});

        assertEquals("New binary values should be added", 3, values.getBinaryValues().size());
        assertTrue("New binary values should be added", values.getBinaryValues().contains(bytes2));
        assertTrue("New binary values should be added", values.getBinaryValues().contains(bytes3));
        assertTrue("The existing binary values should not be replaced", values.getBinaryValues().contains(bytes1));

        // Tests the invalid arguments handling
        try {
            values.add((byte[][]) null);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}

        try {
            values.add(new byte[][] {bytes1, null, bytes2, bytes3});
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}
    }

    /**
     * Tests the <code>add(byte[])</code> method. VErifies that specified data is added and the existing binary values
     * are not affected. Also tests the invalid arguments handling.
     */
    public void testMethodAdd_byteArray() {
        byte[] bytes1 = new byte[] {1, 1, 1};
        byte[] bytes2 = new byte[] {2, 2, 2};
        byte[] bytes3 = new byte[] {3, 3, 3};
        Values values = new Values(bytes1);

        values.add(bytes2);
        values.add(bytes3);

        assertEquals("New binary values should be added", 3, values.getBinaryValues().size());
        assertTrue("New binary values should be added", values.getBinaryValues().contains(bytes2));
        assertTrue("New binary values should be added", values.getBinaryValues().contains(bytes3));
        assertTrue("The existing binary values should not be replaced", values.getBinaryValues().contains(bytes1));

        // Tests the invalid arguments handling
        try {
            values.add((byte[]) null);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}
    }

    /**
     * Tests the <code>add(String[])</code> method. Verifies that specified data is added and the existing text values
     * are not affected. Also tests the invalid arguments handling.
     */
    public void testMethodAdd_StringArray() {

        Values values = new Values("String1");

        values.add(new String[] {"String2", "String3"});

        assertEquals("New text values should be added", 3, values.getTextValues().size());
        assertTrue("New text values should be added", values.getTextValues().contains("String2"));
        assertTrue("New text values should be added", values.getTextValues().contains("String3"));
        assertTrue("The existing text values should not be replaced", values.getTextValues().contains("String1"));

        // Tests the invalid arguments handling
        try {
            values.add((String[]) null);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}

        try {
            values.add(new String[] {"String4", null});
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}
    }


    /**
     * Tests the <code>add(String)</code> method. Verifies that specified data is added and the existing text values
     * are not affected. Also tests the invalid arguments handling.
     */
    public void testMethodAdd_String() {

        Values values = new Values("String1");

        values.add("String2");
        values.add("String3");

        assertEquals("New text values should be added", 3, values.getTextValues().size());
        assertTrue("New text values should be added", values.getTextValues().contains("String2"));
        assertTrue("New text values should be added", values.getTextValues().contains("String3"));
        assertTrue("The existing text values should not be replaced", values.getTextValues().contains("String1"));

        // Tests the invalid arguments handling
        try {
            values.add((String) null);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}
    }

    /**
     * Tests the <code>getBinaryValues()</code> method. Verifies that the method returns an original data but not a
     * copy. Also verifies that the method returns the actual binary values.
     */
    public void testMethodGetBinaryValues() {
        byte[] bytes1 = new byte[] {1, 2, 3};
        byte[] bytes2 = new byte[] {4, 5, 6};
        byte[] bytes3 = new byte[] {7, 8, 9};
        byte[] bytes4 = new byte[] {1, 1, 1};

        Values values = new Values(bytes1);
        values.add(bytes2);
        values.add(bytes3);

        assertEquals("The method should return the list of binary values of expected size",
                3, values.getBinaryValues().size());
        values.getBinaryValues().add(bytes4);
        assertEquals("The method should return the shallow copy of modifications of expected size but not an original "
                + "list", 3, values.getBinaryValues().size());
        assertEquals("The method should return an actual data", bytes1, values.getBinaryValues().get(0));
        assertEquals("The method should return an actual data", bytes2, values.getBinaryValues().get(1));
        assertEquals("The method should return an actual data", bytes3, values.getBinaryValues().get(2));
    }

    /**
     * Tests the <code>delete(byte[])</code> method. Verifies that existing byte arrays equal to specified one by values
     * are removed. Also tests the invalid arguments handling.
     */
    public void testMethodDelete_byteArray() {
        byte[] bytes1 = new byte[] {1, 2, 3};
        byte[] bytes2 = new byte[] {4, 5, 6};
        byte[] bytes3 = new byte[] {7, 8, 9};
        byte[] bytes4 = new byte[] {7, 8, 9};

        Values values = new Values("String1");

        values.add(bytes1);
        values.add(bytes2);
        values.add(bytes3);
        values.add(bytes4);

        assertTrue("The existing binary value should be deleted", values.delete(bytes1));
        assertFalse("The non-existing binary value should be ignored", values.delete(new byte[] {1}));
        assertEquals("The text values shouldn't be affected", 1, values.getTextValues().size());
        assertTrue("The existing binary value should be deleted", values.delete(bytes3));
        assertFalse("The method should return the binary values by values", values.getBinaryValues().contains(bytes3));
        assertFalse("The method should return the binary values by values", values.getBinaryValues().contains(bytes4));


        // Tests the invalid arguments handling
        try {
            values.delete((byte[]) null);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}
    }

    /**
     * Tests the <code>delete(String)</code> method. Verifies that existing String is removed. Also tests the invalid
     * arguments handling.
     */
    public void testMethodDelete_String() {
        Values values = new Values();
        values.add("String1");
        values.add("String2");
        values.add("String3");
        values.add("String4");
        values.add(new byte[] {1, 1, 1});

        assertTrue("The existing text value should be deleted", values.delete("String1"));
        assertTrue("The existing text value should be deleted", values.delete("String3"));
        assertFalse("The non-existing text value should be ignored", values.delete("String110"));
        assertEquals("The binary values shouldn't be affected", 1, values.getBinaryValues().size());
        assertTrue("The binary values shouldn't be affected",
                areEqual(new byte[] {1, 1, 1}, (byte[]) values.getBinaryValues().get(0)));

        // Tests the invalid arguments handling
        try {
            values.delete((String) null);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}

    }

    /**
     * Tests the <code>getTextValues()</code> method. Verifies that the method returns an original data but not a copy.
     * Also verifies that the method returns the actual text values.
     */
    public void testMethodGetTextValues() {
        Values values = new Values(new String[] {"String1", "String2", "String3"});

        assertEquals("The method should return the list of text values of expected size",
                3, values.getTextValues().size());
        values.getTextValues().add("String4");
        assertEquals("The method should return the shallow copy of modifications of expected size but not an original"
                + " list ", 3, values.getTextValues().size());
        assertEquals("The method should return an actual data", "String1", values.getTextValues().get(0));
        assertEquals("The method should return an actual data", "String2", values.getTextValues().get(1));
        assertEquals("The method should return an actual data", "String3", values.getTextValues().get(2));
    }

    /**
     * Tests the <code>setTextValues(String[])</code> method. Verifies that existing text values are replaced with
     * new ones while the binary values remain unaffected. Also tests the invalid arguments handling.
     */
    public void testMethodSetTextValues_StringArray() {
        String string1 = "String1";
        String string2 = "String2";
        String string3 = "String3";

        Values values = new Values();

        values.add(new byte[] {1, 1});
        values.add(new byte[] {2, 2});
        values.add(string1);

        values.setTextValues(new String[] {string2, string3});

        assertEquals("The binary values should remain the same", 2,values.getBinaryValues().size());
        assertTrue("The binary values should remain the same",
                areEqual(new byte[] {1,1}, (byte[]) values.getBinaryValues().get(0)));
        assertTrue("The binary values should remain the same",
                areEqual(new byte[] {2,2}, (byte[]) values.getBinaryValues().get(1)));

        assertEquals("The text values should be replaced with new values", 2,values.getTextValues().size());
        assertFalse("The previous text data should be removed", values.getTextValues().contains(string1));
        assertEquals("The new text data should be in effect", string2, values.getTextValues().get(0));
        assertEquals("The new text data should be in effect", string3, values.getTextValues().get(1));
        assertEquals("The new text data should be in effect", string2, (String) values.getTextValues().get(0));
        assertEquals("The new text data should be in effect", string3, (String) values.getTextValues().get(1));

        // Tests the invalid arguments handling
        try {
            values.setTextValues((String[]) null);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}

        try {
            values.setTextValues(new String[] {string1, string2, null});
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}
    }

    public static Test suite() {
        return new TestSuite(ValuesTest.class);
    }

    /**
     * A helper method checking if to arrays are equal, i.e. have the same number of elements and their corresponding
     * elements are equal.
     *
     * @param  element
     * @param  value
     * @return
     */
    private boolean areEqual(byte[] element, byte[] value) {
        if (element.length != value.length) {
            return false;
        }

        for (int i = 0; i < element.length; i++) {
            if (element[i] != value[i]) {
                return false;
            }
        }
        return true;
    }
}
