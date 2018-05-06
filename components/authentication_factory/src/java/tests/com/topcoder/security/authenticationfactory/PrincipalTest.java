/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.security.authenticationfactory;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * The class contain testcase for test <code>Principal</code>.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class PrincipalTest extends TestCase {

    /**
     * <p>The Principal instance.</p>
     */
    private Principal principal = null;

    /**
     * <p>The object id for Principal.</p>
     */
    private Object id = new String("a obj");

    /**
     * <p>
     * Creates a test suite of the tests contained in this class.
     * </p>
     *
     * @return a test suite of the tests contained in this class.
     */
    public static Test suite() {
        return new TestSuite(PrincipalTest.class);
    }

    /**
     * <p>Setup the fixture.</p>
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        principal = new Principal(id);
    }

    /**
     * <p>Test constructor with null id, should throw NullPointerException.</p>
     */
    public void testPrincipal() {
        try {
            Principal p = new Principal(null);
            fail("should throw NullPointerException");
        } catch (NullPointerException npe) {
            // pass
        }
    }

    /**
     * <p>Test getId method.</p>
     */
    public void testGetId() {
        assertEquals("the two object should be equal", principal.getId(), id);
    }

    /**
     * <p>Test addMapping method, pass it with null parameter, should throw NullPointerException.</p>
     */
    public void testAddMappingNPE() {
        String value1 = "value1";
        try {
            principal.addMapping(null, value1);
            fail("null 'key' is not acceptable, should throw NullPointerException");
        } catch (NullPointerException e) {
            // pass
        }
    }

    /**
     * <p>Test addMapping method, pass it with null parameter, should throw NullPointerException.</p>
     */
    public void testAddMappingNPE2() {
        String key1 = "key1";
        try {
            principal.addMapping(key1, null);
            fail("null 'value' is not acceptable, should throw NullPointerException");
        } catch (NullPointerException e) {
            // pass
        }
    }

    /**
     * <p>Test addMapping method, pass it with empty 'key' parameter, should
     * throw IllegalArgumentException.</p>
     */
    public void testAddMappingIAE() {
        try {
            principal.addMapping(" ", "value");
            fail("empty 'key' is not acceptable, should throw IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * <p>Test accuracy functionality of addMapping, getValue, containsKey, removeMapping method.</p>
     */
    public void testAddMapping() {
        String key1 = "key1";
        String value1 = "value1";

        assertFalse("should not contain key " + key1, principal.containsKey(key1));
        assertNull("the value of key " + key1 + " is null", principal.getValue(key1));

        principal.addMapping(key1, value1);
        assertEquals("should contains value " + value1, principal.getValue(key1), value1);
        assertTrue("should contain key " + key1, principal.containsKey(key1));

        // test replaces the existing value with new one
        String newValue1 = "new value";
        principal.addMapping(key1, newValue1);
        assertTrue("now the value of key " + key1 + "has been changed", principal.getValue(key1) == newValue1);

        principal.removeMapping(key1);

        assertFalse("should not contain key " + key1 + " after remove mapping", principal.containsKey(key1));
        assertNull("the value of key " + key1 + " is null after remove mapping", principal.getValue(key1));
    }

    /**
     * <p>Test RemoveMapping method, pass it with null parameter, should throw NullPointerException.</p>
     */
    public void testRemoveMappingNPE() {
        try {
            principal.removeMapping(null);
            fail("null 'key' is not acceptable, should throw NullPointerException");
        } catch (NullPointerException e) {
            // pass
        }
    }

    /**
     * <p>Test RemoveMapping method, pass it with empty parameter, should throw IllegalArgumentException.</p>
     */
    public void testRemoveMappingIAE() {
        try {
            principal.removeMapping("  ");
            fail("empty 'key' is not acceptable, should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>Test getValue with null parameter, should throw NullPointerException.</p>
     */
    public void testGetValueNPE() {
        try {
            principal.getValue(null);
            fail("null 'key' is not acceptable, should throw NullPointerException");
        } catch (NullPointerException e) {
            // pass
        }
    }

    /**
     * <p>Test getValue with empty String, should throw IllegalArgumentException.</p>
     */
    public void testGetValueIAE() {
        try {
            principal.getValue(" ");
            fail("empty 'key' is not acceptable, should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>Test getKeys and clearMappings method.</p>
     */
    public void testGetKeys() {
        String key1 = "key1";
        String value1 = "value1";
        String key2 = "key2";
        String value2 = "value2";

        principal.addMapping(key1, value1);
        principal.addMapping(key2, value2);
        List keys = principal.getKeys();

        assertTrue("keys should contain " + key1, keys.contains(key1));
        assertTrue("keys should contain " + key2, keys.contains(key2));

        principal.clearMappings();
        keys = principal.getKeys();
        assertTrue("now keys should be empty", keys.size() == 0);
    }

    /**
     * <p>Test containsKey, pass it with null parameter, should throw NullPointerException.</p>
     */
    public void testContainsKeyNPE() {
        try {
            principal.containsKey(null);
            fail("null 'key' is not acceptable, should throw NullPointerException");
        } catch (NullPointerException e) {
            // pass
        }
    }

    /**
     * <p>Test containsKey, pass it with empty String, should throw IllegalArgumentException.</p>
     */
    public void testContainsKeyIAE() {
        try {
            principal.containsKey(" ");
            fail("empty 'key' is not acceptable, should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test clearMappings functionality.
     */
    public void testClearMappings() {
        principal.addMapping("key", "value");
        List keys = principal.getKeys();
        assertTrue("the list's size is not zero", keys.size() != 0);
        principal.clearMappings();
        keys = principal.getKeys();
        assertTrue("the list's size is zero", keys.size() == 0);
    }
}
