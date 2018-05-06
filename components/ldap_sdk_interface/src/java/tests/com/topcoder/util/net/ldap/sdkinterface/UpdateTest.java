/*
 * TCS LDAP SDK Interface 1.0
 *
 * UpdateTest.java
 *
 * Copyright (c), 2004. TopCoder, Inc. All rights reserved.
 *
 */
package com.topcoder.util.net.ldap.sdkinterface;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

import java.util.List;

/**
 * A test case testing the behavior of <code>Update</code> class. No special settings are required to run this
 * test.
 *
 * @author  isv
 * @version 1.0 05/18/2004
 */
public class UpdateTest extends TestCase {

    public UpdateTest(String testName) {
        super(testName);
    }

    /**
     * Tests the default public constructor. Since this is a default constructor the test simply verifies that nothing
     * prevents the instantiation of <code>Update</code> object.
     */
    public void testConstructor() {
        Update update = null;
        try {
            update = new Update();
        } catch(Exception e) {
            fail("There shouldn't be any reasons preventing the successful instantiation of Update");
        }
    }

    /**
     * Tests the <code>delete(String)</code> method. Verifies that <code>Update</code> contains an appropriate <code>
     * Modification</code> object after method execution. Also tests the invalid arguments handling.
     */
    public void testMethodDelete_String() {
        List modifications = null;
        Modification modification = null;
        Update update = null;
        update = new Update();

        update.delete("attribute name");
        modifications = update.getModifications();
        modification = (Modification) modifications.get(0);

        assertNotNull("The Update object should contain a Modification object", modification);
        assertEquals("The Modification object should have the attribute name as specified",
                "attribute name", modification.getAttributeName());
        assertEquals("The Modificiation object should have the type set to appropriate value",
                Modification.DELETE_ATTRIBUTE, modification.getType());
        assertNull("The Modification object should not have a Values object initialized", modification.getValues());

        // Tests the invalid arguments handling
        try {
            update.delete((String) null);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}

        try {
            update.delete("");
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            update.delete("     ");
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}
    }

    /**
     * Tests the <code>delete(String, Values)</code> method. Verifies that <code>Update</code> contains an appropriate
     * <code>Modification</code> object after method execution. Also tests the invalid arguments handling.
     */
    public void testMethodDelete_String_Values() {
        List modifications = null;
        Modification modification = null;
        Update update = null;
        Values values = null;

        update = new Update();
        values = new Values("attribute value");

        update.delete("attribute name", values);
        modifications = update.getModifications();
        modification = (Modification) modifications.get(0);

        assertNotNull("The Update object should contain a Modification object", modification);
        assertEquals("The Modification object should have the attribute name as specified",
                "attribute name", modification.getAttributeName());
        assertEquals("The Modificiation object should have the type set to appropriate value",
                Modification.DELETE_VALUE, modification.getType());
        assertNotNull("The Modification object should have a Values object initialized", modification.getValues());
        assertTrue("The Modification object should have been initialized with the same instance of Values",
                values == modification.getValues());
        assertEquals("The Modification object shouldn't have modify the Values object", values.getTextValues().get(0),
                modification.getValues().getTextValues().get(0));

        // Tests the invalid arguments handling
        try {
            update.delete((String) null, values);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}

        try {
            update.delete((String) null, (Values) null);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}

        try {
            update.delete("attribute name", (Values) null);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}

        try {
            update.delete("", values);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            update.delete("     ", values);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}
    }

    /**
     * Tests the <code>getModifications</code> method. Verifies that the method returns the original list of the
     * modifications but not the copy.
     */
    public void testMethodGetModifications() {
        List modifications = null;
        Modification modification = null;
        Update update = null;
        Values values = null;

        update = new Update();
        values = new Values("value of the attribute");

        update.delete("attribute to delete");
        update.delete("attribute to delete value from", values);
        update.add("another attribute", values);

        modifications = update.getModifications();

        assertEquals("The method should return the list of modifications of expected size", 3, modifications.size());
        modifications.add(new Modification(Modification.REPLACE, "attribute name", values));
        assertEquals("The method should return the original list of modifications of expected size but not a copy",
                4, modifications.size());
    }

    /**
     * Tests the <code>add(String, Values)</code> method. Verifies that <code>Update</code> contains an appropriate
     * <code>Modification</code> object after method execution. Also tests the invalid arguments handling.
     */
    public void testMethodAdd_String_Values() {
        List modifications = null;
        Modification modification = null;
        Update update = null;
        Values values = null;

        update = new Update();
        values = new Values("attribute value");

        update.add("attribute name", values);
        modifications = update.getModifications();
        modification = (Modification) modifications.get(0);

        assertNotNull("The Update object should contain a Modification object", modification);
        assertEquals("The Modification object should have the attribute name as specified",
                "attribute name", modification.getAttributeName());
        assertEquals("The Modificiation object should have the type set to appropriate value",
                Modification.ADD, modification.getType());
        assertNotNull("The Modification object should have a Values object initialized", modification.getValues());
        assertTrue("The Modification object should have been initialized with the same instance of Values",
                values == modification.getValues());
        assertEquals("The Modification object shouldn't have modify the Values object", values.getTextValues().get(0),
                modification.getValues().getTextValues().get(0));

        // Tests the invalid arguments handling
        try {
            update.add((String) null, values);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}

        try {
            update.add((String) null, (Values) null);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}

        try {
            update.add("attribute name", (Values) null);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}

        try {
            update.add("", values);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            update.add("     ", values);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}
    }

    /**
     * Tests the <code>replace(String, Values)</code> method. Verifies that <code>Update</code> contains an appropriate
     * <code>Modification</code> object after method execution. Also tests the invalid arguments handling.
     */
    public void testMethodReplace_String_Values() {
        List modifications = null;
        Modification modification = null;
        Update update = null;
        Values values = null;

        update = new Update();
        values = new Values("attribute value");

        update.replace("attribute name", values);
        modifications = update.getModifications();
        modification = (Modification) modifications.get(0);

        assertNotNull("The Update object should contain a Modification object", modification);
        assertEquals("The Modification object should have the attribute name as specified",
                "attribute name", modification.getAttributeName());
        assertEquals("The Modificiation object should have the type set to appropriate value",
                Modification.REPLACE, modification.getType());
        assertNotNull("The Modification object should have a Values object initialized", modification.getValues());
        assertTrue("The Modification object should have been initialized with the same instance of Values",
                values == modification.getValues());
        assertEquals("The Modification object shouldn't have modify the Values object", values.getTextValues().get(0),
                modification.getValues().getTextValues().get(0));

        // Tests the invalid arguments handling
        try {
            update.add((String) null, values);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}

        try {
            update.add((String) null, (Values) null);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}

        try {
            update.add("attribute name", (Values) null);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}

        try {
            update.add("", values);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            update.add("     ", values);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}
    }


    public static Test suite() {
        return new TestSuite(UpdateTest.class);
    }
}
