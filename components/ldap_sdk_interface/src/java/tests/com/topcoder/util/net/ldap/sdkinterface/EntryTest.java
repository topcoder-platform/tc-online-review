/*
 * TCS LDAP SDK Interface 1.0
 *
 * EntryTest.java
 *
 * Copyright (c), 2004. TopCoder, Inc. All rights reserved.
 *
 */
package com.topcoder.util.net.ldap.sdkinterface;

import junit.framework.*;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

/**
 * A test case testing the behavior of <code>Entry</code> class. No special settings are required to run this test.
 *
 * @author  isv
 * @version 1.0 05/18/2004
 */
public class EntryTest extends TestCase {

    /**
     * A <code>String</code> representing the name of "address" attribute of pre-configured LDAP entry.
     */
    private final static String ADDRESS_ATTR = "address";

    /**
     * A <code>String</code> representing the content of "address" attribute of pre-configured LDAP entry.
     */
    private final static String ADDRESS_DATA = "703, Hebron Avenue, Glastonburry, CT";

    /**
     * A <code>String</code> representing the name of "email" attribute of pre-configured LDAP entry.
     */
    private final static String EMAIL_ATTR = "email";

    /**
     * A <code>String</code> representing the content of "email" attribute of pre-configured LDAP entry.
     */
    private final static String EMAIL_DATA = "service@topcoder.com";

    public EntryTest(String testName) {
        super(testName);
    }

    /**
     * Tests the non-argument constructor. Verifies that the new Entry is initialized with empty DN and attribute set.
     */
    public void testConstructor() {
        Entry entry = null;

        entry = new Entry();
        assertEquals("The Entry constructed with non-argument constructor should have an empty Dn", entry.getDn(), "");
        assertNotNull("The Entry constructed with non-argument constructor should have an attribute set initialized",
                entry.getAttributes() );
        assertTrue("The Entry constructed with non-argument constructor should have an empty set of attributes",
                entry.getAttributes().size() == 0);
    }

    /**
     * Tests the constrcutor taking the String DN. Verifies that the specified DN is properly set, the attribute set is
     * initialized and is empty. Tests the handling of invalid String DN by the constructor. Also tests that the
     * constructor does not prohibits the empty DN.
     */
    public void testConstructor_String() {
        Entry entry = null;

        // Tests the valid arguments handling
        String DN = "o=TopCoder,c=US";
        entry = new Entry(DN);
        assertEquals("Should have the specified DN properly saved", entry.getDn(), DN);
        assertNotNull("Should have an attribute set initialized", entry.getAttributes() );
        assertTrue("Should have an empty set of attributes", entry.getAttributes().size() == 0);

        // Tests the empty String DN
        try {
            entry = new Entry("");
            entry = new Entry("           ");
        } catch(Exception e) {
            fail("The empty DN should not be prohibited.");
        }

        // com.topcoder.util.net.ldap.sdkinterface.netscape.Test the invalid arguments handling
        try {
            entry = new Entry((String) null);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}
    }

    /**
     * Tests the <code>deleteAttribute(String)</code> method. Verifies that the requested attribute does not exist after
     * deletion. Also tests the invalid arguments handling. Tests the deletion of existing and non-existing attributes.
     */
    public void testMethodDeleteAttribute_String() {
        Entry entry = null;
        Values values = null;

        // Tests the deletion of existing attributes
        entry = createSampleEntry();
        entry.deleteAttribute(ADDRESS_ATTR);
        try {
            values = entry.getValues(ADDRESS_ATTR);
            fail("The specified attribute should be removed from Entry");
        } catch(IllegalArgumentException e) {}

        entry.deleteAttribute(EMAIL_ATTR);
        try {
            values = entry.getValues(EMAIL_ATTR);
            fail("The specified attribute should be removed from Entry");
        } catch(IllegalArgumentException e) {}

        // Tests the deletion of non-existing attributes
        try {
            entry.deleteAttribute("Non-existing attribute");
        } catch(Exception e) {
            fail("The deletion of non-existing attributes should be performed silently.");
        }

        // Tests the invalid arguments handling
        try {
            entry.deleteAttribute((String) null);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}

        try {
            entry.deleteAttribute("");
            fail("IlegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            entry.deleteAttribute("          ");
            fail("IlegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}
    }

    /**
     * Tests the <code>getValues(String)</code> method. Creates the pre-configured <code>Entry</code> object and
     * verifies that method returns correct data, then updates the attributes of entry and verifies that method returns
     * new attribute values. Also tests the invalid arguments handling.
     */
    public void testMethodGetValues_String() {
        Entry entry = null;
        Values values = null;

        // Tests the method on pre-configured entry.
        entry = createSampleEntry();
        values = entry.getValues(EMAIL_ATTR);
        assertEquals("The method should return the existing attribute value",
                EMAIL_DATA, values.getTextValues().get(0));

        values = entry.getValues(ADDRESS_ATTR);
        assertEquals("The method should return the existing attribute value",
                ADDRESS_DATA, values.getTextValues().get(0));

        // Tests the method after modification of entry.
        entry.setAttribute(ADDRESS_ATTR, new Values("New address"));
        assertEquals("The method should return the updated attribute value",
                "New address", entry.getValues(ADDRESS_ATTR).getTextValues().get(0));

        entry.setAttribute(EMAIL_ATTR, new Values("New email"));
        assertEquals("The method should return the updated attribute value",
                "New email", entry.getValues(EMAIL_ATTR).getTextValues().get(0));

        // Tests the invalid arguments handling
        try {
            entry.getValues((String) null);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}

        try {
            entry.getValues("");
            fail("IlegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            entry.getValues("          ");
            fail("IlegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            entry.getValues("Non-existing attribute");
            fail("IlegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}
    }

    /**
     * Tests the <code>setAttributes(Map)</code> method. Verifies that specified attributes are set with specified
     * values and all previous attributes are removed. Also tests the invalid arguments handling. Uses the
     * pre-configured entry to perform the tests.
     */
    public void testMethodSetAttributes_Map() {
        Entry entry = null;
        Values values = null;
        Map attributes = null;

        entry = createSampleEntry();

        // Tests that attributes are set with new values
        attributes = new HashMap();
        attributes.put(ADDRESS_ATTR, new Values("New address"));
        attributes.put(EMAIL_ATTR, new Values("New email"));

        entry.setAttributes(attributes);

        values = entry.getValues(ADDRESS_ATTR);
        assertEquals("The attribute should be set with new value", "New address", values.getTextValues().get(0));
        assertTrue("The new attributes shouldn;t be appended to existing attributes",
                values.getTextValues().size() == 1);
        values = entry.getValues(EMAIL_ATTR);
        assertEquals("The attribute should be set with new value", "New email", values.getTextValues().get(0));
        assertTrue("The new attributes shouldn;t be appended to existing attributes",
                values.getTextValues().size() == 1);

        // Tests that existing but unspecified attributes are removed
        attributes = new HashMap();
        attributes.put(EMAIL_ATTR, new Values("Another new email"));

        entry.setAttributes(attributes);
        try {
            values = entry.getValues(ADDRESS_ATTR);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        // Tests invalid arguments handling
        try {
            entry.setAttributes((Map) null);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}

        attributes = new HashMap();
        attributes.put(null, null);

        try {
            entry.setAttributes(attributes);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}
        // Tests invalid arguments handling
        try {
            entry.setAttributes((Map) null);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}

        attributes = new HashMap();
        attributes.put(null, null);

        try {
            entry.setAttributes(attributes);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        attributes = new HashMap();
        attributes.put(null, new Values("Invalid mapping"));

        try {
            entry.setAttributes(attributes);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        attributes = new HashMap();
        attributes.put(ADDRESS_ATTR, null);

        try {
            entry.setAttributes(attributes);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        attributes = new HashMap();
        attributes.put(new Values("The values as key"), "The String as value" );

        try {
            entry.setAttributes(attributes);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}
    }

    /**
     * Tests the <code>setDn(String)</code> method. Verifies that the new DN is taken into effect. Also tests the
     * invalid arguments handling. Verifies that empty DN aren't prohibited.
     */
    public void testMethodSetDn_String() {
        Entry entry = null;

        entry = new Entry();
        entry.setDn("o=TopCoder,c=US");
        assertEquals("The new DN should be set", "o=TopCoder,c=US", entry.getDn());

        entry.setDn("");
        assertEquals("The empty DN should not be prohibited", "", entry.getDn());

        entry.setDn("  ");
        assertEquals("The empty DN should not be prohibited", "  ", entry.getDn());

        // Tests invalid arguments handling
        try {
            entry.setDn((String) null);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}
    }

    /**
     * Tests the <code>getDn()</code> method. Verifies that the method always returns the actual DN. Uses
     * pre-configured entry to perform tests. Then updates the DN of the entry and checks if <code>getDn()</code>
     * returns a new DN.
     */
    public void testMethodGetDn() {
        Entry entry = null;
        Values values = null;

        entry = createSampleEntry();

        assertEquals("", "o=TopCoder,c=US", entry.getDn());

        String TCS = "o=TopCoder Software,c=US";
        entry.setDn(TCS);
        assertEquals("", TCS, entry.getDn());
    }

    /**
     * Tests the <code>setAttribute(String, Values)</code> method. Verifies that existing attribute value is replaced
     * with new one; the non-existing attribute is added. Also tests the invalid arguments handling. Uses a
     * pre-configured entry to perform the tests.
     */
    public void testMethodSetAttribute_String_Values() {
        Entry entry = null;
        Values values = null;

        entry = createSampleEntry();

        entry.setAttribute(ADDRESS_ATTR, new Values("New address"));
        values = entry.getValues(ADDRESS_ATTR);
        assertEquals("The attribute should be set with new value", "New address", values.getTextValues().get(0));
        assertTrue("The new attributes shouldn;t be appended to existing attributes",
                values.getTextValues().size() == 1);

        entry.setAttribute("Non-existing attribute", new Values("New attribute value"));
        values = entry.getValues("Non-existing attribute");
        assertEquals("The non-existing attribute should be added", "New attribute value",
                values.getTextValues().get(0));

        // Tests invalid arguments handling
        try {
            entry.setAttribute((String) null, new Values("value"));
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}


        try {
            entry.setAttribute("Attribute name", (Values) null);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}

        try {
            entry.setAttribute((String) null, (Values) null);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}

        try {
            entry.setAttribute("", new Values("value"));
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}
    }

    /**
     * com.topcoder.util.net.ldap.sdkinterface.netscape.Test the <code>getAttributes()</code> method. Verifies that the method returns a Map mapping String keys to
     * Values objects. Also verifies that the method returns all attributes and never returns a null.
     */
    public void testMethodGetAttributes() {
        Entry entry = null;
        Values values = null;
        Map attributes = null;

        entry = createSampleEntry();

        attributes = entry.getAttributes();
        assertNotNull("The method should never return a null Map", attributes);
        assertTrue("The method should return all attributes", attributes.size() == 2);

        Iterator iterator = attributes.keySet().iterator();

        while (iterator.hasNext()) {
            Object key = iterator.next();

            if (key == null) {
                throw new IllegalArgumentException("The method should return the Map without null keys");
            }

            if (!(key instanceof String)) {
                throw new IllegalArgumentException("The method should return the Map without non-String keys");
            }

            Object value = attributes.get(key);

            if (value == null) {
                throw new IllegalArgumentException("The method should return the Map without null values");
            }

            if (!(value instanceof Values)) {
                throw new IllegalArgumentException("The method should return the Map without non-Values values");
            }
        }

    }

    public static Test suite() {
        return new TestSuite(EntryTest.class);
    }

    /**
     * A helper method creating an <code>Entry</code> object configured with sample set of attributes.
     *
     * @return a new instance of pre-configured <code>Entry</code> object.
     */
    private Entry createSampleEntry() {
        Values values = null;
        Entry entry = new Entry("o=TopCoder,c=US");

        values = new Values(ADDRESS_DATA);
        entry.setAttribute(ADDRESS_ATTR, values);

        values = new Values(EMAIL_DATA);
        entry.setAttribute(EMAIL_ATTR, values);

        return entry;
    }
}
