/**
 *
 * Copyright ?2004, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.net.ldap.sdkinterface.accuracytests;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;
import com.topcoder.util.net.ldap.sdkinterface.Entry;
import com.topcoder.util.net.ldap.sdkinterface.Values;

import java.util.Map;
import java.util.HashMap;

/**
 * Tests accuracy of various methods in the Entry class
 *
 * @author BryanChen
 * @version 1.0
 */
public class EntryAccuracyTests extends TestCase {

    /** instance of an Entry */
    private Entry entry;

    /**
     * Sets up the instance of the entry, just calls a blank constructor
     */
    protected void setUp() {
        entry = new Entry();
    }

    /**
     * Tests the accuracy of getDn
     */
    public void testEntryGetDnAccuracy() {
        assertEquals("incorrect default dn", "", entry.getDn());

        entry.setDn("hi");
        assertEquals("incorrect dn", "hi", entry.getDn());
    }

    /**
     * Tests the accuracy of various methods related to attributes
     */
    public void testEntryAttributesAccuracy() {
        assertEquals(0, entry.getAttributes().size());

        Values value = new Values("dude");
        entry.setAttribute("hola", value);
        assertEquals(1, entry.getAttributes().size());

        entry.setAttribute("hi", value);
        assertEquals(2, entry.getAttributes().size());

        assertTrue(entry.getAttributes().containsKey("hi"));
        assertTrue(entry.getAttributes().containsKey("hola"));
        assertTrue(entry.getAttributes().containsValue(value));

        entry.deleteAttribute("hi");
        entry.deleteAttribute("hola");
        assertEquals(0, entry.getAttributes().size());

        Map map = new HashMap();
        map.put("hi", value);
        entry.setAttributes(map);
        assertEquals(1, entry.getAttributes().size());
        assertTrue(entry.getAttributes().containsKey("hi"));
        assertTrue(entry.getAttributes().containsValue(value));
        assertFalse(entry.getAttributes().containsKey("hola"));
    }

    /**
     * Returns the suite of tests
     * @return the suite of tests
     */
    public static Test suite() {
        return new TestSuite(EntryAccuracyTests.class);
    }
}
